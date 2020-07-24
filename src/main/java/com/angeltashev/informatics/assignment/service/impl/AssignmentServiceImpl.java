package com.angeltashev.informatics.assignment.service.impl;

import com.angeltashev.informatics.assignment.exception.InvalidArgumentIdException;
import com.angeltashev.informatics.assignment.model.AssignmentEntity;
import com.angeltashev.informatics.assignment.model.view.AssignmentDetailsViewModel;
import com.angeltashev.informatics.assignment.repository.AssignmentRepository;
import com.angeltashev.informatics.assignment.service.AssignmentService;
import com.angeltashev.informatics.file.exception.FileStorageException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@AllArgsConstructor
@Service
public class AssignmentServiceImpl implements AssignmentService {

    private final ModelMapper modelMapper;

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
        String fileName = StringUtils.cleanPath(submission.getOriginalFilename());

        try {
            if (fileName.contains("..")) {
                throw new FileStorageException("Filename contains invalid characters!");
            }

            assignmentEntity.setSubmission(submission.getBytes());
            this.assignmentRepository.save(assignmentEntity);
        } catch (IOException e) {
            throw new FileStorageException("Could not upload picture " + fileName + ". Please try again!", e);
        }
        return true;
    }
}
