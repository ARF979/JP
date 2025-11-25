package com.example.jp.controller;

import com.example.jp.dto.FileItemDTO;
import com.example.jp.model.FileUploadTask;
import com.example.jp.service.AsyncFileProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Controller for asynchronous file operations
 */
@RestController
@RequestMapping("/api/async/files")
@RequiredArgsConstructor
public class AsyncFileController {

    private final AsyncFileProcessingService asyncFileProcessingService;

    /**
     * Upload file asynchronously - returns immediately with task ID
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFileAsync(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folderPath", required = false, defaultValue = "") String folderPath) {
        
        // Create task
        String taskId = asyncFileProcessingService.createUploadTask(
            file.getOriginalFilename(),
            folderPath
        );
        
        // Start async upload
        asyncFileProcessingService.uploadFileAsync(file, folderPath, taskId);
        
        Map<String, String> response = new HashMap<>();
        response.put("taskId", taskId);
        response.put("message", "File upload started");
        response.put("statusUrl", "/api/async/files/status/" + taskId);
        
        return ResponseEntity.accepted().body(response);
    }

    /**
     * Upload file asynchronously and wait for completion
     */
    @PostMapping("/upload-sync")
    public CompletableFuture<ResponseEntity<FileItemDTO>> uploadFileAsyncWithResult(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folderPath", required = false, defaultValue = "") String folderPath) {
        
        String taskId = asyncFileProcessingService.createUploadTask(
            file.getOriginalFilename(),
            folderPath
        );
        
        return asyncFileProcessingService.uploadFileAsync(file, folderPath, taskId)
            .thenApply(ResponseEntity::ok)
            .exceptionally(ex -> ResponseEntity.internalServerError().build());
    }

    /**
     * Get upload task status
     */
    @GetMapping("/status/{taskId}")
    public ResponseEntity<FileUploadTask> getTaskStatus(@PathVariable String taskId) {
        FileUploadTask task = asyncFileProcessingService.getTaskStatus(taskId);
        
        if (task == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(task);
    }

    /**
     * Delete completed task
     */
    @DeleteMapping("/status/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable String taskId) {
        asyncFileProcessingService.removeTask(taskId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Batch upload files asynchronously
     */
    @PostMapping("/batch-upload")
    public ResponseEntity<Map<String, Object>> batchUploadFiles(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam(value = "folderPath", required = false, defaultValue = "") String folderPath) {
        
        Map<String, String> taskIds = new HashMap<>();
        
        for (MultipartFile file : files) {
            String taskId = asyncFileProcessingService.createUploadTask(
                file.getOriginalFilename(),
                folderPath
            );
            asyncFileProcessingService.uploadFileAsync(file, folderPath, taskId);
            taskIds.put(file.getOriginalFilename(), taskId);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("totalFiles", files.length);
        response.put("taskIds", taskIds);
        response.put("message", "Batch upload started");
        
        return ResponseEntity.accepted().body(response);
    }
}
