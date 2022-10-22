package ru.mcko.registry.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mcko.registry.entity.FileInfo;
import ru.mcko.registry.service.FilesStorageService;

@RestController
public class FileUploadController {
    private final FilesStorageService storageService;

    public FileUploadController(FilesStorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/file/upload")
    public FileInfo uploadFile(@RequestParam("file") MultipartFile file) {

        try {
            return storageService.save(file);
        } catch (Exception e) {
//            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return null;
        }
    }
}
