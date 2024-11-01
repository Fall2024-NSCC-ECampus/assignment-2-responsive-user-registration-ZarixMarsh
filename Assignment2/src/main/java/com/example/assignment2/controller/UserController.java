package com.example.assignment2.controller;

import com.example.assignment2.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.assignment2.controller.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Show registration form
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // Return registration template
    }

    // Process registration form submission
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register"; // Return to registration page if there are errors
        }
        try {
            userService.registerUser(user); // Register the user
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage()); // Add error message
            return "register"; // Return to registration page if there's an error
        }
        return "redirect:/login"; // Redirect to login after registration
    }

    // Show login form
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // Returns the login template
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute("user") User user, HttpSession session, Model model) {
        if (userService.validateUser(user)) {
            session.setAttribute("username", user.getUsername());
            return "redirect:/home"; // Redirect to home page after successful login
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login"; // Return to login template
        }
    }

    @GetMapping("/home")
    public String showHomePage(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login"; // Redirect to login if not logged in
        }
        model.addAttribute("username", username);
        return "home"; // Returns the home template
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Invalidate the session to log out
        return "redirect:/login"; // Redirect to login page after logout
    }
}
