package com.angeltashev.informatics.web;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@AllArgsConstructor
@Controller
public class UserController {

    @GetMapping("login")
    public String getLoginForm() {
        return "login/login";
    }

    @PostMapping("login")
    public String confirmLoginForm() {
        return "redirect:/";
    }
}
