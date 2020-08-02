package com.angeltashev.informatics.web;

import com.angeltashev.informatics.exceptions.PageNotFoundException;
import com.angeltashev.informatics.file.exception.FileStorageException;
import com.angeltashev.informatics.user.model.view.UserProfileViewModel;
import com.angeltashev.informatics.user.model.view.UserVisitViewModel;
import com.angeltashev.informatics.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.security.Principal;

@Slf4j
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

        UserProfileViewModel user = this.userService.getUserProfile(principal.getName());
        model.addAttribute("userView", user);
        log.info("Get user profile: Retrieving user profile: " + user.getUsername());
        return "user/profile-details";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{username}")
    public String getUserByUsername(@PathVariable("username") String username, Model model) throws PageNotFoundException {
        UserVisitViewModel user = this.userService.getUserVisitProfile(username);
        if (user == null) {
            log.error("Get user by username: Username is not found");
            throw new PageNotFoundException("Username is not found");
        }
        log.info("Get user by username: Retrieved visit model for: " + username);
        model.addAttribute("userVisitView", user);
        return "user/profile-visit";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/upload-picture")
    public String uploadProfilePicture(@RequestParam("file") MultipartFile file,
                                       Principal principal
                                       ) throws FileStorageException, IOException {
        log.info("Upload profile picture (controller): Requested upload of picture for user: " + principal.getName());
        this.userService.uploadProfilePicture(principal.getName(), file);
        return "redirect:/users/my-profile";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/change-phrase")
    public String changePhrase(@RequestParam("phrase") String phrase,
                                       Principal principal
    ) {
        log.info("Change phrase (controller): Requested change of phrase for user: " + principal.getName());
        this.userService.changePhrase(principal.getName(), phrase);
        return "redirect:/users/my-profile";
    }

}
