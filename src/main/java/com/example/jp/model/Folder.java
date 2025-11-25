package com.example.jp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Folder {
    private String name;
    private String path;
    private List<Folder> subfolders;
    private List<FileItem> files;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
