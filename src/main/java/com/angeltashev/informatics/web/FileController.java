package com.angeltashev.informatics.web;

import com.angeltashev.informatics.file.exception.FileStorageException;
import com.angeltashev.informatics.file.model.DBFile;
import com.angeltashev.informatics.file.service.DBFileStorageService;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@AllArgsConstructor
@Controller
@RequestMapping("/files")
public class FileController {

    private final DBFileStorageService fileStorageService;

    // TODO Refactor User to upload and download picture
    // TODO Refactor Assignment to upload and download resources

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/resources/download/{id}")
    public void downloadResources(@PathVariable("id") String resourcesId, HttpServletResponse response) {
        try {
            // get your file as InputStream
            DBFile file = this.fileStorageService.getFile(resourcesId);
            response.setContentType(file.getFileType());
            byte[] fileBytes = file.getData();
            InputStream is = new ByteArrayInputStream(file.getData());
            // copy it to response's OutputStream
            IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file to output stream");
        }
    }

}
