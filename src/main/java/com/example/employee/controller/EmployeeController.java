package com.example.employee.controller;

import com.example.employee.model.Employee;
import com.example.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeRepository repo;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @GetMapping("/register")
    public String showRegister() { return "register"; }

    @PostMapping("/register")
    public String register(Employee emp) {
        emp.setPassword(encoder.encode(emp.getPassword()));
        repo.save(emp);
        return "redirect:/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        Employee emp = repo.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("employee", emp);
        return "dashboard";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}