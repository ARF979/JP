package com.example.jp.service;

import com.example.jp.dto.FileItemDTO;
import com.example.jp.model.FileItem;
import com.example.jp.model.FileUploadTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for handling asynchronous file processing operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AsyncFileProcessingService {

    private final FileStorageService fileStorageService;
    private final Map<String, FileUploadTask> taskStore = new ConcurrentHashMap<>();

    /**
     * Upload file asynchronously
     */
    @Async("fileProcessingExecutor")
    public CompletableFuture<FileItemDTO> uploadFileAsync(MultipartFile file, String folderPath, String taskId) {
        log.info("Starting async upload for file: {} with taskId: {}", file.getOriginalFilename(), taskId);
        
        FileUploadTask task = taskStore.get(taskId);
        if (task == null) {
            task = new FileUploadTask();
            task.setTaskId(taskId);
            task.setFileName(file.getOriginalFilename());
            task.setFolderPath(folderPath);
            task.setStartTime(LocalDateTime.now());
            taskStore.put(taskId, task);
        }

        try {
            // Update status to IN_PROGRESS
            task.setStatus(FileUploadTask.TaskStatus.IN_PROGRESS);
            task.setProgressPercent(10);
            
            // Simulate processing for demonstration
            Thread.sleep(1000); // Remove in production
            task.setProgressPercent(50);
            
            // Store the file
            FileItem fileItem = fileStorageService.storeFile(file, folderPath);
            task.setProgressPercent(90);
            
            // Convert to DTO
            FileItemDTO dto = convertToDTO(fileItem);
            
            // Update task as completed
            task.setStatus(FileUploadTask.TaskStatus.COMPLETED);
            task.setProgressPercent(100);
            task.setFilePath(fileItem.getPath());
            task.setFileSize(fileItem.getSize());
            task.setEndTime(LocalDateTime.now());
            task.setMessage("File uploaded successfully");
            
            log.info("Completed async upload for file: {} with taskId: {}", file.getOriginalFilename(), taskId);
            
            return CompletableFuture.completedFuture(dto);
            
        } catch (Exception e) {
            log.error("Failed to upload file: {} with taskId: {}", file.getOriginalFilename(), taskId, e);
            
            task.setStatus(FileUploadTask.TaskStatus.FAILED);
            task.setMessage("Upload failed: " + e.getMessage());
            task.setEndTime(LocalDateTime.now());
            
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * Process file metadata extraction asynchronously
     */
    @Async("metadataExtractionExecutor")
    public CompletableFuture<Void> extractMetadataAsync(String filePath) {
        log.info("Starting metadata extraction for file: {}", filePath);
        
        try {
            // Simulate metadata extraction
            Thread.sleep(2000);
            
            // In real implementation, you would:
            // - Extract image dimensions
            // - Get video duration
            // - Read audio tags
            // - etc.
            
            log.info("Completed metadata extraction for file: {}", filePath);
            return CompletableFuture.completedFuture(null);
            
        } catch (Exception e) {
            log.error("Failed to extract metadata for file: {}", filePath, e);
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * Create a new upload task
     */
    public String createUploadTask(String fileName, String folderPath) {
        String taskId = UUID.randomUUID().toString();
        
        FileUploadTask task = new FileUploadTask();
        task.setTaskId(taskId);
        task.setFileName(fileName);
        task.setFolderPath(folderPath);
        task.setStatus(FileUploadTask.TaskStatus.PENDING);
        task.setProgressPercent(0);
        task.setStartTime(LocalDateTime.now());
        
        taskStore.put(taskId, task);
        
        log.info("Created upload task with ID: {}", taskId);
        return taskId;
    }

    /**
     * Get task status
     */
    public FileUploadTask getTaskStatus(String taskId) {
        return taskStore.get(taskId);
    }

    /**
     * Remove completed task
     */
    public void removeTask(String taskId) {
        taskStore.remove(taskId);
        log.info("Removed task with ID: {}", taskId);
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
