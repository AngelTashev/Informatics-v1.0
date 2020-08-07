package com.angeltashev.informatics.web;

import com.angeltashev.informatics.message.model.MessageEntity;
import com.angeltashev.informatics.message.repository.MessageRepository;
import com.angeltashev.informatics.user.model.UserEntity;
import com.angeltashev.informatics.user.repository.AuthorityRepository;
import com.angeltashev.informatics.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class RootAdminRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private MessageRepository messageRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    private UserEntity userEntity;
    private MessageEntity messageEntity;
    private String VALID_USER_ID = "validUserId";
    private String VALID_MESSAGE_ID = "validMessageId";
    private String VALID_EMAIL = "validEmail";
    private String VALID_USERNAME = "validUsername";

    @BeforeEach
    void setUp() {
//        userRepository.deleteAll();

        // Message
        messageEntity = new MessageEntity();
        messageEntity.setId(VALID_MESSAGE_ID);
        messageEntity.setEmail(VALID_EMAIL);
        when(this.messageRepository.findAll()).thenReturn(List.of(messageEntity));

        // User
        userEntity = new UserEntity();
        userEntity.setId(VALID_USER_ID);
        userEntity.setUsername(VALID_USERNAME);
        userEntity.setPassword("123");
        userEntity.setGrade(10);
        userEntity.setGradeClass("A");
        userEntity.setEmail("email@email.com");
        userEntity.setPoints(0);
        userEntity.setFullName(VALID_USERNAME + " name");
        userEntity.setAuthorities(Set.of(authorityRepository.findByAuthority("ROLE_ADMIN"), authorityRepository.findByAuthority("ROLE_STUDENT")));
//        userEntity = userRepository.saveAndFlush(userEntity);
//        VALID_ID = userEntity.getId();

        when(this.userRepository.findAll()).thenReturn(List.of(userEntity));
    }

    @Test
    @WithMockUser(username = "user", password = "pass", roles = "ROOT_ADMIN")
    public void getAllAdminsShouldReturnAllAdmins() throws Exception {
        this.mockMvc.perform(get("/root-admin-panel/admins-rest")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$", hasSize(1))).
                andExpect(jsonPath("$.[0].id", is(VALID_USER_ID))).
                andExpect(jsonPath("$.[0].username", is(VALID_USERNAME)));
    }

    @Test
    @WithMockUser(username = "user", password = "pass", roles = "ROOT_ADMIN")
    public void getAllStudentsShouldReturnAllStudents() throws Exception {
        this.mockMvc.perform(get("/root-admin-panel/students-rest")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$", hasSize(1))).
                andExpect(jsonPath("$.[0].id", is(VALID_USER_ID))).
                andExpect(jsonPath("$.[0].username", is(VALID_USERNAME)));
    }

    @Test
    @WithMockUser(username = "user", password = "pass", roles = "ROOT_ADMIN")
    public void getAllMessagesShouldReturnAllMessages() throws Exception {
        this.mockMvc.perform(get("/root-admin-panel/messages-rest")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$", hasSize(1))).
                andExpect(jsonPath("$.[0].id", is(VALID_MESSAGE_ID))).
                andExpect(jsonPath("$.[0].email", is(VALID_EMAIL)));
    }
}
