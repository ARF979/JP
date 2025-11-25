package com.example.jp.service;

import com.example.jp.dto.FolderContentsDTO;
import com.example.jp.dto.FolderDTO;
import com.example.jp.dto.FileItemDTO;
import com.example.jp.model.FileItem;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FolderService {

    @Value("${app.storage.root}")
    private String storageRoot;

    private final FileStorageService fileStorageService;

    /**
     * Create a new folder
     */
    public FolderDTO createFolder(String name, String parentPath) throws IOException {
        // Validate folder name
        if (name == null || name.trim().isEmpty() || name.contains("/") || name.contains("\\")) {
            throw new IllegalArgumentException("Invalid folder name");
        }

        // Determine the full path
        String folderPath = parentPath != null && !parentPath.isEmpty()
                ? Paths.get(parentPath, name).toString()
                : name;

        Path fullPath = Paths.get(storageRoot, folderPath);

        // Check if folder already exists
        if (Files.exists(fullPath)) {
            throw new IOException("Folder already exists: " + name);
        }

        // Create directory
        Files.createDirectories(fullPath);

        // Get metadata
        BasicFileAttributes attrs = Files.readAttributes(fullPath, BasicFileAttributes.class);

        FolderDTO dto = new FolderDTO();
        dto.setId(null); // No ID in file system based approach
        dto.setName(name);
        dto.setParentId(null);
        dto.setCreatedAt(LocalDateTime.ofInstant(attrs.creationTime().toInstant(), ZoneId.systemDefault()));
        dto.setUpdatedAt(LocalDateTime.ofInstant(attrs.lastModifiedTime().toInstant(), ZoneId.systemDefault()));
        dto.setPath(folderPath);

        return dto;
    }

    /**
     * Get folder contents (subfolders and files)
     */
    public FolderContentsDTO getFolderContents(String folderPath) throws IOException {
        Path fullPath = folderPath == null || folderPath.isEmpty()
                ? Paths.get(storageRoot)
                : Paths.get(storageRoot, folderPath);

        // Create root directory if it doesn't exist
        if (!Files.exists(fullPath)) {
            Files.createDirectories(fullPath);
        }

        List<FolderDTO> folders = new ArrayList<>();
        List<FileItemDTO> files = new ArrayList<>();

        try (Stream<Path> stream = Files.list(fullPath)) {
            stream.forEach(path -> {
                try {
                    BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
                    String relativePath = Paths.get(storageRoot).relativize(path).toString();

                    if (attrs.isDirectory()) {
                        FolderDTO folderDTO = new FolderDTO();
                        folderDTO.setName(path.getFileName().toString());
                        folderDTO.setPath(relativePath);
                        folderDTO.setCreatedAt(LocalDateTime.ofInstant(attrs.creationTime().toInstant(), ZoneId.systemDefault()));
                        folderDTO.setUpdatedAt(LocalDateTime.ofInstant(attrs.lastModifiedTime().toInstant(), ZoneId.systemDefault()));
                        folders.add(folderDTO);
                    } else if (attrs.isRegularFile()) {
                        FileItem fileItem = fileStorageService.getFileMetadata(relativePath);
                        FileItemDTO fileDTO = new FileItemDTO();
                        fileDTO.setName(fileItem.getName());
                        fileDTO.setPath(fileItem.getPath());
                        fileDTO.setSize(fileItem.getSize());
                        fileDTO.setMimeType(fileItem.getMimeType());
                        fileDTO.setFileTypeCategory(fileItem.getFileTypeCategory());
                        fileDTO.setFileTypeDescription(fileItem.getFileTypeDescription());
                        fileDTO.setExtension(fileItem.getExtension());
                        fileDTO.setCreatedAt(fileItem.getCreatedAt());
                        fileDTO.setUpdatedAt(fileItem.getUpdatedAt());
                        files.add(fileDTO);
                    }
                } catch (IOException e) {
                    // Skip files that can't be read
                }
            });
        }

        // Sort by name
        folders.sort(Comparator.comparing(FolderDTO::getName));
        files.sort(Comparator.comparing(FileItemDTO::getName));

        FolderContentsDTO contents = new FolderContentsDTO();
        contents.setFolders(folders);
        contents.setFiles(files);

        return contents;
    }

    /**
     * Delete a folder and all its contents
     */
    public void deleteFolder(String folderPath) throws IOException {
        if (folderPath == null || folderPath.isEmpty()) {
            throw new IllegalArgumentException("Cannot delete root folder");
        }

        Path fullPath = Paths.get(storageRoot, folderPath);

        if (!Files.exists(fullPath)) {
            throw new IOException("Folder not found: " + folderPath);
        }

        if (!Files.isDirectory(fullPath)) {
            throw new IllegalArgumentException("Path is not a directory: " + folderPath);
        }

        // Delete directory and all contents recursively
        try (Stream<Path> walk = Files.walk(fullPath)) {
            walk.sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            // Log error but continue
                        }
                    });
        }
    }
}

