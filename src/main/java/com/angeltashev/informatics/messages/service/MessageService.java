package com.angeltashev.informatics.messages.service;

import com.angeltashev.informatics.messages.model.binding.MessageAddBindingModel;
import com.angeltashev.informatics.messages.model.view.MessageViewModel;

import java.util.List;

public interface MessageService {

    boolean addMessage(MessageAddBindingModel messageAddBindingModel);

    boolean deleteMessageById(String messageId);

    boolean deleteAllMessages();

    List<MessageViewModel> getAllMessages();
}
