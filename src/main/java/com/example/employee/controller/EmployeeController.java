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

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeRepository repo;


    @Autowired
    private BCryptPasswordEncoder encoder;


    // Example for the /addemployee page in EmployeeController
    @GetMapping("/addemployee")
    public String showAddEmployee(HttpSession session) {
        if (session.getAttribute("adminName") == null) {
            return "redirect:/admin/login";
        }
        return "addemployee";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model, @RequestParam(name = "keyword", required = false) String keyword) {
        Admin currentAdmin = (Admin) session.getAttribute("loggedInAdmin");

        if (currentAdmin == null) {
            return "redirect:/admin/login";
        }

        List<Employee> list;
        if (keyword != null && !keyword.trim().isEmpty()) {
            list = repo.findByNameContainingIgnoreCaseAndAdmin(keyword, currentAdmin);
        } else {
            list = repo.findByAdmin(currentAdmin);
        }

        double totalPayroll = list.stream()
                .mapToDouble(e -> e.getSalary() != null ? e.getSalary() : 0)
                .sum();

        model.addAttribute("employees", list);
        model.addAttribute("totalPayroll", totalPayroll); // THIS connects to th:text="${totalPayroll}"
        model.addAttribute("keyword", keyword);

        return "dashboard";
    }


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, HttpSession session, Model model) {
        // SECURITY CHECK: Redirect to login if not authenticated
        if (session.getAttribute("adminName") == null) {
            return "redirect:/admin/login";
        }

        Employee emp = repo.findById(id).orElseThrow();
        model.addAttribute("employee", emp);
        return "edit-employee";
    }

    @PostMapping("/update")
    public String updateEmployee(@ModelAttribute Employee emp, HttpSession session) {
        if (session.getAttribute("adminName") == null) {
            return "redirect:/admin/login";
        }

        Employee existingEmp = repo.findById(emp.getId()).orElseThrow();

        // Update fields
        existingEmp.setName(emp.getName());

        // FIX: Prevent negative salary during update
        if (emp.getSalary() == null || emp.getSalary() < 0.0) {
            existingEmp.setSalary(0.0);
        } else {
            existingEmp.setSalary(emp.getSalary());
        }

        Admin currentAdmin = (Admin) session.getAttribute("loggedInAdmin");
        existingEmp.setAdmin(currentAdmin);

        repo.save(existingEmp);
        return "redirect:/dashboard";
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable("id") Long id, HttpSession session) {
        // SECURITY CHECK
        if (session.getAttribute("adminName") == null) {
            return "redirect:/admin/login";
        }

        repo.deleteById(id);
        return "redirect:/dashboard";
    }

}