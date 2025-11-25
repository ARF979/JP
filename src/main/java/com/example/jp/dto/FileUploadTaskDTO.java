package com.example.jp.dto;

import com.example.jp.model.FileUploadTask;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for file upload task status
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadTaskDTO {
    private String taskId;
    private String fileName;
    private String folderPath;
    private String status;
    private String message;
    private String filePath;
    private Long fileSize;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer progressPercent;
    private Long durationMs;

    public static FileUploadTaskDTO fromTask(FileUploadTask task) {
        FileUploadTaskDTO dto = new FileUploadTaskDTO();
        dto.setTaskId(task.getTaskId());
        dto.setFileName(task.getFileName());
        dto.setFolderPath(task.getFolderPath());
        dto.setStatus(task.getStatus() != null ? task.getStatus().name() : null);
        dto.setMessage(task.getMessage());
        dto.setFilePath(task.getFilePath());
        dto.setFileSize(task.getFileSize());
        dto.setStartTime(task.getStartTime());
        dto.setEndTime(task.getEndTime());
        dto.setProgressPercent(task.getProgressPercent());
        
        if (task.getStartTime() != null && task.getEndTime() != null) {
            dto.setDurationMs(
                java.time.Duration.between(task.getStartTime(), task.getEndTime()).toMillis()
            );
        }
        
        return dto;
    }
}
