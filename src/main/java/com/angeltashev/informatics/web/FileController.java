package com.angeltashev.informatics.web;

import com.angeltashev.informatics.file.exception.FileStorageException;
import com.angeltashev.informatics.file.model.DBFile;
import com.angeltashev.informatics.file.service.DBFileStorageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/files")
public class FileController {

    private final DBFileStorageService fileStorageService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/resources/download/{id}")
    public void downloadResources(@PathVariable("id") String resourcesId, HttpServletResponse response) {
        log.info("Download resources: Downloading resources of the assignment");
        getDownloadableFileById(resourcesId, response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/submissions/download/{id}")
    public void downloadUserSubmission(@PathVariable("id") String submissionId, HttpServletResponse response) {
        log.info("Download submission: Downloading submission of the assignment");
        getDownloadableFileById(submissionId, response);
    }

    private void getDownloadableFileById(String fileId, HttpServletResponse response) {
        try {
            DBFile file = this.fileStorageService.getFile(fileId);
            byte[] fileBytes = file.getData();

            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getFileName());
            response.setContentType(file.getFileType());
            InputStream is = new ByteArrayInputStream(file.getData());
            IOUtils.copy(is, response.getOutputStream());

            log.info("Get downloadable file by id: Successfully got downloadable file with id: " + fileId);

            response.flushBuffer();
        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file to output stream");
        }
    }

}
