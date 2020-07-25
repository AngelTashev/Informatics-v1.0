package com.angeltashev.informatics.web;

import com.angeltashev.informatics.assignment.model.AssignmentAddBindingModel;
import com.angeltashev.informatics.assignment.model.view.AssignmentDetailsViewModel;
import com.angeltashev.informatics.assignment.model.view.AssignmentHomeViewModel;
import com.angeltashev.informatics.assignment.service.AssignmentService;
import com.angeltashev.informatics.constants.ApplicationParameters;
import com.angeltashev.informatics.exceptions.PageNotFoundException;
import com.angeltashev.informatics.file.exception.FileStorageException;
import com.angeltashev.informatics.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

import static com.angeltashev.informatics.constants.ApplicationParameters.BINDING_RESULT_PATH;

@AllArgsConstructor
@Controller
@RequestMapping("/users/my-profile/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public String getAssignmentById(@PathVariable("id") String id, Principal principal, Model model) {
        AssignmentDetailsViewModel assignmentView = this.assignmentService.getAssignmentByIdAndUser(id, principal.getName());
        if (assignmentView == null) {
            throw new PageNotFoundException("Page cannot be found!");
        }
        model.addAttribute("assignmentView", assignmentView);
        return "assignment/assignment-details";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}")
    public String uploadSubmission(@RequestParam("submission") MultipartFile submission, @PathVariable("id") String assignmentId) throws FileStorageException {
        this.assignmentService.uploadSubmission(assignmentId, submission);
        return "redirect:/home";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/add")
    public String getAddAssignmentForm(Model model) {
        if (!model.containsAttribute("assignmentModel") |
                !model.containsAttribute("usernameModel")) {
            model.addAttribute("assignmentModel", new AssignmentAddBindingModel());
            model.addAttribute("usernameModel", this.userService.getUserAssignmentAddModels());
        }
        return "assignment/assignment-add";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public String postAddAssignment(@Valid @ModelAttribute("assignmentModel") AssignmentAddBindingModel assignmentModel,
                                    BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("assignmentModel", assignmentModel);
            redirectAttributes.addFlashAttribute(BINDING_RESULT_PATH + "assignmentModel", bindingResult);
            System.out.println();
            return "redirect:/users/my-profile/assignments/add";
        }
        this.assignmentService.addAssignment(assignmentModel);
        return "redirect:/home";
    }
}