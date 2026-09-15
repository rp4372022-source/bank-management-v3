package com.bank.bms.view;

import com.bank.bms.account.Account;
import com.bank.bms.account.AccountRepository;
import com.bank.bms.user.User;
import com.bank.bms.user.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ViewController {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

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

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        // 1. Security Check: If no user is logged in, kick them back to login page
        Long userId = (Long) session.getAttribute("LOGGED_IN_USER");
        if (userId == null) {
            return "redirect:/login";
        }

        // 2. Fetch the user's data from MySQL
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3. Fetch all bank accounts belonging to this specific user
        // Note: We use a custom method or look them up by the user entity
        List<Account> accounts = accountRepository.findAll().stream()
                .filter(acc -> acc.getUser().getId().equals(userId))
                .toList();

        // 4. Send the Java data over to the HTML page via the Spring Model
        model.addAttribute("currentUser", user);
        model.addAttribute("userAccounts", accounts);

        return "dashboard"; // Looks for templates/dashboard.html
    }
}
