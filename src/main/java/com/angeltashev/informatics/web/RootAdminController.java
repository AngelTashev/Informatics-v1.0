package com.angeltashev.informatics.web;

import com.angeltashev.informatics.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/root-admin-panel")
@PreAuthorize("hasRole('ROLE_ROOT_ADMIN')")
public class RootAdminController {

    private final UserService userService;

    @PreAuthorize("hasRole('ROLE_ROOT_ADMIN')")
    @GetMapping
    public String getRootAdminPanel() {
        return "root/root-admin-panel";
    }


    // Admins

    @PreAuthorize("hasRole('ROLE_ROOT_ADMIN')")
    @GetMapping("/admins")
    public String getAdminsView() {
        return "root/admins-details";
    }

    @PreAuthorize("hasRole('ROLE_ROOT_ADMIN')")
    @GetMapping("/admins/demote-admin/{id}")
    public String demoteAdminById(@PathVariable("id") String adminId) {
        log.info("Demote admin by id (controller): Requested demotion of admin with id: " + adminId);
        this.userService.demoteAdminById(adminId);
        return "redirect:/root-admin-panel/admins";
    }


    // Users

    @PreAuthorize("hasRole('ROLE_ROOT_ADMIN')")
    @GetMapping("/students")
    public String getStudentsView() {
        return "root/students-details";
    }

    @PreAuthorize("hasRole('ROLE_ROOT_ADMIN')")
    @GetMapping("/students/promote-student/{id}")
    public String promoteStudentById(@PathVariable("id") String studentId) {
        log.info("Promote student by id (controller): Requested promotion of student with id: " + studentId);
        this.userService.promoteStudentById(studentId);
        return "redirect:/root-admin-panel/students";
    }
}
