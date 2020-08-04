package com.angeltashev.informatics.file.service;

import com.angeltashev.informatics.file.exception.FileStorageException;
import com.angeltashev.informatics.file.model.DBFile;
import com.angeltashev.informatics.file.repository.DBFileRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
@Service
public class DBFileStorageService {

    private final DBFileRepository fileRepo;

    public DBFile storeFile(MultipartFile file) throws FileStorageException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            if (fileName.contains("..")) {
                log.error("Store file: Filename contains invalid characters!");
                throw new FileStorageException("Filename contains invalid characters!");
            }

            DBFile dbFile = new DBFile(fileName, file.getContentType(), file.getBytes());
            log.info("Store file: Successfully stored file " + fileName + ", type: " + file.getContentType());
            return this.fileRepo.save(dbFile);
        } catch (IOException | FileStorageException e) {
            log.error("Store file: Could not store file " + fileName + ". Please try again!");
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", e);
        }
    }

    public DBFile getFile(String fileId) throws FileNotFoundException {
        log.info("Get file: Retrieving file with id: " + fileId);
        return this.fileRepo.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File with id " + fileId + " not found!"));
    }

    public void deleteFile(String fileId) {
        log.info("Delete file: Deleting file with id: " + fileId);
        this.fileRepo.deleteById(fileId);

    }

    public List<DBFile> getAllFiles() {
        log.info("Get all files: Retrieving all files");
        return this.fileRepo.findAll();
    }
}
