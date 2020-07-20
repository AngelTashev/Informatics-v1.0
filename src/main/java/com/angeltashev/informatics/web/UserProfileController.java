package com.angeltashev.informatics.web;

import com.angeltashev.informatics.file.exception.FileStorageException;
import com.angeltashev.informatics.user.model.view.UserProfileViewModel;
import com.angeltashev.informatics.user.model.view.UserVisitViewModel;
import com.angeltashev.informatics.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@AllArgsConstructor
@Controller
@RequestMapping("/users")
public class UserProfileController {

    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my-profile")
    public String getUserProfile(
            Principal principal,
            Model model
    ) {

        // TODO Change picture border depending on authority
        // TODO Add remaining info to profile
        // TODO Add a way to change profile info
        
        UserProfileViewModel user = this.userService.getUserProfile(principal.getName());
        model.addAttribute("userView", user);
        return "user/profile-details";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{username}")
    public String getUserByUsername(@PathVariable("username") String username, Model model) throws UsernameNotFoundException {
        UserVisitViewModel user = this.userService.getUserVisitProfile(username);
        if (user == null) throw new UsernameNotFoundException("Username is not found");
        model.addAttribute("userVisitView", user);
        return "user/profile-visit";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/upload-picture")
    public String uploadProfilePicture(@RequestParam("file") MultipartFile file,
                                       Principal principal
                                       ) throws FileStorageException {

        this.userService.uploadPicture(principal.getName(), file);
        return "redirect:/users/my-profile";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({UsernameNotFoundException.class})
    public ModelAndView userNotFound() {
        ModelAndView modelAndView = new ModelAndView("user/user-404");
        return modelAndView;
    }

}
