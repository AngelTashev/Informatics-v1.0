package com.angeltashev.informatics.assignment.service;

import com.angeltashev.informatics.assignment.model.view.AssignmentDetailsViewModel;
import com.angeltashev.informatics.assignment.model.view.AssignmentHomeViewModel;
import com.angeltashev.informatics.file.exception.FileStorageException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AssignmentService {

    AssignmentDetailsViewModel getAssignmentByIdAndUser(String id, String username);

    boolean uploadSubmission(String assignmentId, MultipartFile submission) throws FileStorageException;
}
