package com.angeltashev.informatics.web;

import com.angeltashev.informatics.message.model.MessageEntity;
import com.angeltashev.informatics.message.model.binding.MessageAddBindingModel;
import com.angeltashev.informatics.message.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private MessageController controllerToTest;
    @Mock
    private MessageService mockMessageService;
    @Mock
    private BindingResult mockBindingResult;
    @Mock
    private RedirectAttributes mockRedirectAttributes;

    @BeforeEach
    void setUp() {
        controllerToTest = new MessageController(mockMessageService);
    }

    @Test
    public void getMessageFormShouldReturnCorrectMessageForm() throws Exception {
        this.mockMvc.perform(get("/contact-us"))
                .andExpect(status().isOk());
    }

    @Test
    public void confirmMessageFormShouldSuccessfullyConfirm() throws Exception {
        // Arrange
        MessageAddBindingModel messageAddBindingModel = new MessageAddBindingModel();
        when(this.mockBindingResult.hasErrors())
                .thenReturn(false);

        // Act
        mockMvc.perform(post("/contact-us").with(csrf()))
                .andExpect(status().isFound());
        String result = this.controllerToTest.confirmMessageForm(messageAddBindingModel, mockBindingResult, mockRedirectAttributes);

        // Assert
        assertEquals("redirect:/", result);
        verify(this.mockMessageService, times(1)).addMessage(messageAddBindingModel);
    }

    @Test
    public void confirmMessageFormShouldRedirectOnWrongModel() throws Exception {
        // Arrange
        MessageAddBindingModel messageAddBindingModel = new MessageAddBindingModel();
        when(this.mockBindingResult.hasErrors())
                .thenReturn(true);

        // Act
        mockMvc.perform(post("/contact-us").with(csrf()))
                .andExpect(status().isFound());
        String result = this.controllerToTest.confirmMessageForm(messageAddBindingModel, mockBindingResult, mockRedirectAttributes);

        // Assert
        assertEquals("redirect:/contact-us", result);
        verify(this.mockRedirectAttributes, times(1)).addFlashAttribute("messageModel", messageAddBindingModel);
    }
}
