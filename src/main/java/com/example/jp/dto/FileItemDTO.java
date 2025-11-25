package com.example.jp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileItemDTO {
    private String name;
    private String path;
    private Long size;
    private String mimeType;
    private String fileTypeCategory;
    private String fileTypeDescription;
    private String extension;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

