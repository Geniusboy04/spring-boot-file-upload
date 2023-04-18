package com.example.demo.controller;

import com.example.demo.model.FileStorage;
import com.example.demo.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileUrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;

@RestController
@RequiredArgsConstructor
public class FileStorageController {

    private final FileStorageService fileStorageService;

    @Value("${upload.folder}")
    private String uploadFolder;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile multipartFile) {
        fileStorageService.save(multipartFile);
        return ResponseEntity.ok(multipartFile.getOriginalFilename() + "file saqlandi");
    }

    @GetMapping("/preview/{hashId}")
    public ResponseEntity<?> previewFile(@PathVariable String hashId) throws Exception {

        FileStorage fileStorage = fileStorageService.findByHashId(hashId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; fileName=\"" + URLEncoder.encode(fileStorage.getName()))
                .contentType(MediaType.parseMediaType(fileStorage.getContentType()))
                .contentLength(fileStorage.getFileSize())
                .body(new FileUrlResource(String.format("%s/%s", uploadFolder, fileStorage.getUploadPath())));
    }

    @GetMapping("/download/{hashId}")
    public ResponseEntity<?> downloadFile(@PathVariable String hashId) throws Exception {

        FileStorage fileStorage = fileStorageService.findByHashId(hashId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + URLEncoder.encode(fileStorage.getName()))
                .contentType(MediaType.parseMediaType(fileStorage.getContentType()))
                .contentLength(fileStorage.getFileSize())
                .body(new FileUrlResource(String.format("%s/%s", uploadFolder, fileStorage.getUploadPath())));
    }

    @DeleteMapping("/delete/{hashId}")
    public ResponseEntity<?> delete(@PathVariable String hashId) {
        fileStorageService.delete(hashId);
        return ResponseEntity.ok("File ochirildi");
    }

}
