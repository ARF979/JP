package com.example.jp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class FileItem {
    private String name;
    private String path;
    private Long size;
    private String mimeType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    /**
     * Get the file extension
     */
    public String getExtension() {
        if (name != null && name.contains(".")) {
            return name.substring(name.lastIndexOf(".") + 1).toLowerCase();
        }
        return "";
    }
    
    /**
     * Get file type category
     */
    public abstract String getFileTypeCategory();
    
    /**
     * Get human-readable file type description
     */
    public abstract String getFileTypeDescription();
}
