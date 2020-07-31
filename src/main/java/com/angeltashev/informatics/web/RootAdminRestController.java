package com.angeltashev.informatics.web;

import com.angeltashev.informatics.user.model.view.UserRoleViewModel;
import com.angeltashev.informatics.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/root-admin-panel")
@PreAuthorize("hasRole('ROLE_ROOT_ADMIN')")
public class RootAdminRestController {

    private final UserService userService;

    @PreAuthorize("hasRole('ROLE_ROOT_ADMIN')")
    @GetMapping("/admins-rest")
    public List<UserRoleViewModel> getAllAdmins() {
        return this.userService.getAllAdmins();
    }

    @PreAuthorize("hasRole('ROLE_ROOT_ADMIN')")
    @GetMapping("/students-rest")
    public List<UserRoleViewModel> getAllStudents() {
        return this.userService.getAllStudents();
    }

}
