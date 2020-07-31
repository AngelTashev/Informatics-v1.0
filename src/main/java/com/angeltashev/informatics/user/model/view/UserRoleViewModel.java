package com.angeltashev.informatics.user.model.view;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRoleViewModel {

    private String id;
    private String fullName;
    private String username;
    private String email;
    private Integer points;
}
