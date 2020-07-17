package com.angeltashev.informatics.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {

    @GetMapping
    public String getIndex(Model model) {
        model.addAttribute("username", "Ivan");
        return "index/index";
    }

    @GetMapping("/home")
    public String getHome(@AuthenticationPrincipal Principal principal, Model model) {
        model.addAttribute("user", principal);
        return "index/home";
    }
}
