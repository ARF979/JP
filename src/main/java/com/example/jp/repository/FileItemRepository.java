package com.example.jp.repository;

import com.example.jp.entity.FileItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileItemRepository extends JpaRepository<FileItem, Long> {

    List<FileItem> findByFolderIdAndUserId(Long folderId, String userId);

    Optional<FileItem> findByIdAndUserId(Long id, String userId);

    boolean existsByIdAndUserId(Long id, String userId);
}

