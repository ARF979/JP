package com.example.jp.service;

import com.example.jp.dto.FileItemDTO;
import com.example.jp.entity.FileItem;
import com.example.jp.entity.Folder;
import com.example.jp.repository.FileItemRepository;
import com.example.jp.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileItemService {

    private final FileItemRepository fileItemRepository;
    private final FolderRepository folderRepository;
    private final FileStorageService fileStorageService;
    private static final String HARDCODED_USER_ID = "hardcoded-user-id";

    @Transactional
    public FileItemDTO uploadFile(MultipartFile file, Long folderId) throws IOException {
        // Verify folder exists
        Folder folder = folderRepository.findByIdAndUserId(folderId, HARDCODED_USER_ID)
                .orElseThrow(() -> new RuntimeException("Folder not found"));

        // Store physical file
        String storedFilename = fileStorageService.storeFile(file);

        // Save metadata
        FileItem fileItem = new FileItem();
        fileItem.setName(file.getOriginalFilename());
        fileItem.setStoragePath(storedFilename);
        fileItem.setSize(file.getSize());
        fileItem.setMimeType(file.getContentType());
        fileItem.setFolder(folder);
        fileItem.setUserId(HARDCODED_USER_ID);

        FileItem savedFile = fileItemRepository.save(fileItem);
        return convertToDTO(savedFile);
    }

    @Transactional
    public void deleteFile(Long fileId) throws IOException {
        FileItem fileItem = fileItemRepository.findByIdAndUserId(fileId, HARDCODED_USER_ID)
                .orElseThrow(() -> new RuntimeException("File not found"));

        // Delete physical file
        fileStorageService.deleteFile(fileItem.getStoragePath());

        // Delete metadata
        fileItemRepository.delete(fileItem);
    }

    @Transactional(readOnly = true)
    public FileItem getFile(Long fileId) {
        return fileItemRepository.findByIdAndUserId(fileId, HARDCODED_USER_ID)
                .orElseThrow(() -> new RuntimeException("File not found"));
    }

    private FileItemDTO convertToDTO(FileItem file) {
        FileItemDTO dto = new FileItemDTO();
        dto.setId(file.getId());
        dto.setName(file.getName());
        dto.setSize(file.getSize());
        dto.setMimeType(file.getMimeType());
        dto.setFolderId(file.getFolder().getId());
        dto.setCreatedAt(file.getCreatedAt());
        dto.setUpdatedAt(file.getUpdatedAt());
        return dto;
    }
}

