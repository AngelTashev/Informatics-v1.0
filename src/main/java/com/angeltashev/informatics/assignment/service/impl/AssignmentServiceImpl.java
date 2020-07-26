package com.angeltashev.informatics.assignment.service.impl;

import com.angeltashev.informatics.assignment.exception.InvalidArgumentIdException;
import com.angeltashev.informatics.assignment.model.binding.AssignmentAddBindingModel;
import com.angeltashev.informatics.assignment.model.AssignmentEntity;
import com.angeltashev.informatics.assignment.model.view.AssignmentDetailsViewModel;
import com.angeltashev.informatics.assignment.repository.AssignmentRepository;
import com.angeltashev.informatics.assignment.service.AssignmentService;
import com.angeltashev.informatics.file.exception.FileStorageException;
import com.angeltashev.informatics.file.model.DBFile;
import com.angeltashev.informatics.file.service.DBFileStorageService;
import com.angeltashev.informatics.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@Service
public class AssignmentServiceImpl implements AssignmentService {

    private final ModelMapper modelMapper;

    private final DBFileStorageService fileStorageService;

    private final UserRepository userRepository;
    private final AssignmentRepository assignmentRepository;

    @Override
    public AssignmentDetailsViewModel getAssignmentByIdAndUser(String id, String username) {
        AssignmentEntity assignmentEntity = this.assignmentRepository.findById(id).orElse(null);
        if (assignmentEntity == null) return null;
        if (!assignmentEntity.getUser().getUsername().equals(username)) return null;
        AssignmentDetailsViewModel assignmentDetailsViewModel = this.modelMapper.map(assignmentEntity, AssignmentDetailsViewModel.class);
        assignmentDetailsViewModel.setDueDate(assignmentEntity.getDueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm a")));
        return assignmentDetailsViewModel;
    }

    @Override
    public boolean uploadSubmission(String assignmentId, MultipartFile submission) throws FileStorageException {
        AssignmentEntity assignmentEntity = this.assignmentRepository.findById(assignmentId).orElse(null);
        if (assignmentEntity == null) {
            throw new InvalidArgumentIdException("Assignment id is invalid!");
        }
        String oldSubmissionId = null;
        if (assignmentEntity.getSubmission() != null) {
            oldSubmissionId = assignmentEntity.getSubmission().getId();
        }
        DBFile file = this.fileStorageService.storeFile(submission);
        assignmentEntity.setSubmission(file);
        this.assignmentRepository.save(assignmentEntity);
        if (oldSubmissionId != null) {
            this.fileStorageService.deleteFile(oldSubmissionId);
        }
        return true;
    }

    @Override
    public boolean addAssignment(AssignmentAddBindingModel assignment, MultipartFile resources) throws IOException, FileStorageException {
        DBFile file = null;
        if (!resources.isEmpty()) {
            file = this.fileStorageService.storeFile(resources);
        }
        for(String username : assignment.getUsers()) {
            AssignmentEntity assignmentEntity = this.modelMapper.map(assignment, AssignmentEntity.class);
            if (file != null) {
                assignmentEntity.setResources(file);
            }
            assignmentEntity.setUser(this.userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username cannot be found")));
            this.assignmentRepository.saveAndFlush(assignmentEntity);
        }
        return false;
    }
}
