package com.angeltashev.informatics.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void downloadResourcesShouldReturnStatusFound() throws Exception {
        this.mockMvc.perform(get("/files/resources/download/{id}", "validId"))
                .andExpect(status().isFound());
    }

    @Test
    public void downloadSubmissionShouldReturnStatusFound() throws Exception {
        this.mockMvc.perform(get("/files/submissions/download/{id}", "validId"))
                .andExpect(status().isFound());
    }
}
