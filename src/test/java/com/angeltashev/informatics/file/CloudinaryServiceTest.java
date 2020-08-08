package com.angeltashev.informatics.file;

import com.angeltashev.informatics.file.service.CloudinaryService;
import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CloudinaryServiceTest {

    private CloudinaryService serviceToTest;

    private final String VALID_FILE_NAME = "validFileName.file";
    private final String VALID_URL = "validUrl";

    @Mock
    private Uploader mockUploader;
    @Mock
    private Map mockMap;
    @Mock
    private Object mockObject;

    @Mock
    private Cloudinary mockCloudinary;
    @Mock
    private MultipartFile mockMultipartFile;

    @BeforeEach
    void setUp() {
        this.serviceToTest = new CloudinaryService(mockCloudinary);
    }

    @Test
    void uploadFileShouldStoreFileAndReturnCorrectFileUrl() throws IOException {
        // Arrange
        when(this.mockCloudinary.uploader())
                .thenReturn(mockUploader);
        when(this.mockUploader.upload(Mockito.any(File.class), Mockito.any(HashMap.class)))
                .thenReturn(mockMap);
        when(this.mockMap.get("url"))
                .thenReturn(mockObject);
        when(this.mockObject
                .toString())
                .thenReturn(VALID_URL);
        when(this.mockMultipartFile.getOriginalFilename())
                .thenReturn(VALID_FILE_NAME);

        // Act
        String result = this.serviceToTest.uploadFile(mockMultipartFile);

        // Assert
        assertEquals(VALID_URL, result);

    }
}
