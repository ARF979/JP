package com.example.jp.controller;

import com.example.jp.dto.FileItemDTO;
import com.example.jp.entity.FileItem;
import com.example.jp.service.FileItemService;
import com.example.jp.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileItemService fileItemService;
    private final FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<FileItemDTO> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("folderId") Long folderId) throws IOException {
        FileItemDTO fileItem = fileItemService.uploadFile(file, folderId);
        return ResponseEntity.ok(fileItem);
    }

    @GetMapping("/{fileId}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) throws MalformedURLException {
        FileItem fileItem = fileItemService.getFile(fileId);
        Path filePath = fileStorageService.loadFile(fileItem.getStoragePath());
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            throw new RuntimeException("File not found");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileItem.getMimeType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileItem.getName() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long fileId) throws IOException {
        fileItemService.deleteFile(fileId);
        return ResponseEntity.noContent().build();
    }
}

