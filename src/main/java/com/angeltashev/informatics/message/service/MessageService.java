package com.angeltashev.informatics.message.service;

import com.angeltashev.informatics.message.model.binding.MessageAddBindingModel;
import com.angeltashev.informatics.message.model.view.MessageViewModel;

import java.util.List;

public interface MessageService {

    boolean addMessage(MessageAddBindingModel messageAddBindingModel);

    boolean deleteMessageById(String messageId);

    boolean deleteAllMessages();

    List<MessageViewModel> getAllMessages();
}
