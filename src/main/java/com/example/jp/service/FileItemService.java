package com.example.jp.service;

import com.example.jp.dto.FileItemDTO;
import com.example.jp.model.FileItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class FileItemService {

    private final FileStorageService fileStorageService;

    /**
     * Upload a file to the specified folder path
     */
    public FileItemDTO uploadFile(MultipartFile file, String folderPath) throws IOException {
        // Validate folderPath
        if (folderPath == null) {
            folderPath = ""; // Root folder
        }

        // Store file
        FileItem fileItem = fileStorageService.storeFile(file, folderPath);

        // Convert to DTO
        return convertToDTO(fileItem);
    }

    /**
     * Delete a file by its path
     */
    public void deleteFile(String filePath) throws IOException {
        fileStorageService.deleteFile(filePath);
    }

    /**
     * Get file metadata by path
     */
    public FileItem getFile(String filePath) throws IOException {
        return fileStorageService.getFileMetadata(filePath);
    }

    /**
     * Get file path for download
     */
    public Path getFilePath(String filePath) {
        return fileStorageService.loadFile(filePath);
    }

    private FileItemDTO convertToDTO(FileItem file) {
        FileItemDTO dto = new FileItemDTO();
        dto.setName(file.getName());
        dto.setPath(file.getPath());
        dto.setSize(file.getSize());
        dto.setMimeType(file.getMimeType());
        dto.setFileTypeCategory(file.getFileTypeCategory());
        dto.setFileTypeDescription(file.getFileTypeDescription());
        dto.setExtension(file.getExtension());
        dto.setCreatedAt(file.getCreatedAt());
        dto.setUpdatedAt(file.getUpdatedAt());
        return dto;
    }
}

