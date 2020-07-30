package com.angeltashev.informatics.assignment.repository;

import com.angeltashev.informatics.assignment.model.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, String> {

}
