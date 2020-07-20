package com.angeltashev.informatics.web;

import com.angeltashev.informatics.file.exception.FileStorageException;
import com.angeltashev.informatics.user.model.view.UserProfileViewModel;
import com.angeltashev.informatics.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@AllArgsConstructor
@Controller
@RequestMapping("/users/id")
public class UserProfileController {

    private final UserService userService;

    @GetMapping
    public String getProfileById(
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

    @PostMapping("/upload-picture")
    public String uploadProfilePicture(@RequestParam("file") MultipartFile file,
                                       Principal principal
                                       ) throws FileStorageException {

        this.userService.uploadPicture(principal.getName(), file);
        return "redirect:/users/id";
    }

}
