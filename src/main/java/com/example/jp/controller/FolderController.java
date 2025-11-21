package com.example.jp.controller;

import com.example.jp.dto.CreateFolderRequest;
import com.example.jp.dto.FolderContentsDTO;
import com.example.jp.dto.FolderDTO;
import com.example.jp.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/folders")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;

    @PostMapping
    public ResponseEntity<FolderDTO> createFolder(@RequestBody CreateFolderRequest request) {
        FolderDTO folder = folderService.createFolder(request.getName(), request.getParentId());
        return ResponseEntity.ok(folder);
    }

    @GetMapping("/{folderId}/contents")
    public ResponseEntity<FolderContentsDTO> getFolderContents(@PathVariable Long folderId) {
        FolderContentsDTO contents = folderService.getFolderContents(folderId);
        return ResponseEntity.ok(contents);
    }

    @GetMapping("/root")
    public ResponseEntity<FolderContentsDTO> getRootContents() {
        FolderContentsDTO contents = folderService.getFolderContents(null);
        return ResponseEntity.ok(contents);
    }

    @DeleteMapping("/{folderId}")
    public ResponseEntity<Void> deleteFolder(@PathVariable Long folderId) {
        folderService.deleteFolder(folderId);
        return ResponseEntity.noContent().build();
    }
}

