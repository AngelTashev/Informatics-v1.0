package com.angeltashev.informatics.web;

import com.angeltashev.informatics.constants.ApplicationParameters;
import com.angeltashev.informatics.user.model.binding.UserRegisterBindingModel;
import com.angeltashev.informatics.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import static com.angeltashev.informatics.constants.ApplicationParameters.BINDING_RESULT_PATH;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/users")
public class UserAuthenticationController {

    private final UserService userService;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String getLoginForm() {
        return "user/login";
    }

//    @PostMapping("/login")
//    public String confirmLoginForm() {
//        return "redirect:/home";
//    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/register")
    public String getRegisterForm(Model model) {
        if (!model.containsAttribute("registerModel")) {
            model.addAttribute("registerModel", new UserRegisterBindingModel());
        }
        return "user/register";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/register")
    public String confirmRegisterForm(@Valid @ModelAttribute("registerModel") UserRegisterBindingModel registerModel,
                                      BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if(this.userService.findByUsername(registerModel.getUsername()) != null) {
            bindingResult.rejectValue("username", "error.registerModel", "Username is already taken");
        }

        if (this.userService.findByEmail(registerModel.getEmail()) != null) {
            bindingResult.rejectValue("email", "error.registerModel", "A profile with this email address already exists");
        }

        if (!registerModel.getPassword().equals(registerModel.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.registerModel", "Passwords don't match");
        }
        if (bindingResult.hasErrors()) {
            log.error("Confirm register form: Binding model contains errors");
            redirectAttributes.addFlashAttribute("registerModel", registerModel);
            redirectAttributes.addFlashAttribute(BINDING_RESULT_PATH + "registerModel", bindingResult);
            return "redirect:/users/register";
        }

        //Successful registration
        this.userService.registerUser(registerModel);
        log.info("Confirm register form: Successfully registered user " + registerModel.getFullName() + " with username " + registerModel.getUsername());
        return "redirect:/users/login";
    }
}
