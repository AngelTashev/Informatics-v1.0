package com.angeltashev.informatics.web;

import com.angeltashev.informatics.user.model.view.UserProfileViewModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class UserProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @WithMockUser(username = "username", password = "pass", roles = "STUDENT")
    @Test
    public void getPageShouldRedirectToLoginPage() throws Exception {
        this.mockMvc.perform(get("/users/my-profile").secure(true))
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }


}
