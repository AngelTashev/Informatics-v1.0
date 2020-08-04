package com.angeltashev.informatics.message;

import com.angeltashev.informatics.message.model.MessageEntity;
import com.angeltashev.informatics.message.model.binding.MessageAddBindingModel;
import com.angeltashev.informatics.message.model.view.MessageViewModel;
import com.angeltashev.informatics.message.repository.MessageRepository;
import com.angeltashev.informatics.message.service.MessageService;
import com.angeltashev.informatics.message.service.impl.MessageServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageServiceImplTest {

    private final String VALID_ID = "validId";
    private final String VALID_FULL_NAME = "validFullName";
    private final String VALID_EMAIL = "validEmail";
    private final String VALID_MESSAGE = "validMessage";

    private MessageService serviceToTest;

    private ModelMapper realMapper;

    private MessageEntity messageEntity;

    @Mock
    private ModelMapper mockModelMapper;

    @Mock
    private MessageRepository mockMessageRepository;

    @BeforeEach
    void setUp() {
        this.serviceToTest = new MessageServiceImpl(mockModelMapper, mockMessageRepository);
        this.realMapper = new ModelMapper();
        messageEntity = new MessageEntity();
        messageEntity.setId(VALID_ID);
        messageEntity.setFullName(VALID_FULL_NAME);
        messageEntity.setEmail(VALID_EMAIL);
        messageEntity.setMessage(VALID_MESSAGE);
    }

    @Test
    public void addMessageShouldReturnTrueOnSuccessfulAdd() {
        // Arrange
        MessageAddBindingModel messageAddBindingModel = new MessageAddBindingModel();
        when(this.mockModelMapper.map(messageAddBindingModel, MessageEntity.class))
                .thenReturn(messageEntity);

        // Act
        boolean result = this.serviceToTest.addMessage(messageAddBindingModel);

        // Assert
        assertTrue(result);
        verify(this.mockMessageRepository, times(1)).save(messageEntity);

    }

    @Test
    public void deleteMessageByIdShouldReturnTrueOnSuccessfulDeletion() {
        // Arrange

        // Act
        boolean result = this.serviceToTest.deleteMessageById(VALID_ID);

        // Assert
        assertTrue(result);
        verify(this.mockMessageRepository, times(1)).deleteById(VALID_ID);
    }

    @Test
    public void deleteAllMessagesShouldReturnTrueOnSuccessfulDeletion() {
        // Arrange

        // Act
        boolean result = this.serviceToTest.deleteAllMessages();

        // Assert
        assertTrue(result);
        verify(this.mockMessageRepository, times(1)).deleteAll();
    }

    @Test
    public void getAllMessages() {
        // Arrange
        when(this.mockMessageRepository.findAll())
                .thenReturn(List.of(messageEntity));
        when(this.mockModelMapper.map(messageEntity, MessageViewModel.class))
                .thenReturn(this.realMapper.map(messageEntity, MessageViewModel.class));

        // Act
        List<MessageViewModel> messages = this.serviceToTest.getAllMessages();

        // Assert
        assertEquals(1, messages.size());
        assertEquals(VALID_ID, messages.get(0).getId());
    }
}
