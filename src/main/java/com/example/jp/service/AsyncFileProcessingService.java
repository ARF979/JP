package com.example.jp.service;

import com.example.jp.dto.FileItemDTO;
import com.example.jp.model.FileItem;
import com.example.jp.model.FileUploadTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

/**
 * Service for handling asynchronous file processing operations
 */
@Service
@Slf4j
public class AsyncFileProcessingService {

    private final FileStorageService fileStorageService;
    private final Executor fileProcessingExecutor;
    private final Map<String, FileUploadTask> taskStore = new ConcurrentHashMap<>();

    @Autowired
    public AsyncFileProcessingService(
            FileStorageService fileStorageService,
            @Qualifier("fileProcessingExecutor") Executor fileProcessingExecutor) {
        this.fileStorageService = fileStorageService;
        this.fileProcessingExecutor = fileProcessingExecutor;
    }

    /**
     * Upload file asynchronously
     * Note: We buffer file bytes immediately because the temp file may be deleted
     * after the HTTP request completes but before the async thread processes it.
     */
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

        // Buffer file data immediately before async handoff to avoid temp file issues
        final byte[] fileBytes;
        final String originalFilename = file.getOriginalFilename();
        final String contentType = file.getContentType();
        final long fileSize = file.getSize();
        
        try {
            fileBytes = file.getBytes();
            task.setStatus(FileUploadTask.TaskStatus.IN_PROGRESS);
            task.setProgressPercent(10);
        } catch (Exception e) {
            log.error("Failed to read file bytes: {}", originalFilename, e);
            task.setStatus(FileUploadTask.TaskStatus.FAILED);
            task.setMessage("Failed to read file: " + e.getMessage());
            task.setEndTime(LocalDateTime.now());
            return CompletableFuture.failedFuture(e);
        }

        final FileUploadTask finalTask = task;
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Simulate processing for demonstration
                Thread.sleep(1000);
                finalTask.setProgressPercent(50);
                
                // Create a simple MultipartFile wrapper from buffered bytes
                MultipartFile bufferedFile = new ByteArrayMultipartFile(
                    fileBytes, originalFilename, contentType, fileSize
                );
                
                // Store the file
                FileItem fileItem = fileStorageService.storeFile(bufferedFile, folderPath);
                finalTask.setProgressPercent(90);
                
                return fileItem;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, fileProcessingExecutor).thenApply(fileItem -> {
            // Convert to DTO
            FileItemDTO dto = convertToDTO(fileItem);
            
            // Update task as completed
            finalTask.setStatus(FileUploadTask.TaskStatus.COMPLETED);
            finalTask.setProgressPercent(100);
            finalTask.setFilePath(fileItem.getPath());
            finalTask.setFileSize(fileItem.getSize());
            finalTask.setEndTime(LocalDateTime.now());
            finalTask.setMessage("File uploaded successfully");
            
            log.info("Completed async upload for file: {} with taskId: {}", originalFilename, taskId);
            
            return dto;
        }).exceptionally(ex -> {
            log.error("Failed to upload file: {} with taskId: {}", originalFilename, taskId, ex);
            finalTask.setStatus(FileUploadTask.TaskStatus.FAILED);
            finalTask.setMessage("Upload failed: " + ex.getMessage());
            finalTask.setEndTime(LocalDateTime.now());
            return null;
        });
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

    /**
     * Simple MultipartFile implementation backed by a byte array
     */
    private static class ByteArrayMultipartFile implements MultipartFile {
        private final byte[] content;
        private final String name;
        private final String originalFilename;
        private final String contentType;
        private final long size;

        public ByteArrayMultipartFile(byte[] content, String originalFilename, String contentType, long size) {
            this.content = content;
            this.name = "file";
            this.originalFilename = originalFilename;
            this.contentType = contentType;
            this.size = size;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getOriginalFilename() {
            return originalFilename;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public boolean isEmpty() {
            return content == null || content.length == 0;
        }

        @Override
        public long getSize() {
            return size;
        }

        @Override
        public byte[] getBytes() throws IOException {
            return content;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(content);
        }

        @Override
        public void transferTo(File dest) throws IOException, IllegalStateException {
            java.nio.file.Files.write(dest.toPath(), content);
        }
    }
}
