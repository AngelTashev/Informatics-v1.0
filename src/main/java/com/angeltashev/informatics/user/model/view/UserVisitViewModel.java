package com.angeltashev.informatics.user.model.view;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserVisitViewModel {

    private String fullName;
    private String username;
    private String authority;
    private String phrase;
    private Integer points;
    private String profilePictureString;
}
