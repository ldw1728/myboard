package com.project.pboard.repo;

import java.util.List;

import com.project.pboard.model.CommentsEntity;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentsRepository extends JpaRepository<CommentsEntity, Long>{
    List<CommentsEntity> findByBid(Long bid);
    void deleteByBid(Long bid);
    void deleteById(Long id);

}   