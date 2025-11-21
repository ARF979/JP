package com.example.jp.repository;

import com.example.jp.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

    List<Folder> findByParentIdAndUserId(Long parentId, String userId);

    Optional<Folder> findByIdAndUserId(Long id, String userId);

    List<Folder> findByParentIsNullAndUserId(String userId);

    boolean existsByIdAndUserId(Long id, String userId);
}

