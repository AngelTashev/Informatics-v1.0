package com.angeltashev.informatics.assignment.model.view;

import com.angeltashev.informatics.file.model.DBFile;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AssignmentAllViewModel {

    private String id;
    private String name;
    private String username;
    private DBFile submission;
    private Integer points;
}
