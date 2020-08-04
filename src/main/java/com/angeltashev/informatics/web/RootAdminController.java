package com.angeltashev.informatics.web;

import com.angeltashev.informatics.message.service.MessageService;
import com.angeltashev.informatics.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/root-admin-panel")
@PreAuthorize("hasRole('ROLE_ROOT_ADMIN')")
public class RootAdminController {

    private final UserService userService;
    private final MessageService messageService;

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

    @PreAuthorize("hasRole('ROLE_ROOT_ADMIN')")
    @GetMapping("/messages")
    public String getMessagesView() {
        return "root/messages-details";
    }

    @PreAuthorize("hasRole('ROLE_ROOT_ADMIN')")
    @GetMapping("/messages/delete/{id}")
    public String deleteMessageById(@PathVariable("id") String messageId) {
        log.info("Delete message by id (controller): Requested deletion of message with id: " + messageId);
        this.messageService.deleteMessageById(messageId);
        return "redirect:/root-admin-panel/messages";
    }

    @PreAuthorize("hasRole('ROLE_ROOT_ADMIN')")
    @GetMapping("/messages/delete/all")
    public String deleteAllMessages() {
        log.info("Delete all messages (controller): Requested deletion of all messages");
        this.messageService.deleteAllMessages();
        return "redirect:/root-admin-panel/messages";
    }
}
