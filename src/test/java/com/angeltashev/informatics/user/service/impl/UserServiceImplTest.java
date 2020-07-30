package com.angeltashev.informatics.user.service.impl;

import com.angeltashev.informatics.user.model.binding.UserRegisterBindingModel;
import com.angeltashev.informatics.user.repository.UserRepository;
import com.angeltashev.informatics.user.service.AuthorityProcessingService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

//@ExtendWith(MockitoExtension.class)
//@RunWith(JUnitPlatform.class)
class UserServiceImplTest {

//    @InjectMocks
//    static UserServiceImpl userService;

    @BeforeAll
    static void init() {
//        userService.registerUser(createPesho());
        System.out.println("Initializing tests");
    }

//    @DisplayName("Test number 1")
//    @Test
//    void findByNameReturnsCorrectUser() {
//        String name = "Pesho Ivanov";
//        assertTrue(userService.findByUsername("pesho").equals(name));
//    }

    static UserRegisterBindingModel createPesho() {
        UserRegisterBindingModel pesho = new UserRegisterBindingModel();
        pesho.setFullName("Pesho Ivanov");
        pesho.setUsername("pesho.ivanov");
        pesho.setEmail("pesho.i@abv.bg");
        pesho.setPassword("pass");
        pesho.setConfirmPassword("pass");
        pesho.setGrade(9);
        pesho.setGradeClass("A");
        pesho.setPhrase("Love JUnit!");
        return pesho;
    }

}