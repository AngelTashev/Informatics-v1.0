package com.angeltashev.informatics.user.model.view;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserProfileViewModel {

    private String username;
    private String email;
    private String profilePictureString;
}
