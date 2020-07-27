package com.angeltashev.informatics.web;

import com.angeltashev.informatics.assignment.model.binding.AssignmentAddBindingModel;
import com.angeltashev.informatics.assignment.model.view.AssignmentAllViewModel;
import com.angeltashev.informatics.assignment.model.view.AssignmentDetailsViewModel;
import com.angeltashev.informatics.assignment.service.AssignmentService;
import com.angeltashev.informatics.exceptions.PageNotFoundException;
import com.angeltashev.informatics.file.exception.FileStorageException;
import com.angeltashev.informatics.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

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
        return "redirect:/users/my-profile/assignments/" + assignmentId;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/add")
    public String getAddAssignmentForm(Model model) {
        if (!model.containsAttribute("assignmentModel")) {
            model.addAttribute("assignmentModel", new AssignmentAddBindingModel());
            model.addAttribute("usernameModel", this.userService.getUserAssignmentAddModels());
        }
        return "assignment/assignment-add";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public String postAddAssignment(@Valid @ModelAttribute("assignmentModel") AssignmentAddBindingModel assignmentModel,
                                    BindingResult bindingResult,
                                    @RequestParam("resources") MultipartFile resources,
                                    RedirectAttributes redirectAttributes) throws IOException, FileStorageException {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("assignmentModel", assignmentModel);
            redirectAttributes.addFlashAttribute(BINDING_RESULT_PATH + "assignmentModel", bindingResult);
            redirectAttributes.addFlashAttribute("usernameModel", this.userService.getUserAssignmentAddModels());
            return "redirect:/users/my-profile/assignments/add";
        }
        this.assignmentService.addAssignment(assignmentModel, resources);
        return "redirect:/home";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public String getAllAssignments(Model model) {
        List<AssignmentAllViewModel> viewModel = this.assignmentService.getAllAssignments();
        model.addAttribute("viewModel", viewModel);
        return "assignment/assignment-all";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/score/{id}")
    public String scoreAssignment(@PathVariable("id") String assignmentId, @RequestParam("score") Integer score) {
        this.assignmentService.scoreAssigment(assignmentId, score);
        return "redirect:/users/my-profile/assignments/all";
    }
}
