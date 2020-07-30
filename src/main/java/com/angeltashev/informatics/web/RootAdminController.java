package com.angeltashev.informatics.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/root-admin-panel")
public class RootAdminController {

    @PreAuthorize("hasRole('ROLE_ROOT_ADMIN')")
    @GetMapping
    public String getRootAdminPanel() {
        return "/root/root-admin-panel";
    }

}
