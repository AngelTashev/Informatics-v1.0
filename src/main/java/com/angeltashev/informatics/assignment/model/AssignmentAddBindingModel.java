package com.angeltashev.informatics.assignment.model;

import com.angeltashev.informatics.user.model.binding.UserAssignmentAddBindingModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
public class AssignmentAddBindingModel {

    @NotNull
    @Length(min = 8, max = 50, message = "Title length must be between 8 and 50 characters")
    String name;

    @NotNull
    @Length(min = 12, max = 400, message = "Title length must be between 12 and 400 characters")
    String description;

    byte[] resources;

    @NotNull(message = "Select at least one student, please :)")
    List<String> users;
}
