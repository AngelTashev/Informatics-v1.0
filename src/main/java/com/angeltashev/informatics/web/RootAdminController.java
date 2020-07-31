package com.angeltashev.informatics.web;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/root-admin-panel")
@PreAuthorize("hasRole('ROLE_ROOT_ADMIN')")
public class RootAdminController {

    @GetMapping
    public String getRootAdminPanel() {
        return "root/root-admin-panel";
    }

    @GetMapping("/admins")
    public String getAdminsView() {
        return "root/admins-details";
    }

    @GetMapping("/students")
    public String getStudentsView() {
        return "root/students-details";
    }
}
