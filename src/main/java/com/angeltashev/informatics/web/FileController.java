package com.angeltashev.informatics.web;

import com.angeltashev.informatics.file.exception.FileStorageException;
import com.angeltashev.informatics.file.service.DBFileStorageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@AllArgsConstructor
@Controller
@RequestMapping("/files")
public class FileController {

    private final DBFileStorageService fileService;

    @GetMapping("/upload")
    public ModelAndView getFileUpload() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("file-upload");
        modelAndView.addObject("files", fileService.getAllFiles());
        return modelAndView;
    }

    @PostMapping("upload")
    public String confirmUploadFile(@RequestParam("file") MultipartFile file) {
        try {
            fileService.storeFile(file);
        } catch (FileStorageException e) {
            e.printStackTrace();
        }
        return "redirect:/files/upload";
    }
}
