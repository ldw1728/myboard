package com.project.pboard.repo;

import java.util.List;

import com.project.pboard.model.FileEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
    List<FileEntity> findByBid(Long bid);
}