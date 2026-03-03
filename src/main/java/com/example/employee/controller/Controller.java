package com.example.employee.controller;

import ch.qos.logback.core.model.Model;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@org.springframework.stereotype.Controller
@Slf4j
public class Controller {

    @GetMapping({"register.html"})
    public String view() {
        return "register";
    }


}
