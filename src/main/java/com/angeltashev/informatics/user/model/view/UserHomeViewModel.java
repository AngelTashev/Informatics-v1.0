package com.angeltashev.informatics.user.model.view;

import com.angeltashev.informatics.assignment.model.view.AssignmentHomeViewModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class UserHomeViewModel {

    private String fullName;
    private Set<AssignmentHomeViewModel> assignments;
}
