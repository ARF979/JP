package com.example.jp.service;

import com.example.jp.model.FileItem;
import com.example.jp.model.FileItemFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class FileStorageService {

    @Value("${app.storage.root}")
    private String storageRoot;

    /**
     * Store a file in the specified folder path with its original name
     */
    public FileItem storeFile(MultipartFile file, String folderPath) throws IOException {
        Path targetDir = Paths.get(storageRoot, folderPath);
        
        // Create directory if it doesn't exist
        if (!Files.exists(targetDir)) {
            Files.createDirectories(targetDir);
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IOException("Invalid filename");
        }

        // Store file with original name
        Path targetFile = targetDir.resolve(originalFilename);
        
        // If file exists, add number suffix
        if (Files.exists(targetFile)) {
            String nameWithoutExt = originalFilename.contains(".")
                    ? originalFilename.substring(0, originalFilename.lastIndexOf("."))
                    : originalFilename;
            String extension = originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : "";
            
            int counter = 1;
            do {
                originalFilename = nameWithoutExt + " (" + counter + ")" + extension;
                targetFile = targetDir.resolve(originalFilename);
                counter++;
            } while (Files.exists(targetFile));
        }

        Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);

        // Create FileItem with metadata using factory pattern
        BasicFileAttributes attrs = Files.readAttributes(targetFile, BasicFileAttributes.class);
        
        // Use factory to create appropriate file type
        FileItem fileItem = FileItemFactory.createFileItem(
            originalFilename,
            Paths.get(folderPath, originalFilename).toString(),
            file.getSize(),
            file.getContentType()
        );
        
        fileItem.setCreatedAt(LocalDateTime.ofInstant(attrs.creationTime().toInstant(), ZoneId.systemDefault()));
        fileItem.setUpdatedAt(LocalDateTime.ofInstant(attrs.lastModifiedTime().toInstant(), ZoneId.systemDefault()));

        return fileItem;
    }

    /**
     * Delete a file at the given path
     */
    public void deleteFile(String filePath) throws IOException {
        Path fullPath = Paths.get(storageRoot, filePath);
        Files.deleteIfExists(fullPath);
    }

    /**
     * Load file path for download
     */
    public Path loadFile(String filePath) {
        return Paths.get(storageRoot, filePath);
    }

    /**
     * Get file metadata
     */
    public FileItem getFileMetadata(String filePath) throws IOException {
        Path fullPath = Paths.get(storageRoot, filePath);
        
        if (!Files.exists(fullPath)) {
            throw new IOException("File not found: " + filePath);
        }

        BasicFileAttributes attrs = Files.readAttributes(fullPath, BasicFileAttributes.class);
        String mimeType = Files.probeContentType(fullPath);
        
        // Use factory to create appropriate file type
        FileItem fileItem = FileItemFactory.createFileItem(
            fullPath.getFileName().toString(),
            filePath,
            attrs.size(),
            mimeType != null ? mimeType : "application/octet-stream"
        );
        
        fileItem.setCreatedAt(LocalDateTime.ofInstant(attrs.creationTime().toInstant(), ZoneId.systemDefault()));
        fileItem.setUpdatedAt(LocalDateTime.ofInstant(attrs.lastModifiedTime().toInstant(), ZoneId.systemDefault()));

        return fileItem;
    }
}

