package com.angeltashev.informatics.web;

import com.angeltashev.informatics.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class UserAuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private String VALID_USER_ID = "peshoId";
    private String VALID_FULLNAME = "Pesho Ivanov";
    private String VALID_USERNAME = "pesho.ivanov";
    private String VALID_EMAIL = "pesho@ivanov.bg";
    private String VALID_PASSWORD = "pesho123";
    private Integer VALID_GRADE = 12;
    private String VALID_GRADE_CLASS = "A";
    private Integer VALID_POINTS = 100;


}
