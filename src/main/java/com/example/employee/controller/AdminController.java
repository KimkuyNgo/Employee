package com.example.employee.controller;

import com.example.employee.module.Admin;
import com.example.employee.module.Employee;
import com.example.employee.repository.AdminRepository;
import com.example.employee.repository.EmployeeRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class AdminController {

    @Autowired private AdminRepository adminRepo;
    @Autowired private EmployeeRepository employeeRepo;
    @Autowired private BCryptPasswordEncoder encoder;


    @GetMapping("/admin/register")
    public String showAdminRegister() {
        return "admin-register"; // Points to admin-addemployee.html
    }

    @GetMapping("/")
    public String showHomePage() {
        return "homepage"; // This opens index.html
    }

    @GetMapping("/admin/login")
    public String showAdminLogin() {
        return "admin-login"; // Points to admin-login.html
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Clears the adminName and all session data
        return "redirect:/admin/login"; // Sends them back to the login page
    }


    @PostMapping("/admin/login")
    public String loginAdmin(@RequestParam String email, @RequestParam String password, HttpSession session) {
        Optional<Admin> admin = adminRepo.findByEmail(email);

        if (admin.isPresent() && encoder.matches(password, admin.get().getPassword())) {
            // Store both Name and the Admin Object/ID
            session.setAttribute("adminName", admin.get().getUsername());
            session.setAttribute("loggedInAdmin", admin.get());
            return "redirect:/dashboard";
        }
        return "redirect:/admin/login?error";
    }


    // ADMIN REGISTRATION
    @PostMapping("/admin/register")
    public String registerAdmin(Admin admin) {
        admin.setPassword(encoder.encode(admin.getPassword()));
        adminRepo.save(admin); // Saves to 'admins' table
        return "redirect:/admin/login";
    }

    @PostMapping("/saveEmployee")
    public String saveEmployee(Employee emp, HttpSession session) {
        Admin currentAdmin = (Admin) session.getAttribute("loggedInAdmin");

        if (currentAdmin == null) {
            return "redirect:/admin/login";
        }

        emp.setAdmin(currentAdmin); // Link the new employee to this admin

        if (emp.getPassword() != null) {
            emp.setPassword(encoder.encode(emp.getPassword()));
        }
        employeeRepo.save(emp);
        return "redirect:/dashboard";
    }
}
