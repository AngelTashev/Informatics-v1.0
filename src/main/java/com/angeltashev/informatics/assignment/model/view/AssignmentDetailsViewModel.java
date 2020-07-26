package com.angeltashev.informatics.assignment.model.view;

import com.angeltashev.informatics.file.model.DBFile;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AssignmentDetailsViewModel {

    private String id;
    private String name;
    private String description;
    private String points;
    private String dueDate;
    private DBFile resources;
    private DBFile submission;
}
