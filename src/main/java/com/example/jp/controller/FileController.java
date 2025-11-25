package com.example.jp.controller;

import com.example.jp.dto.FileItemDTO;
import com.example.jp.model.FileItem;
import com.example.jp.service.FileItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileItemService fileItemService;

    @PostMapping("/upload")
    public ResponseEntity<FileItemDTO> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folderPath", required = false, defaultValue = "") String folderPath) throws IOException {
        FileItemDTO fileItem = fileItemService.uploadFile(file, folderPath);
        return ResponseEntity.ok(fileItem);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam("path") String filePath) throws IOException {
        FileItem fileItem = fileItemService.getFile(filePath);
        Path path = fileItemService.getFilePath(filePath);
        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists()) {
            throw new RuntimeException("File not found");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileItem.getMimeType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileItem.getName() + "\"")
                .body(resource);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteFile(@RequestParam("path") String filePath) throws IOException {
        fileItemService.deleteFile(filePath);
        return ResponseEntity.noContent().build();
    }
}

