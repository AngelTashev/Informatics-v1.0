package com.angeltashev.informatics.web;

import com.angeltashev.informatics.user.model.view.UserHomeViewModel;
import com.angeltashev.informatics.user.repository.UserRepository;
import com.angeltashev.informatics.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@AllArgsConstructor
@Controller
public class HomeController {

    private final UserService userService;

    @GetMapping
    public String getIndex() {
        return "index/index";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/home")
    public String getHome(@AuthenticationPrincipal Principal principal, Model model) {
        model.addAttribute("user", principal);
        UserHomeViewModel userHomeViewModel = this.userService.getUserHomeDetails(principal.getName());
        model.addAttribute("userHomeView", userHomeViewModel);
        return "index/home";
    }
}
