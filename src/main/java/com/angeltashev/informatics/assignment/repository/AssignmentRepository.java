package com.angeltashev.informatics.assignment.repository;

import com.angeltashev.informatics.assignment.model.AssignmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepository extends JpaRepository<AssignmentEntity, String> {
}
