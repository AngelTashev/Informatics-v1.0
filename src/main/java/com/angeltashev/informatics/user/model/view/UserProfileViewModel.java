package com.angeltashev.informatics.user.model.view;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserProfileViewModel {

    private String fullName;
    private String username;
    private String points;
    private String email;
    private String authority;
    private String phrase;
    private String profilePictureString;
}
