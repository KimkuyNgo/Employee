package com.example.employee.controller;

import com.example.employee.module.Admin;
import com.example.employee.module.Employee;
import com.example.employee.repository.AdminRepository;
import com.example.employee.repository.EmployeeRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeRepository repo;

    @Autowired
    private AdminRepository adminRepo;

    @Autowired
    private BCryptPasswordEncoder encoder;



    @GetMapping("/register")
    public String showRegister() { return "register"; }



    @PostMapping("/register")
    public String register(Employee emp, RedirectAttributes redirectAttributes) {
        try {
            // 1. Check if email already exists
            if (repo.findByEmail(emp.getEmail()).isPresent()) {
                return "redirect:/register?alreadyExists";
            }

            // 2. Encode password (uncomment when ready)
            if (emp.getPassword() != null) {
                emp.setPassword(encoder.encode(emp.getPassword()));
            }

            repo.save(emp);
            return "redirect:/dashboard";
        } catch (Exception e) {
            System.err.println("Registration failed: " + e.getMessage());
            return "redirect:/register?error";
        }
    }

    @PostMapping("/admin/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session) {
        // Look for admin by email
        Optional<Admin> admin = adminRepo.findByEmail(email);

        if (admin.isPresent() && encoder.matches(password, admin.get().getPassword())) {
            // Store the username in the session to show on the dashboard
            session.setAttribute("adminName", admin.get().getUsername());
            return "redirect:/dashboard";
        }
        return "redirect:/admin/login?error";
    }



    @GetMapping({"/","/dashboard"})
    public String dashboard(HttpSession session, Model model, @RequestParam(name = "keyword", required = false) String keyword) {
        // 1. Security Check FIRST
        if (session.getAttribute("adminName") == null) {
            return "redirect:/admin/login";
        }

        // 2. Search Logic
        List<Employee> list = (keyword != null && !keyword.trim().isEmpty())
                ? repo.findByNameContainingIgnoreCase(keyword)
                : repo.findAll();

        // 3. Calculation Logic
        double totalPayroll = list.stream()
                .mapToDouble(e -> e.getSalary() != null ? e.getSalary() : 0)
                .sum();

        model.addAttribute("employees", list);
        model.addAttribute("totalPayroll", totalPayroll);
        model.addAttribute("keyword", keyword);
        return "dashboard";
    }


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Employee emp = repo.findById(id).orElseThrow();
        model.addAttribute("employee", emp);
        return "edit-employee"; // We will create this file
    }

    @PostMapping("/update")
    public String updateEmployee(@ModelAttribute Employee emp) {
        // Keep the old password if not changing it, or re-encode if it's new
        repo.save(emp);
        return "redirect:/dashboard";
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable("id") Long id) {
        repo.deleteById(id);
        return "redirect:/dashboard";
    }

}