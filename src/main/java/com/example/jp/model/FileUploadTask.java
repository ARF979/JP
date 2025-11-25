package com.example.jp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents the status of an asynchronous file upload task
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadTask {
    private String taskId;
    private String fileName;
    private String folderPath;
    private TaskStatus status;
    private String message;
    private String filePath;
    private Long fileSize;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer progressPercent;

    public enum TaskStatus {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
        FAILED
    }
}
