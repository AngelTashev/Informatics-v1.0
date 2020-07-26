package com.angeltashev.informatics.file.service;

import com.angeltashev.informatics.file.exception.FileStorageException;
import com.angeltashev.informatics.file.model.DBFile;
import com.angeltashev.informatics.file.repository.DBFileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@Service
public class DBFileStorageService {

    private final DBFileRepository fileRepo;

    public DBFile storeFile(MultipartFile file) throws FileStorageException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (fileName.contains("..")) {
                throw new FileStorageException("Filename contains invalid characters!");
            }

            DBFile dbFile = new DBFile(fileName, file.getContentType(), file.getBytes());
            return this.fileRepo.save(dbFile);
        } catch (IOException | FileStorageException e) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", e);
        }
    }

    public DBFile getFile(String fileId) throws FileNotFoundException {
        return this.fileRepo.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File with id " + fileId + " not found!"));
    }

    public void deleteFile(String fileId) {
        this.fileRepo.deleteById(fileId);

    }

    public List<DBFile> getAllFiles() {
        return this.fileRepo.findAll();
    }
}
