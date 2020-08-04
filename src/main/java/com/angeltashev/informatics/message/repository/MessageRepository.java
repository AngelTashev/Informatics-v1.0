package com.angeltashev.informatics.message.repository;

import com.angeltashev.informatics.message.model.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, String> {
}
