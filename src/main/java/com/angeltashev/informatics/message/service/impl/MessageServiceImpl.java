package com.angeltashev.informatics.message.service.impl;

import com.angeltashev.informatics.message.model.MessageEntity;
import com.angeltashev.informatics.message.model.binding.MessageAddBindingModel;
import com.angeltashev.informatics.message.model.view.MessageViewModel;
import com.angeltashev.informatics.message.repository.MessageRepository;
import com.angeltashev.informatics.message.service.MessageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class MessageServiceImpl implements MessageService {

    private final ModelMapper modelMapper;

    private final MessageRepository messageRepository;

    @Override
    public boolean addMessage(MessageAddBindingModel messageAddBindingModel) {

        MessageEntity messageEntity = this.modelMapper.map(messageAddBindingModel, MessageEntity.class);
        this.messageRepository.save(messageEntity);

        return true;
    }

    @Override
    public boolean deleteMessageById(String messageId) {
        this.messageRepository.deleteById(messageId);
        log.info("Delete message by id: Successfully deleted message with id: " + messageId);
        return true;
    }

    @Override
    public boolean deleteAllMessages() {
        this.messageRepository.deleteAll();
        log.info("Delete all messages: Successfully deleted all messages");
        return true;
    }

    @Override
    public List<MessageViewModel> getAllMessages() {
        return this.messageRepository.findAll()
                .stream()
                .map(message -> this.modelMapper.map(message, MessageViewModel.class))
                .collect(Collectors.toList());
    }
}
