package com.example.jp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FolderContentsDTO {
    private List<FolderDTO> folders;
    private List<FileItemDTO> files;
}

