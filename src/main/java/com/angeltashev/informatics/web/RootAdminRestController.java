package com.angeltashev.informatics.web;

import com.angeltashev.informatics.messages.model.view.MessageViewModel;
import com.angeltashev.informatics.messages.service.MessageService;
import com.angeltashev.informatics.user.model.view.UserRoleViewModel;
import com.angeltashev.informatics.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/root-admin-panel")
@PreAuthorize("hasRole('ROLE_ROOT_ADMIN')")
public class RootAdminRestController {

    private final UserService userService;
    private final MessageService messageService;

    @PreAuthorize("hasRole('ROLE_ROOT_ADMIN')")
    @GetMapping("/admins-rest")
    public List<UserRoleViewModel> getAllAdmins() {
        log.info("Get all admins (rest): Loading async data into all admins table");
        return this.userService.getAllAdmins();
    }

    @PreAuthorize("hasRole('ROLE_ROOT_ADMIN')")
    @GetMapping("/students-rest")
    public List<UserRoleViewModel> getAllStudents() {
        log.info("Get all students (rest): Loading async data into all students table");
        return this.userService.getAllStudents();
    }

    @PreAuthorize("hasRole('ROLE_ROOT_ADMIN')")
    @GetMapping("/messages-rest")
    public List<MessageViewModel> getMessages() {
        log.info("Get all messages (rest): Loading async data into messages table");
        return this.messageService.getAllMessages();
    }

}
