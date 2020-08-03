package com.angeltashev.informatics.user.service.impl;

import com.angeltashev.informatics.assignment.model.AssignmentEntity;
import com.angeltashev.informatics.exceptions.PageNotFoundException;
import com.angeltashev.informatics.file.exception.FileStorageException;
import com.angeltashev.informatics.file.model.DBFile;
import com.angeltashev.informatics.file.service.DBFileStorageService;
import com.angeltashev.informatics.user.model.AuthorityEntity;
import com.angeltashev.informatics.user.model.UserEntity;
import com.angeltashev.informatics.user.model.binding.UserAssignmentAddBindingModel;
import com.angeltashev.informatics.user.model.binding.UserDTO;
import com.angeltashev.informatics.user.model.binding.UserRegisterBindingModel;
import com.angeltashev.informatics.user.model.view.*;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    public void testRegisterUser() {
        // Arrange
        UserRegisterBindingModel userRegisterBindingModel = this.realMapper.map(userEntity, UserRegisterBindingModel.class);
        long startingCount = this.mockUserRepository.count();
        when(this.mockModelMapper.map(userRegisterBindingModel, UserEntity.class))
                .thenReturn(this.realMapper.map(userRegisterBindingModel, UserEntity.class));
        when(this.mockAuthorityProcessingService.getStudentAuthority())
                .thenReturn(studentAuthority);
        when(this.mockUserRepository.saveAndFlush(Mockito.any(UserEntity.class)))
                .thenReturn(new UserEntity());

        // Act
        this.serviceToTest.registerUser(userRegisterBindingModel);

        // Assert
        verify(this.mockUserRepository, times(1)).saveAndFlush(Mockito.any(UserEntity.class));

    }

    @Test
    public void getUserProfileShouldReturnProfileWithNoPicture() {
        // Arrange
        when(this.mockModelMapper.map(userEntity, UserProfileViewModel.class))
                .thenReturn(this.realMapper.map(userEntity, UserProfileViewModel.class));
        when(this.mockUserRepository.findByUsername(VALID_USERNAME))
                .thenReturn(Optional.of(userEntity));

        // Act
        UserProfileViewModel userProfileViewModel = this.serviceToTest.getUserProfile(VALID_USERNAME);

        // Assert
        assertEquals(userProfileViewModel.getFullName(), VALID_FULLNAME);
        assertEquals(userProfileViewModel.getProfilePictureString(), "");
    }

    @Test
    public void getUserProfileShouldReturnProfileWithPicture() {
        // Arrange
        when(this.mockModelMapper.map(userEntity, UserProfileViewModel.class))
                .thenReturn(this.realMapper.map(userEntity, UserProfileViewModel.class));
        when(this.mockUserRepository.findByUsername(VALID_USERNAME))
                .thenReturn(Optional.of(userEntity));
        DBFile dbFile = new DBFile();
        dbFile.setData(new byte[] {11, 11});
        userEntity.setProfilePicture(dbFile);

        // Act
        UserProfileViewModel userProfileViewModel = this.serviceToTest.getUserProfile(VALID_USERNAME);

        // Assert
        assertEquals(userProfileViewModel.getFullName(), VALID_FULLNAME);
        assertEquals(userProfileViewModel.getProfilePictureString(), Base64.getEncoder().encodeToString(new byte[]{11, 11}));
    }

    @Test
    public void getUserProfileShouldThrowUsernameNotFoundExceptionOnInvalidUsername() {
        // Arrange
        final String invalidUsername = "Invalid username";
        when(this.mockUserRepository.findByUsername(invalidUsername))
                .thenReturn(Optional.empty());
        // Act // Assert
        assertThrows(UsernameNotFoundException.class, () -> this.serviceToTest.getUserProfile(invalidUsername));
    }


    @Test
    public void getUserVisitProfileShouldReturnProfileWithNoPicture() {
        // Arrange
        when(this.mockModelMapper.map(userEntity, UserVisitViewModel.class))
                .thenReturn(this.realMapper.map(userEntity, UserVisitViewModel.class));
        when(this.mockUserRepository.findByUsername(VALID_USERNAME))
                .thenReturn(Optional.of(userEntity));

        // Act
        UserVisitViewModel userVisitViewModel = this.serviceToTest.getUserVisitProfile(VALID_USERNAME);

        // Assert
        assertEquals(userVisitViewModel.getFullName(), VALID_FULLNAME);
        assertEquals(userVisitViewModel.getProfilePictureString(), "");
    }

    @Test
    public void getUserVisitProfileShouldReturnProfileWithPicture() {
        // Arrange
        when(this.mockModelMapper.map(userEntity, UserVisitViewModel.class))
                .thenReturn(this.realMapper.map(userEntity, UserVisitViewModel.class));
        when(this.mockUserRepository.findByUsername(VALID_USERNAME))
                .thenReturn(Optional.of(userEntity));
        DBFile dbFile = new DBFile();
        dbFile.setData(new byte[] {11, 11});
        userEntity.setProfilePicture(dbFile);

        // Act
        UserVisitViewModel userVisitViewModel = this.serviceToTest.getUserVisitProfile(VALID_USERNAME);

        // Assert
        assertEquals(userVisitViewModel.getFullName(), VALID_FULLNAME);
        assertEquals(userVisitViewModel.getProfilePictureString(), Base64.getEncoder().encodeToString(new byte[]{11, 11}));
    }

    @Test
    public void getUserVisitProfileShouldThrowPageNotFoundExceptionOnInvalidUsername() {
        // Arrange
        final String invalidUsername = "Invalid username";
        when(this.mockUserRepository.findByUsername(invalidUsername))
                .thenReturn(Optional.empty());
        // Act // Assert
        assertThrows(PageNotFoundException.class, () -> this.serviceToTest.getUserVisitProfile(invalidUsername));
    }

    @Test
    public void getUserHomeDetailsShouldReturnCorrectUserModel() {
        // Arrange
        when(this.mockUserRepository.findByUsername(VALID_USERNAME))
                .thenReturn(Optional.of(userEntity));
        when(this.mockModelMapper.map(userEntity, UserHomeViewModel.class))
                .thenReturn(this.realMapper.map(userEntity, UserHomeViewModel.class));

        // Act
        UserHomeViewModel userHomeViewModel = this.serviceToTest.getUserHomeDetails(VALID_USERNAME);

        // Assert
        assertEquals(userHomeViewModel.getFullName(), VALID_FULLNAME);
    }

    @Test
    public void findByUsernameShouldReturnCorrectUserDTO() {
        // Arrange
        when(this.mockUserRepository.findByUsername(VALID_USERNAME))
                .thenReturn(Optional.of(userEntity));
        when(this.mockModelMapper.map(userEntity, UserDTO.class))
                .thenReturn(this.realMapper.map(userEntity, UserDTO.class));

        // Act
        UserDTO userDTO = this.serviceToTest.findByUsername(VALID_USERNAME);

        // Assert
        assertNotEquals(null, userDTO);
    }

    @Test
    public void findByUsernameShouldReturnNullOnInvalidUsername() {
        // Arrange
        final String invalidUsername = "Invalid username";
        when(this.mockUserRepository.findByUsername(invalidUsername))
                .thenReturn(Optional.empty());

        // Act
        UserDTO userDTO = this.serviceToTest.findByUsername(invalidUsername);

        // Assert
        assertEquals(null, userDTO);
    }

    @Test
    public void findByEmailShouldReturnCorrectUserDTO() {
        // Arrange
        when(this.mockUserRepository.findByEmail(VALID_EMAIL))
                .thenReturn(Optional.of(userEntity));
        when(this.mockModelMapper.map(userEntity, UserDTO.class))
                .thenReturn(this.realMapper.map(userEntity, UserDTO.class));

        // Act
        UserDTO userDTO = this.serviceToTest.findByEmail(VALID_EMAIL);

        // Assert
        assertNotEquals(null, userDTO);
    }

    @Test
    public void findByEmailShouldReturnNullOnInvalidEmail() {
        // Arrange
        final String invalidEmail = "Invalid email";
        when(this.mockUserRepository.findByUsername(invalidEmail))
                .thenReturn(Optional.empty());

        // Act
        UserDTO userDTO = this.serviceToTest.findByUsername(invalidEmail);

        // Assert
        assertEquals(null, userDTO);
    }

    @Test
    public void getUserAssignmentAddModelsShouldReturnAllAssignmentModels() {
        // Arrange
        final String assignmentName = "Assignment name";
        final AssignmentEntity assignmentEntity = new AssignmentEntity();
        assignmentEntity.setName(assignmentName);
        userEntity.setAssignments(Set.of(assignmentEntity));
        when(this.mockUserRepository.findAll())
                .thenReturn(List.of(userEntity));
        when(this.mockModelMapper.map(userEntity, UserAssignmentAddBindingModel.class))
                .thenReturn(this.realMapper.map(userEntity, UserAssignmentAddBindingModel.class));

        // Act
        List<UserAssignmentAddBindingModel> assignmentAddBindingModels = this.serviceToTest.getUserAssignmentAddModels();

        assertEquals(assignmentAddBindingModels.get(0).getFullName(), VALID_FULLNAME);
    }

    @Test
    public void uploadProfilePictureShouldUploadNewPicture() throws FileStorageException, IOException {
        // Arrange
        DBFile dbFile = new DBFile();
        dbFile.setData(new byte[] {11, 11});
        userEntity.setProfilePicture(dbFile);
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(this.mockUserRepository.findByUsername(VALID_USERNAME))
                .thenReturn(Optional.of(userEntity));
        when(this.mockDBFileStorageService.storeFile(multipartFile))
                .thenReturn(dbFile);

        // Act
        this.serviceToTest.uploadProfilePicture(VALID_USERNAME, multipartFile);

        // Assert
        assertEquals(userEntity.getProfilePicture().getData(), dbFile.getData());
    }

    @Test
    public void uploadProfilePictureShouldThrowUsernameNotFoundExceptionOnInvalidUsername() throws FileStorageException, IOException {
        // Arrange
        MultipartFile multipartFile = mock(MultipartFile.class);
        final String invalidUsername = "Invalid username";
        when(this.mockUserRepository.findByUsername(invalidUsername))
                .thenReturn(Optional.empty());

        // Act
        // Assert
        assertThrows(UsernameNotFoundException.class, () -> this.serviceToTest.uploadProfilePicture(invalidUsername, multipartFile));
    }

    @Test
    public void uploadProfilePictureShouldOverrideOldPicture() throws FileStorageException, IOException {
        // Arrange
        final String oldFileId = "OldFileId";
        DBFile dbFile = new DBFile();
        dbFile.setData(new byte[] {11, 11});
        dbFile.setId(oldFileId);
        DBFile newDBFile = new DBFile();
        newDBFile.setData(new byte[] {11, 12});
        userEntity.setProfilePicture(dbFile);
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(this.mockUserRepository.findByUsername(VALID_USERNAME))
                .thenReturn(Optional.of(userEntity));
        when(this.mockDBFileStorageService.storeFile(multipartFile))
                .thenReturn(newDBFile);

        // Act
        this.serviceToTest.uploadProfilePicture(VALID_USERNAME, multipartFile);

        // Assert
        verify(this.mockDBFileStorageService, times(1)).deleteFile(oldFileId);
    }

    @Test
    public void uploadProfilePictureShouldReturnFalseOnNullFile() throws FileStorageException, IOException {
        // Arrange
        MultipartFile multipartFile = null;
        when(this.mockUserRepository.findByUsername(VALID_USERNAME))
                .thenReturn(Optional.of(userEntity));

        // Act
        // Assert
        assertEquals(false, this.serviceToTest.uploadProfilePicture(VALID_USERNAME, multipartFile));
    }

    @Test
    public void getUserDTOShouldReturnCorrectUserDTO() {
        // Arrange
        when(this.mockUserRepository.findByUsername(VALID_USERNAME))
                .thenReturn(Optional.of(userEntity));
        when(this.mockModelMapper.map(userEntity, UserDTO.class))
                .thenReturn(this.realMapper.map(userEntity, UserDTO.class));

        // Act
        UserDTO userDTO = this.serviceToTest.getUserDTO(VALID_USERNAME);

        // Assert
        assertNotEquals(null, userDTO);
    }

    @Test
    public void getUserDTOShouldThrowUsernameNotFoundExceptionOnInvalidUsername() {
        // Arrange
        final String invalidUsername = "Invalid username";
        when(this.mockUserRepository.findByUsername(invalidUsername))
                .thenReturn(Optional.empty());

        // Act
        // Assert
        assertThrows(UsernameNotFoundException.class, () -> this.serviceToTest.getUserDTO(invalidUsername));
    }

    @Test
    public void addPointsToUserShouldAddPointsToUser() {
        // Arrange
        final Integer oldPoints = userEntity.getPoints();
        final Integer pointsToAdd = 50;
        when(this.mockUserRepository.findByUsername(VALID_USERNAME))
                .thenReturn(Optional.of(userEntity));

        // Act
        this.serviceToTest.addPointsToUser(VALID_USERNAME, pointsToAdd);

        // Assert
        assertEquals(oldPoints + pointsToAdd, userEntity.getPoints());

    }

    @Test
    public void addPointsToUserShouldThrowUsernameNotFoundExceptionOnInvalidUsername() {
        // Arrange
        final String invalidUsername = "Invalid username";
        when(this.mockUserRepository.findByUsername(invalidUsername))
                .thenReturn(Optional.empty());

        // Act
        // Assert
        assertThrows(UsernameNotFoundException.class, () -> this.serviceToTest.addPointsToUser(invalidUsername, 50));
    }

    @Test
    public void updateAllStudentsShouldReturnAllStudents() {
        // Arrange
        when(this.mockUserRepository.findAll())
                .thenReturn(List.of(userEntity));

        // Act
        List<UserEntity> allStudents = this.serviceToTest.updateAllStudents();

        // Assert
        assertEquals(userEntity.getFullName(), allStudents.get(0).getFullName());
    }

    @Test
    public void getAllAdminsShouldReturnAllAdmins() {
        // Arrange
        when(this.mockUserRepository.findAll())
                .thenReturn(List.of(userEntity));
        when(this.mockModelMapper.map(userEntity, UserRoleViewModel.class))
                .thenReturn(this.realMapper.map(userEntity, UserRoleViewModel.class));

        // Act
        List<UserRoleViewModel> users = this.serviceToTest.getAllAdmins();

        // Assert
        assertEquals(userEntity.getFullName(), users.get(0).getFullName());
    }

    @Test
    public void getAllAdminsShouldReturnEmptyListOnStudents() {
        // Arrange
        userEntity.setAuthorities(Set.of(studentAuthority));
        when(this.mockUserRepository.findAll())
                .thenReturn(List.of(userEntity));

        // Act
        List<UserRoleViewModel> users = this.serviceToTest.getAllAdmins();

        // Assert
        assertEquals(0, users.size());
    }

    @Test
    public void getAllStudentsShouldReturnAllStudents() {
        // Arrange
        when(this.mockUserRepository.findAll())
                .thenReturn(List.of(userEntity));
        when(this.mockModelMapper.map(userEntity, UserRoleViewModel.class))
                .thenReturn(this.realMapper.map(userEntity, UserRoleViewModel.class));

        // Act
        List<UserRoleViewModel> users = this.serviceToTest.getAllStudents();

        // Assert
        assertEquals(userEntity.getFullName(), users.get(0).getFullName());
    }

    @Test
    public void getAllStudentsShouldReturnEmptyListOnAdmins() {
        // Arrange
        userEntity.setAuthorities(Set.of(adminAuthority));
        when(this.mockUserRepository.findAll())
                .thenReturn(List.of(userEntity));

        // Act
        List<UserRoleViewModel> users = this.serviceToTest.getAllStudents();

        // Assert
        assertEquals(0, users.size());
    }

    @Test
    public void demoteAdminByIdShouldDemoteCorrectUser() {
        // Arrange
        when(this.mockUserRepository.findById(VALID_USER_ID))
                .thenReturn(Optional.of(userEntity));
        when(this.mockAuthorityProcessingService.getStudentAuthority())
                .thenReturn(studentAuthority);

        // Act
        this.serviceToTest.demoteAdminById(VALID_USER_ID);

        // Assert
        assertEquals(1, userEntity.getAuthorities().size());
        assertEquals(true, userEntity.getAuthorities().contains(studentAuthority));
    }

    @Test
    public void demoteAdminByIdShouldThrowUsernameNotFoundExceptionOnInvalidId() {
        // Arrange
        final String invalidId = "invalidId";
        when(this.mockUserRepository.findById(invalidId))
                .thenReturn(Optional.empty());

        // Act
        // Assert
        assertThrows(UsernameNotFoundException.class, () -> this.serviceToTest.demoteAdminById(invalidId));
    }

    @Test
    public void promoteStudentByIdShouldPromoteCorrectUser() {
        // Arrange
        when(this.mockUserRepository.findById(VALID_USER_ID))
                .thenReturn(Optional.of(userEntity));
        when(this.mockAuthorityProcessingService.getAdminAuthority())
                .thenReturn(adminAuthority);

        // Act
        this.serviceToTest.promoteStudentById(VALID_USER_ID);

        // Assert
        assertEquals(1, userEntity.getAuthorities().size());
        assertEquals(true, userEntity.getAuthorities().contains(adminAuthority));
    }

    @Test
    public void promoteStudentByIdShouldThrowUsernameNotFoundExceptionOnInvalidId() {
        // Arrange
        final String invalidId = "invalidId";
        when(this.mockUserRepository.findById(invalidId))
                .thenReturn(Optional.empty());

        // Act
        // Assert
        assertThrows(UsernameNotFoundException.class, () -> this.serviceToTest.promoteStudentById(invalidId));
    }

    @Test
    public void changePhraseShouldChangePhrase() {
        // Arrange
        String newPhrase = "New phrase";
        when(this.mockUserRepository.findByUsername(VALID_USERNAME))
                .thenReturn(Optional.of(userEntity));

        // Act
        this.serviceToTest.changePhrase(VALID_USERNAME, newPhrase);

        // Assert
        assertEquals(newPhrase, userEntity.getPhrase());
    }

    @Test
    public void changePhraseShouldThrowUsernameNotFoundExceptionOnInvalidUsername() {
        // Arrange
        final String invalidUsername = "Invalid username";
        when(this.mockUserRepository.findByUsername(invalidUsername))
                .thenReturn(Optional.empty());

        // Act
        // Assert
        assertThrows(UsernameNotFoundException.class, () -> this.serviceToTest.changePhrase(invalidUsername, ""));
    }

    @Test
    public void getAllAdminsPicturesShouldReturnAllAdminsPictures() {
        // Arrange
        DBFile dbFile = new DBFile();
        dbFile.setData(new byte[] {11, 11});
        userEntity.setProfilePicture(dbFile);
        when(this.mockUserRepository.findAll())
                .thenReturn(List.of(userEntity));

        // Act
        List<UserAboutViewModel> users = this.serviceToTest.getAllAdminsPictures();

        // Assert
        assertEquals(userEntity.getFullName(), users.get(0).getFullName());
        assertEquals(Base64.getEncoder().encodeToString(new byte[]{11, 11}), users.get(0).getProfilePictureString());
    }

    @Test
    public void getAllAdminsPicturesShouldReturnAllAdminsWithNoPictures() {
        // Arrange
        when(this.mockUserRepository.findAll())
                .thenReturn(List.of(userEntity));

        // Act
        List<UserAboutViewModel> users = this.serviceToTest.getAllAdminsPictures();

        // Assert
        assertEquals(userEntity.getFullName(), users.get(0).getFullName());
        assertEquals("", users.get(0).getProfilePictureString());
    }

    @Test
    public void getAllAdminsPicturesShouldReturnEmptyListOnStudents() {
        // Arrange
        userEntity.setAuthorities(Set.of(studentAuthority));
        when(this.mockUserRepository.findAll())
                .thenReturn(List.of(userEntity));

        // Act
        List<UserAboutViewModel> users = this.serviceToTest.getAllAdminsPictures();

        // Assert
        assertEquals(0, users.size());
    }

    @Test
    public void getUserProfileShouldBeReturnedWithAdminRole() {
        // Arrange
        when(this.mockUserRepository.findByUsername(VALID_USERNAME))
                .thenReturn(Optional.of(userEntity));
        when(this.mockModelMapper.map(userEntity, UserProfileViewModel.class))
                .thenReturn(this.realMapper.map(userEntity, UserProfileViewModel.class));

        // Act
        UserProfileViewModel userProfileViewModel = this.serviceToTest.getUserProfile(VALID_USERNAME);

        // Assert
        Assertions.assertEquals(VALID_FULLNAME, userProfileViewModel.getFullName());
        Assertions.assertEquals(VALID_EMAIL, userProfileViewModel.getEmail());
    }

    @Test
    public void getUserProfileShouldBeReturnedWithStudentRole() {
        // Arrange
        userEntity.setAuthorities(Set.of(studentAuthority));
        when(this.mockUserRepository.findByUsername(VALID_USERNAME))
                .thenReturn(Optional.of(userEntity));
        when(this.mockModelMapper.map(userEntity, UserProfileViewModel.class))
                .thenReturn(this.realMapper.map(userEntity, UserProfileViewModel.class));

        // Act
        UserProfileViewModel userProfileViewModel = this.serviceToTest.getUserProfile(VALID_USERNAME);

        // Assert
        Assertions.assertEquals(VALID_FULLNAME, userProfileViewModel.getFullName());
        Assertions.assertEquals(VALID_EMAIL, userProfileViewModel.getEmail());
    }

}
