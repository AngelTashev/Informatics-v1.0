package com.angeltashev.informatics.file;

import com.angeltashev.informatics.file.exception.FileStorageException;
import com.angeltashev.informatics.file.model.DBFile;
import com.angeltashev.informatics.file.repository.DBFileRepository;
import com.angeltashev.informatics.file.service.CloudinaryService;
import com.angeltashev.informatics.file.service.DBFileStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DBFileStorageServiceTest {

    private final String VALID_ID = "validId";
    private final String VALID_FILE_NAME = "validFileName.txt";
    private final String VALID_FILE_TYPE = "validFileType";
    private final byte[] VALID_DATA = new byte[] {11, 11};

    private DBFile dbFile;

    @Mock
    private DBFileRepository mockDBFileRepository;
    @Mock
    private CloudinaryService cloudinaryService;

    private DBFileStorageService serviceToTest;

    @BeforeEach
    void setUp() {
        serviceToTest = new DBFileStorageService(cloudinaryService, mockDBFileRepository);

        dbFile = new DBFile();
        dbFile.setId(VALID_ID);
        dbFile.setFileName(VALID_FILE_NAME);
        dbFile.setFileType(VALID_FILE_TYPE);
        dbFile.setData(VALID_DATA);
    }

    @Test
    public void storeFileShouldStoreFileCorrectly() throws IOException, FileStorageException {
        // Arrange
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename())
                .thenReturn(VALID_FILE_NAME);
        when(multipartFile.getContentType())
                .thenReturn(VALID_FILE_TYPE);
        when(multipartFile.getBytes())
                .thenReturn(VALID_DATA);

        when(this.mockDBFileRepository.save(Mockito.any(DBFile.class)))
                .thenReturn(dbFile);

        // Act
        DBFile file = this.serviceToTest.storeFile(multipartFile);

        // Assert
        assertEquals(VALID_ID, dbFile.getId());
        assertEquals(VALID_FILE_NAME, dbFile.getFileName());
        assertEquals(VALID_FILE_TYPE, dbFile.getFileType());
        assertEquals(VALID_DATA, dbFile.getData());
    }

    @Test
    public void storeFileShouldStoreUploadBackupFileForPictures() throws IOException, FileStorageException {
        // Arrange
        final String validImageType = "image/x-png";
        final String validBackupUrl = "validBackupUrl";
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        dbFile.setBackupUrl(validBackupUrl);
        dbFile.setFileType(validImageType);
        when(multipartFile.getOriginalFilename())
                .thenReturn(VALID_FILE_NAME);
        when(multipartFile.getBytes())
                .thenReturn(VALID_DATA);
        when(multipartFile.getContentType())
                .thenReturn(validImageType);

        when(this.mockDBFileRepository.save(Mockito.any(DBFile.class)))
                .thenReturn(dbFile);
        when(this.cloudinaryService.uploadFile(multipartFile))
                .thenReturn(validBackupUrl);

        // Act
        DBFile file = this.serviceToTest.storeFile(multipartFile);

        // Assert
        assertEquals(VALID_ID, dbFile.getId());
        assertEquals(VALID_FILE_NAME, dbFile.getFileName());
        assertEquals(validImageType, dbFile.getFileType());
        assertEquals(VALID_DATA, dbFile.getData());
        assertEquals(validBackupUrl, dbFile.getBackupUrl());
        verify(this.cloudinaryService, times(1)).uploadFile(multipartFile);
    }

    @Test
    public void storeFileShouldThrowFileStorageExceptionOnInvalidFileName() {
        // Arrange
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename())
                .thenReturn("invalid..name");

        // Act
        // Assert
        assertThrows(FileStorageException.class, () -> this.serviceToTest.storeFile(multipartFile));

    }

    @Test
    public void getFileShouldReturnCorrectFile() throws FileNotFoundException {
        // Arrange
        when(this.mockDBFileRepository.findById(VALID_ID))
                .thenReturn(Optional.of(dbFile));

        // Act
        DBFile file = this.serviceToTest.getFile(VALID_ID);

        // Assert
        assertEquals(VALID_ID, file.getId());
        assertEquals(VALID_FILE_NAME, file.getFileName());
        assertEquals(VALID_FILE_TYPE, file.getFileType());
        assertEquals(VALID_DATA, file.getData());
    }

    @Test
    public void getFileShouldThrowFileNotFoundExceptionOnInvalidId() throws FileNotFoundException {
        // Arrange
        final String invalidId = "invalidId";
        when(this.mockDBFileRepository.findById(invalidId))
                .thenReturn(Optional.empty());

        // Act
        // Assert
        assertThrows(FileNotFoundException.class, () -> this.serviceToTest.getFile(invalidId));
    }

    @Test
    public void deleteFileShouldCorrectlyDeleteFile() {
        // Arrange

        // Act
        this.serviceToTest.deleteFile(VALID_ID);

        // Assert
        verify(this.mockDBFileRepository, times(1)).deleteById(VALID_ID);
    }

    @Test
    public void getAllFilesShouldReturnAllFiles() {
        // Arrange
        when(this.mockDBFileRepository.findAll())
                .thenReturn(List.of(dbFile));

        // Act
        List<DBFile> files = this.serviceToTest.getAllFiles();

        // Assert
        assertEquals(VALID_ID, files.get(0).getId());
    }
}
