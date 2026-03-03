package com.example.employee.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
@Slf4j
public class EmployeeController {

    @GetMapping({"register.html"})
    public String showregister() {
        return "register";
    }


}
