package com.angeltashev.informatics.assignment.model.view;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AssignmentDetailsViewModel {

    private String id;
    private String name;
    private String description;
    private String dueDate;
    private byte[] resource;
    private byte[] submission;
}
