package com.angeltashev.informatics.assignment.model;

import com.angeltashev.informatics.user.model.binding.UserAssignmentAddBindingModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AssignmentAddBindingModel {

    String name;
    String description;
    byte[] resources;
    List<String> users;
}
