package com.angeltashev.informatics.user.service.impl;

import com.angeltashev.informatics.file.service.DBFileStorageService;
import com.angeltashev.informatics.user.model.AuthorityEntity;
import com.angeltashev.informatics.user.model.UserEntity;
import com.angeltashev.informatics.user.model.binding.UserRegisterBindingModel;
import com.angeltashev.informatics.user.model.view.UserProfileViewModel;
import com.angeltashev.informatics.user.model.view.UserRoleViewModel;
import com.angeltashev.informatics.user.repository.UserRepository;
import com.angeltashev.informatics.user.service.AuthorityProcessingService;
import com.angeltashev.informatics.user.service.UserService;
import org.hsqldb.rights.User;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

//@RunWith(JUnitPlatform.class)
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    // User
    private String VALID_USER_ID = "peshoId";
    private String VALID_FULLNAME = "Pesho Ivanov";
    private String VALID_USERNAME = "pesho.ivanov";
    private String VALID_EMAIL = "pesho@ivanov.bg";
    private String VALID_PHRASE = "Hello there!";
    private String VALID_PASSWORD = "pesho123";
    private Integer VALID_GRADE = 12;
    private String VALID_GRADE_CLASS = "A";
    private Integer VALID_POINTS = 100;
    private String VALID_PICTURE = "";
    // Authority
    private String VALID_STUDENT_ID = "studentId";
    private String VALID_ADMIN_ID = "adminId";
    private String VALID_STUDENT_AUTHORITY = "ROLE_STUDENT";
    private String VALID_ADMIN_AUTHORITY = "ROLE_ADMIN";

    UserEntity userEntity;
    AuthorityEntity studentAuthority;
    AuthorityEntity adminAuthority;

    private UserService serviceToTest;
    private ModelMapper realMapper;

    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private AuthorityProcessingService mockAuthorityProcessingService;

    @Mock
    private ModelMapper mockModelMapper;

    @Mock
    private PasswordEncoder mockPasswordEncoder;

    @Mock
    private DBFileStorageService mockDBFileStorageService;

    @BeforeEach
    public void setUp() {
        serviceToTest = new UserServiceImpl(mockModelMapper, mockPasswordEncoder,
                mockAuthorityProcessingService, mockDBFileStorageService,
                mockUserRepository);
        realMapper = new ModelMapper();

        // Authorities
        studentAuthority = new AuthorityEntity();
        studentAuthority.setId(VALID_STUDENT_ID);
        studentAuthority.setAuthority(VALID_STUDENT_AUTHORITY);
        adminAuthority = new AuthorityEntity();
        adminAuthority.setId(VALID_ADMIN_ID);
        adminAuthority.setAuthority(VALID_ADMIN_AUTHORITY);

        // User
        userEntity = new UserEntity();
        userEntity.setId(VALID_USER_ID);
        userEntity.setFullName(VALID_FULLNAME);
        userEntity.setUsername(VALID_USERNAME);
        userEntity.setEmail(VALID_EMAIL);
        userEntity.setPhrase(VALID_PHRASE);
        userEntity.setPoints(VALID_POINTS);
        userEntity.setPassword(VALID_PASSWORD);
        userEntity.setGrade(VALID_GRADE);
        userEntity.setGradeClass(VALID_GRADE_CLASS);
        userEntity.setAuthorities(Set.of(studentAuthority, adminAuthority));
    }

    @Test
    public void testGetUserProfile() {
        // Arrange


        when(mockUserRepository.findByUsername(VALID_USERNAME))
                .thenReturn(Optional.of(userEntity));

        when(mockModelMapper.map(userEntity, UserProfileViewModel.class))
                .thenReturn(realMapper.map(userEntity, UserProfileViewModel.class));

        // Act
        UserProfileViewModel userProfileViewModel = this.serviceToTest.getUserProfile(VALID_USERNAME);

        // Assert
        Assertions.assertEquals(VALID_FULLNAME, userProfileViewModel.getFullName());
        Assertions.assertEquals(VALID_EMAIL, userProfileViewModel.getEmail());
    }


//    @DisplayName("Test number 1")
//    @Test
//    void findByNameReturnsCorrectUser() {
//        String name = "Pesho Ivanov";
//        assertTrue(userService.findByUsername("pesho").equals(name));
//    }

//    static UserRegisterBindingModel createPesho() {
//        UserRegisterBindingModel pesho = new UserRegisterBindingModel();
//        pesho.setFullName("Pesho Ivanov");
//        pesho.setUsername("pesho.ivanov");
//        pesho.setEmail("pesho.i@abv.bg");
//        pesho.setPassword("pass");
//        pesho.setConfirmPassword("pass");
//        pesho.setGrade(9);
//        pesho.setGradeClass("A");
//        pesho.setPhrase("Love JUnit!");
//        return pesho;
//    }

}