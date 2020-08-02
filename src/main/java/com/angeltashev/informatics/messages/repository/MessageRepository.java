package com.angeltashev.informatics.messages.repository;

import com.angeltashev.informatics.messages.model.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, String> {
}
