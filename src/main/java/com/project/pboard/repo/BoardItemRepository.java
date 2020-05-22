package com.project.pboard.repo;

import java.util.List;

import com.project.pboard.model.BoardItemEntity;


import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardItemRepository extends JpaRepository<BoardItemEntity, Long>{
    List<BoardItemEntity> findByTitleContaining(String keyWord);
}