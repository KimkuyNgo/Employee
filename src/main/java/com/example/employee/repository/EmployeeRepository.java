package com.example.employee.repository;

import com.example.employee.module.Admin;
import com.example.employee.module.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // Find only employees belonging to a specific admin
    List<Employee> findByAdmin(Admin admin);

    // Search within only that admin's employees
    List<Employee> findByNameContainingIgnoreCaseAndAdmin(String name, Admin admin);
}