package com.example.jp.controller;

import com.example.jp.dto.CreateFolderRequest;
import com.example.jp.dto.FolderContentsDTO;
import com.example.jp.dto.FolderDTO;
import com.example.jp.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/folders")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;

    @PostMapping
    public ResponseEntity<FolderDTO> createFolder(@RequestBody CreateFolderRequest request) throws IOException {
        FolderDTO folder = folderService.createFolder(request.getName(), request.getParentPath());
        return ResponseEntity.ok(folder);
    }

    @GetMapping("/contents")
    public ResponseEntity<FolderContentsDTO> getFolderContents(
            @RequestParam(value = "path", required = false, defaultValue = "") String folderPath) throws IOException {
        FolderContentsDTO contents = folderService.getFolderContents(folderPath);
        return ResponseEntity.ok(contents);
    }

    @GetMapping("/root")
    public ResponseEntity<FolderContentsDTO> getRootContents() throws IOException {
        FolderContentsDTO contents = folderService.getFolderContents(null);
        return ResponseEntity.ok(contents);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteFolder(@RequestParam("path") String folderPath) throws IOException {
        folderService.deleteFolder(folderPath);
        return ResponseEntity.noContent().build();
    }
}

