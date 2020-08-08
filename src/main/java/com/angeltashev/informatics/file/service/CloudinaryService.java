package com.angeltashev.informatics.file.service;

import com.cloudinary.Cloudinary;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

@Slf4j
@Service
@AllArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public String uploadFile(MultipartFile multipartFile) throws IOException {
        File file = File.createTempFile("temp-file", multipartFile.getOriginalFilename());
        multipartFile.transferTo(file);
        log.info("Upload file: Uploading backup file " + multipartFile.getOriginalFilename() + " into Cloudinary");
        return this.cloudinary
                .uploader()
                .upload(file, new HashMap())
                .get("url")
                .toString();
    }
}
