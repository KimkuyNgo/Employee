package com.example.employee.controller;

import ch.qos.logback.core.model.Model;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
public class EmployeeController {

    @Autowired
    private repoInterface.EmployeeRepository repo;

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
        Optional<Employee> emp = repo.findByEmail(principal.getName());
        model.addAttribute("employee", emp);
        return "dashboard";
    }
}
