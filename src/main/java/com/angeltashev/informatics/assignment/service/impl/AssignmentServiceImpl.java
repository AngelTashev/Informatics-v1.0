package com.angeltashev.informatics.assignment.service.impl;

import com.angeltashev.informatics.assignment.model.AssignmentEntity;
import com.angeltashev.informatics.assignment.model.view.AssignmentDetailsViewModel;
import com.angeltashev.informatics.assignment.repository.AssignmentRepository;
import com.angeltashev.informatics.assignment.service.AssignmentService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
        return this.modelMapper.map(assignmentEntity, AssignmentDetailsViewModel.class);
    }
}
