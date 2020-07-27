package com.angeltashev.informatics.assignment.service;

import com.angeltashev.informatics.assignment.model.binding.AssignmentAddBindingModel;
import com.angeltashev.informatics.assignment.model.view.AssignmentAllViewModel;
import com.angeltashev.informatics.assignment.model.view.AssignmentDetailsViewModel;
import com.angeltashev.informatics.file.exception.FileStorageException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AssignmentService {

    AssignmentDetailsViewModel getAssignmentByIdAndUser(String id, String username);

    boolean uploadSubmission(String assignmentId, MultipartFile submission) throws FileStorageException;

    boolean addAssignment(AssignmentAddBindingModel assignment, MultipartFile resources) throws IOException, FileStorageException;

    List<AssignmentAllViewModel> getAllAssignments();

    boolean scoreAssigment(String assignmentId, Integer score);
}
