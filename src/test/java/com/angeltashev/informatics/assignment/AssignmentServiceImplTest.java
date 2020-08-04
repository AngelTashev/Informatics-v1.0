package com.angeltashev.informatics.assignment;

import com.angeltashev.informatics.assignment.exception.InvalidArgumentIdException;
import com.angeltashev.informatics.assignment.model.AssignmentEntity;
import com.angeltashev.informatics.assignment.model.CommentEntity;
import com.angeltashev.informatics.assignment.model.binding.AssignmentAddBindingModel;
import com.angeltashev.informatics.assignment.model.view.AssignmentAllViewModel;
import com.angeltashev.informatics.assignment.model.view.AssignmentDetailsViewModel;
import com.angeltashev.informatics.assignment.repository.AssignmentRepository;
import com.angeltashev.informatics.assignment.repository.CommentRepository;
import com.angeltashev.informatics.assignment.service.AssignmentService;
import com.angeltashev.informatics.assignment.service.impl.AssignmentServiceImpl;
import com.angeltashev.informatics.file.exception.FileStorageException;
import com.angeltashev.informatics.file.model.DBFile;
import com.angeltashev.informatics.file.service.DBFileStorageService;
import com.angeltashev.informatics.user.model.UserEntity;
import com.angeltashev.informatics.user.repository.UserRepository;
import com.angeltashev.informatics.user.service.UserService;
import org.hsqldb.rights.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AssignmentServiceImplTest {

    private String VALID_ID = "validId";
    private String VALID_NAME = "validName";
    private String VALID_DESCRIPTION = "validDescription";
    private Integer VALID_POINTS = 50;
    private LocalDateTime VALID_DUE_DATE = LocalDateTime.now().plusDays(3);
    private boolean VALID_ENABLED = true;

    private AssignmentService serviceToTest;
    private ModelMapper realMapper;

    private AssignmentEntity assignmentEntity;


    @Mock
    private ModelMapper mockModelMapper;

    @Mock
    private DBFileStorageService mockDBFileStorageService;
    @Mock
    private UserService mockUserService;


    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private AssignmentRepository mockAssignmentRepository;
    @Mock
    private CommentRepository mockCommentRepository;

    @BeforeEach
    void setUp() {
        this.serviceToTest = new AssignmentServiceImpl(mockModelMapper, mockDBFileStorageService, mockUserService,
                mockUserRepository, mockAssignmentRepository, mockCommentRepository);
        this.realMapper = new ModelMapper();

        assignmentEntity = new AssignmentEntity();
        assignmentEntity.setId(VALID_ID);
        assignmentEntity.setName(VALID_NAME);
        assignmentEntity.setDescription(VALID_DESCRIPTION);
        assignmentEntity.setPoints(VALID_POINTS);
        assignmentEntity.setDueDate(VALID_DUE_DATE);
        assignmentEntity.setEnabled(VALID_ENABLED);
    }

    @Test
    public void getAssignmentByIdAndUserShouldReturnCorrectAssignment() {
        // Arrange
        final String userUsername = "validUsername";
        UserEntity user = new UserEntity();
        user.setUsername(userUsername);
        assignmentEntity.setUser(user);
        when(this.mockAssignmentRepository.findById(VALID_ID))
                .thenReturn(Optional.of(assignmentEntity));
        when(this.mockModelMapper.map(assignmentEntity, AssignmentDetailsViewModel.class))
                .thenReturn(realMapper.map(assignmentEntity, AssignmentDetailsViewModel.class));

        // Act
        AssignmentDetailsViewModel assignmentModel = this.serviceToTest.getAssignmentByIdAndUser(VALID_ID, userUsername);

        // Assert
        assertEquals(VALID_NAME, assignmentModel.getName());
        assertEquals(VALID_DESCRIPTION, assignmentModel.getDescription());
    }

    @Test
    public void getAssignmentByIdAndUserShouldReturnNullOnInvalidId() {
        // Arrange
        final String invalidId = "invalidId";
        when(this.mockAssignmentRepository.findById(invalidId))
                .thenReturn(Optional.empty());

        // Act
        AssignmentDetailsViewModel assigmentModel = this.serviceToTest.getAssignmentByIdAndUser(invalidId, "validUsername");

        // Assert
        assertNull(assigmentModel);
    }

    @Test
    public void getAssignmentByIdAndUserShouldReturnNullOnInvalidUsername() {
        // Arrange
        final String invalidUsername = "invalidUsername";
        UserEntity user = new UserEntity();
        user.setUsername("validUsername");
        assignmentEntity.setUser(user);
        when(this.mockAssignmentRepository.findById(VALID_ID))
                .thenReturn(Optional.of(assignmentEntity));

        // Act
        AssignmentDetailsViewModel assigmentModel = this.serviceToTest.getAssignmentByIdAndUser(VALID_ID, invalidUsername);

        // Assert
        assertNull(assigmentModel);
    }

    @Test
    public void uploadSubmissionShouldReturnTrueOnNewSuccessfulUpload() throws FileStorageException {
        // Arrange
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        final String validFileName = "validFileName";
        DBFile file = new DBFile();
        file.setFileName(validFileName);
        UserEntity user = new UserEntity();
        user.setUsername("validUsername");
        assignmentEntity.setUser(user);
        when(this.mockAssignmentRepository.findById(VALID_ID))
                .thenReturn(Optional.of(assignmentEntity));
        when(this.mockDBFileStorageService.storeFile(multipartFile))
                .thenReturn(file);

        // Act
        boolean result = this.serviceToTest.uploadSubmission(VALID_ID, multipartFile);

        // Assert
        assertTrue(result);
        assertEquals(validFileName, assignmentEntity.getSubmission().getFileName());
        verify(this.mockAssignmentRepository, times(1)).save(assignmentEntity);
    }

    @Test
    public void uploadSubmissionShouldOverrideOldSubmission() throws FileStorageException {
        // Arrange
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        final String oldSubmissionId = "oldSubmissionId";
        final String oldSubmissionName = "oldSubmissionName";
        final String newSubmissionName = "newSubmissionName";
        DBFile oldSubmission = new DBFile();
        oldSubmission.setFileName(oldSubmissionName);
        oldSubmission.setId(oldSubmissionId);
        DBFile newSubmission = new DBFile();
        newSubmission.setFileName(newSubmissionName);
        UserEntity user = new UserEntity();
        user.setUsername("validUsername");
        assignmentEntity.setSubmission(oldSubmission);
        assignmentEntity.setUser(user);
        when(this.mockAssignmentRepository.findById(VALID_ID))
                .thenReturn(Optional.of(assignmentEntity));
        when(this.mockDBFileStorageService.storeFile(multipartFile))
                .thenReturn(newSubmission);

        // Act
        boolean result = this.serviceToTest.uploadSubmission(VALID_ID, multipartFile);

        // Assert
        assertTrue(result);
        assertEquals(newSubmissionName, assignmentEntity.getSubmission().getFileName());
        verify(this.mockDBFileStorageService, times(1)).deleteFile(oldSubmissionId);
        verify(this.mockAssignmentRepository, times(1)).save(assignmentEntity);
    }

    @Test
    public void uploadSubmissionShouldThrowInvalidArgumentIdExceptionOnInvalidId() {
        // Arrange
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        final String invalidId = "invalidId";
        when(this.mockAssignmentRepository.findById(invalidId))
                .thenReturn(Optional.empty());

        // Act
        // Assert
        assertThrows(InvalidArgumentIdException.class, () -> this.serviceToTest.uploadSubmission(invalidId, multipartFile));
    }

    @Test
    public void addAssignmentShouldAddSubmissionWithResources() throws FileStorageException, IOException {
        // Arrange
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        final String validFileName = "validFileName";
        final String validUsername = "validUsername";
        UserEntity user = new UserEntity();
        user.setUsername(validUsername);
        AssignmentAddBindingModel assignmentAddBindingModel = new AssignmentAddBindingModel();
        assignmentAddBindingModel.setUsers(List.of(validUsername));
        DBFile file = new DBFile();
        file.setFileName(validFileName);
        assignmentEntity.setEnabled(false);
        when(this.mockModelMapper.map(assignmentAddBindingModel, AssignmentEntity.class))
                .thenReturn(assignmentEntity);
        when(this.mockUserRepository.findByUsername(validUsername))
                .thenReturn(Optional.of(user));
        when(this.mockDBFileStorageService.storeFile(multipartFile))
                .thenReturn(file);

        // Act
        this.serviceToTest.addAssignment(assignmentAddBindingModel, multipartFile);

        // Assert
        assertEquals(validUsername, assignmentEntity.getUser().getUsername());
        assertEquals(validFileName, assignmentEntity.getResources().getFileName());
        assertTrue(assignmentEntity.isEnabled());
        verify(this.mockAssignmentRepository, times(1)).saveAndFlush(assignmentEntity);
    }

    @Test
    public void addAssignmentShouldAddSubmissionWithoutResources() throws IOException, FileStorageException {
        // Arrange
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        final String validFileName = "validFileName";
        final String validUsername = "validUsername";
        UserEntity user = new UserEntity();
        user.setUsername(validUsername);
        AssignmentAddBindingModel assignmentAddBindingModel = new AssignmentAddBindingModel();
        assignmentAddBindingModel.setUsers(List.of(validUsername));
        DBFile file = new DBFile();
        file.setFileName(validFileName);
        assignmentEntity.setEnabled(false);
        when(this.mockModelMapper.map(assignmentAddBindingModel, AssignmentEntity.class))
                .thenReturn(assignmentEntity);
        when(this.mockUserRepository.findByUsername(validUsername))
                .thenReturn(Optional.of(user));

        // Act
        this.serviceToTest.addAssignment(assignmentAddBindingModel, multipartFile);

        // Assert
        assertEquals(validUsername, assignmentEntity.getUser().getUsername());
        assertTrue(assignmentEntity.isEnabled());
        verify(this.mockAssignmentRepository, times(1)).saveAndFlush(assignmentEntity);
    }

    @Test
    public void addAssignmentShouldNotAddAssignmentsOnEmptyUsernameList() throws IOException, FileStorageException {
        // Arrange
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        AssignmentAddBindingModel assignmentAddBindingModel = new AssignmentAddBindingModel();
        assignmentAddBindingModel.setUsers(List.of());

        // Act
        this.serviceToTest.addAssignment(assignmentAddBindingModel, multipartFile);

        // Assert
        verify(this.mockAssignmentRepository, times(0)).saveAndFlush(Mockito.any(AssignmentEntity.class));
    }

    @Test
    public void getAllAssignmentsShouldReturnAllAssignments() {
        // Arrange
        final String validUsername = "validUsername";
        UserEntity user = new UserEntity();
        user.setUsername(validUsername);
        assignmentEntity.setUser(user);
        when(this.mockAssignmentRepository.findAll())
                .thenReturn(List.of(assignmentEntity));
        when(this.mockModelMapper.map(assignmentEntity, AssignmentAllViewModel.class))
                .thenReturn(this.realMapper.map(assignmentEntity, AssignmentAllViewModel.class));

        // Act
        List<AssignmentAllViewModel> assignments = this.serviceToTest.getAllAssignments();

        // Assert
        assertEquals(1, assignments.size());
        assertEquals(assignmentEntity.getId(), assignments.get(0).getId());
        assertEquals(user.getUsername(), assignments.get(0).getUsername());
    }

    @Test
    public void scoreAssignmentShouldReturnTrueOnSuccessfulScoringOfAssignmentWithComment() {
        // Arrange
        final Integer scoringPoints = 95;
        final String validUsername = "validUsername";
        final String commentString = "commentString";
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setComment(commentString);
        UserEntity user = new UserEntity();
        user.setUsername(validUsername);
        assignmentEntity.setUser(user);

        when(this.mockAssignmentRepository.findById(VALID_ID))
                .thenReturn(Optional.of(assignmentEntity));
        when(this.mockCommentRepository.save(Mockito.any(CommentEntity.class)))
                .thenReturn(commentEntity);

        // Act
        boolean result = this.serviceToTest.scoreAssigment(VALID_ID, scoringPoints, commentString);

        // Assert
        assertTrue(result);
        assertEquals(scoringPoints, assignmentEntity.getPoints());
        assertEquals(assignmentEntity.getComment().getComment(), commentString);
        verify(this.mockCommentRepository, times(1)).save(Mockito.any(CommentEntity.class));
        verify(this.mockUserService, times(1)).addPointsToUser(validUsername, scoringPoints);
        verify(this.mockAssignmentRepository, times(1)).save(assignmentEntity);
    }

    @Test
    public void scoreAssignmentShouldReturnTrueOnSuccessfulScoringOfAssignmentWithoutComment() {
        // Arrange
        final Integer scoringPoints = 95;
        final String validUsername = "validUsername";
        final String commentString = "";
        UserEntity user = new UserEntity();
        user.setUsername(validUsername);
        assignmentEntity.setUser(user);

        when(this.mockAssignmentRepository.findById(VALID_ID))
                .thenReturn(Optional.of(assignmentEntity));

        // Act
        boolean result = this.serviceToTest.scoreAssigment(VALID_ID, scoringPoints, commentString);

        // Assert
        assertTrue(result);
        assertNull(assignmentEntity.getComment());
        assertEquals(scoringPoints, assignmentEntity.getPoints());
        verify(this.mockUserService, times(1)).addPointsToUser(validUsername, scoringPoints);
        verify(this.mockAssignmentRepository, times(1)).save(assignmentEntity);
    }

    @Test
    public void scoreAssignmentShouldThrowInvalidArgumentIdExceptionOnInvalidId() {
        // Arrange
        final String invalidId = "invalidId";
        when(this.mockAssignmentRepository.findById(invalidId))
                .thenReturn(Optional.empty());

        // Act
        // Assert
        assertThrows(InvalidArgumentIdException.class, () -> this.serviceToTest.scoreAssigment(invalidId, 0, ""));
    }

    @Test
    public void disableOldAssignmentsShouldDisableOldAssignments() {
        // Arrange
        assignmentEntity.setDueDate(LocalDateTime.now().minusMinutes(30));
        when(this.mockAssignmentRepository.findAll())
                .thenReturn(List.of(assignmentEntity));

        // Act
        boolean result = this.serviceToTest.disableOldAssignments();

        // Assert
        assertTrue(result);
        assertFalse(assignmentEntity.isEnabled());
        verify(this.mockAssignmentRepository, times(1)).save(assignmentEntity);
    }

    @Test
    public void updateAllAssignmentEntitiesShouldReturnCorrectResults() {
        // Arrange
        when(this.mockAssignmentRepository.findAll())
                .thenReturn(List.of(assignmentEntity));

        // Act
        List<AssignmentEntity> assignments = this.serviceToTest.updateAllAssignments();

        // Assert
        assertEquals(1, assignments.size());
        assertEquals(VALID_ID, assignments.get(0).getId());
    }

    @Test
    public void deleteAssignmentByIdShouldReturnTrueOnSuccessfulDeletion() {
        // Arrange
        when(this.mockAssignmentRepository.findById(VALID_ID))
                .thenReturn(Optional.of(assignmentEntity));

        // Act
        boolean result = this.serviceToTest.deleteAssignmentById(VALID_ID);

        // Assert
        assertTrue(result);
        verify(this.mockAssignmentRepository, times(1)).delete(assignmentEntity);
    }

    @Test
    public void deleteAssignmentByIdShouldThrowsInvalidArgumentIdExceptionOnUnsuccessfulDeletion() {
        // Arrange
        final String invalidId = "invalidId";
        when(this.mockAssignmentRepository.findById(invalidId))
                .thenReturn(Optional.empty());

        // Act
        // Assert
        assertThrows(InvalidArgumentIdException.class, () -> this.serviceToTest.deleteAssignmentById(invalidId));
    }

    @Test
    public void cleanUpOldAssignmentsShouldReturnTrueOnDeletionOfOldAssignments() {
        // Arrange
        assignmentEntity.setDueDate(LocalDateTime.now().minusDays(4));
        when(this.mockAssignmentRepository.findAll())
                .thenReturn(List.of(assignmentEntity));

        // Act
        boolean result = this.serviceToTest.cleanUpOldAssignments();

        // Assert
        verify(this.mockAssignmentRepository, times(1)).delete(assignmentEntity);
    }

}
