package com.angeltashev.informatics.assignment.service.impl;

import com.angeltashev.informatics.assignment.exception.InvalidArgumentIdException;
import com.angeltashev.informatics.assignment.model.CommentEntity;
import com.angeltashev.informatics.assignment.model.binding.AssignmentAddBindingModel;
import com.angeltashev.informatics.assignment.model.AssignmentEntity;
import com.angeltashev.informatics.assignment.model.view.AssignmentAllViewModel;
import com.angeltashev.informatics.assignment.model.view.AssignmentDetailsViewModel;
import com.angeltashev.informatics.assignment.repository.AssignmentRepository;
import com.angeltashev.informatics.assignment.repository.CommentRepository;
import com.angeltashev.informatics.assignment.service.AssignmentService;
import com.angeltashev.informatics.file.exception.FileStorageException;
import com.angeltashev.informatics.file.model.DBFile;
import com.angeltashev.informatics.file.service.DBFileStorageService;
import com.angeltashev.informatics.user.repository.UserRepository;
import com.angeltashev.informatics.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class AssignmentServiceImpl implements AssignmentService {

    private final ModelMapper modelMapper;

    private final DBFileStorageService fileStorageService;
    private final UserService userService;

    private final UserRepository userRepository;
    private final AssignmentRepository assignmentRepository;
    private final CommentRepository commentRepository;

    @Override
    public AssignmentDetailsViewModel getAssignmentByIdAndUser(String id, String username) {
        AssignmentEntity assignmentEntity = this.assignmentRepository.findById(id).orElse(null);
        if (assignmentEntity == null) {
            log.error("Get assignment: Invalid assignment id");
            return null;
        }
        if (!assignmentEntity.getUser().getUsername().equals(username)) {
            log.error("Get assignment: Invalid username");
            return null;
        }
        AssignmentDetailsViewModel assignmentDetailsViewModel = this.modelMapper.map(assignmentEntity, AssignmentDetailsViewModel.class);
        assignmentDetailsViewModel.setDueDate(assignmentEntity.getDueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm a")));
        log.info("Get assignment: Returned assignment with id " + id + " / Username: " + username);
        return assignmentDetailsViewModel;
    }

    @Override
    public boolean uploadSubmission(String assignmentId, MultipartFile submission) throws FileStorageException {
        AssignmentEntity assignmentEntity = this.assignmentRepository.findById(assignmentId).orElse(null);
        if (assignmentEntity == null) {
            log.error("Upload submission: Assignment id is invalid!");
            throw new InvalidArgumentIdException("Assignment id is invalid!");
        }
        String oldSubmissionId = null;
        if (assignmentEntity.getSubmission() != null) {
            oldSubmissionId = assignmentEntity.getSubmission().getId();
        }
        DBFile file = this.fileStorageService.storeFile(submission);
        assignmentEntity.setSubmission(file);
        this.assignmentRepository.save(assignmentEntity);
        log.info("Upload submission: Uploaded submission with id: " + assignmentId + " / Username: " + assignmentEntity.getUser().getUsername());
        if (oldSubmissionId != null) {
            this.fileStorageService.deleteFile(oldSubmissionId);
            log.info("Upload submission: Deleted old submission with id: " + assignmentId + " / Username: " + assignmentEntity.getUser().getUsername());
        }
        return true;
    }

    @Override
    public boolean addAssignment(AssignmentAddBindingModel assignment, MultipartFile resources) throws FileStorageException {
        DBFile file = null;
        if (!resources.isEmpty()) {
            log.info("Add assignment: Resources added to assignment " + assignment.getName());
            file = this.fileStorageService.storeFile(resources);
        }
        for(String username : assignment.getUsers()) {
            AssignmentEntity assignmentEntity = this.modelMapper.map(assignment, AssignmentEntity.class);
            if (file != null) {
                assignmentEntity.setResources(file);
            }
            assignmentEntity.setUser(this.userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username cannot be found")));
            assignmentEntity.setEnabled(true);
            log.info("Add assignment: Added assignment " + assignment.getName() + " to " + username);
            this.assignmentRepository.saveAndFlush(assignmentEntity);
        }
        return false;
    }

    @Override
    public List<AssignmentAllViewModel> getAllAssignments() {
        log.info("Get all assignments: Getting all assignments");
        return this.getAllAssignmentEntities()
                .stream()
                .map(assignment -> {
                    AssignmentAllViewModel viewModel = this.modelMapper.map(assignment, AssignmentAllViewModel.class);
                    viewModel.setUsername(assignment.getUser().getUsername());
                    return viewModel;
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean scoreAssigment(String assignmentId, Integer score, String comment) {
        AssignmentEntity assignment = this.assignmentRepository.findById(assignmentId).orElse(null);
        if(assignment == null) {
            log.error("Score assignment: Assignment id is invalid!");
            throw new InvalidArgumentIdException("Assignment id is invalid!");
        }
        assignment.setPoints(score);
        log.info("Score assignment: Points (" + score + ") set to assignment with id: " + assignmentId);
        if (!comment.trim().isEmpty()) {
            CommentEntity commentEntity = new CommentEntity();
            commentEntity.setComment(comment);
            commentEntity = this.commentRepository.save(commentEntity);
            assignment.setComment(commentEntity);
        }
        this.userService.addPointsToUser(assignment.getUser().getUsername(), score);
        log.info("Score assignment: Points (" + score + ") added to username:  " + assignment.getUser().getUsername());
        this.assignmentRepository.save(assignment);
        return true;
    }

    @Override
    public boolean disableOldAssignments() {
        LocalDateTime dateTime = LocalDateTime.now();
        List<AssignmentEntity> assignments = this.assignmentRepository.findAll();
        assignments.forEach(assignment -> {
            if (assignment.getDueDate().isBefore(dateTime)) {
                assignment.setEnabled(false);
                log.info("Disable old assignments: disabled assignment with id: " + assignment.getId());
                this.assignmentRepository.save(assignment);
            }
        });
        return true;
    }

    @CachePut("assignments")
    @Override
    public List<AssignmentEntity> updateAllAssignments() {
        log.info("Update all assignments: Updated assignments cache");
        return this.getAllAssignmentEntities();
    }

    @Override
    public boolean deleteAssignmentById(String assignmentId) {
        try {
            this.assignmentRepository.deleteById(assignmentId);
            log.info("Delete assignment by id: Deleted assignment with id: " + assignmentId);
        } catch (Exception e) {
            log.error("Delete assignment by id: Assignment with id " + assignmentId + " does not exist");
        }
        return true;
    }

    @Cacheable("assignments")
    public List<AssignmentEntity> getAllAssignmentEntities() {
        log.info("Get all assignments: Retrieved all assignments");
        return this.assignmentRepository.findAll();
    }

    @Override
    public boolean cleanUpOldAssignments() {
        LocalDateTime dateTime = LocalDateTime.now();
        List<AssignmentEntity> assignments = this.assignmentRepository.findAll();
        assignments.forEach(assignment -> {
            if (assignment.getDueDate().plusDays(3).isBefore(dateTime)) {
                log.info("Clean up old assignments: Deleted assignment with id: " + assignment.getId());
                this.assignmentRepository.delete(assignment);
            }
        });
        return true;
    }
}
