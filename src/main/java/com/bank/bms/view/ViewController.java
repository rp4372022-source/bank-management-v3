package com.bank.bms.view;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ViewController {

    @GetMapping("/login")
    public String showLoginPage(HttpSession session) {
        // If already logged in, skip login page and send straight to dashboard (to be built)
        if (session.getAttribute("LOGGED_IN_USER") != null) {
            return "redirect:/dashboard";
        }
        return "login"; // Looks for templates/login.html
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Clear out the session memory
        return "redirect:/login";
    }
}
