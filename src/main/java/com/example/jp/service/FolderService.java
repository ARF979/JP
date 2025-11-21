package com.example.jp.service;

import com.example.jp.dto.FolderContentsDTO;
import com.example.jp.dto.FolderDTO;
import com.example.jp.dto.FileItemDTO;
import com.example.jp.entity.FileItem;
import com.example.jp.entity.Folder;
import com.example.jp.repository.FileItemRepository;
import com.example.jp.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;
    private final FileItemRepository fileItemRepository;
    private static final String HARDCODED_USER_ID = "hardcoded-user-id";

    @Transactional
    public FolderDTO createFolder(String name, Long parentId) {
        Folder folder = new Folder();
        folder.setName(name);
        folder.setUserId(HARDCODED_USER_ID);

        if (parentId != null) {
            Folder parent = folderRepository.findByIdAndUserId(parentId, HARDCODED_USER_ID)
                    .orElseThrow(() -> new RuntimeException("Parent folder not found"));
            folder.setParent(parent);
        }

        Folder savedFolder = folderRepository.save(folder);
        return convertToDTO(savedFolder);
    }

    @Transactional(readOnly = true)
    public FolderContentsDTO getFolderContents(Long folderId) {
        // Verify folder exists and belongs to user
        if (folderId != null) {
            folderRepository.findByIdAndUserId(folderId, HARDCODED_USER_ID)
                    .orElseThrow(() -> new RuntimeException("Folder not found"));
        }

        List<Folder> folders = folderId == null
                ? folderRepository.findByParentIsNullAndUserId(HARDCODED_USER_ID)
                : folderRepository.findByParentIdAndUserId(folderId, HARDCODED_USER_ID);

        List<FileItem> files = folderId == null
                ? List.of()
                : fileItemRepository.findByFolderIdAndUserId(folderId, HARDCODED_USER_ID);

        FolderContentsDTO contents = new FolderContentsDTO();
        contents.setFolders(folders.stream().map(this::convertToDTO).collect(Collectors.toList()));
        contents.setFiles(files.stream().map(this::convertToFileDTO).collect(Collectors.toList()));

        return contents;
    }

    @Transactional
    public void deleteFolder(Long folderId) {
        Folder folder = folderRepository.findByIdAndUserId(folderId, HARDCODED_USER_ID)
                .orElseThrow(() -> new RuntimeException("Folder not found"));

        // Delete all files in this folder
        List<FileItem> files = fileItemRepository.findByFolderIdAndUserId(folderId, HARDCODED_USER_ID);
        for (FileItem file : files) {
            // Physical file deletion will be handled by FileStorageService
            fileItemRepository.delete(file);
        }

        folderRepository.delete(folder);
    }

    private FolderDTO convertToDTO(Folder folder) {
        FolderDTO dto = new FolderDTO();
        dto.setId(folder.getId());
        dto.setName(folder.getName());
        dto.setParentId(folder.getParent() != null ? folder.getParent().getId() : null);
        dto.setCreatedAt(folder.getCreatedAt());
        dto.setUpdatedAt(folder.getUpdatedAt());
        return dto;
    }

    private FileItemDTO convertToFileDTO(FileItem file) {
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

