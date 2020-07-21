package com.angeltashev.informatics.assignment.service;

import com.angeltashev.informatics.assignment.model.view.AssignmentDetailsViewModel;
import com.angeltashev.informatics.assignment.model.view.AssignmentHomeViewModel;

import java.util.List;

public interface AssignmentService {

    AssignmentDetailsViewModel getAssignmentByIdAndUser(String id, String username);
}
