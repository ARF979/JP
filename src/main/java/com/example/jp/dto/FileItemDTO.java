package com.example.jp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileItemDTO {
    private Long id;
    private String name;
    private Long size;
    private String mimeType;
    private Long folderId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

