package com.angeltashev.informatics.web;

import com.angeltashev.informatics.assignment.model.view.AssignmentDetailsViewModel;
import com.angeltashev.informatics.assignment.model.view.AssignmentHomeViewModel;
import com.angeltashev.informatics.assignment.service.AssignmentService;
import com.angeltashev.informatics.exceptions.PageNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@AllArgsConstructor
@Controller
@RequestMapping("/users/my-profile/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    @GetMapping("/{id}")
    public String getAssignmentById(@PathVariable("id") String id, Principal principal, Model model) {
        AssignmentDetailsViewModel assignmentView = this.assignmentService.getAssignmentByIdAndUser(id, principal.getName());
        if (assignmentView == null) {
            throw new PageNotFoundException("Page cannot be found!");
        }
        model.addAttribute("assignmentView", assignmentView);
        return "assignment/assignment-details";
    }
}
