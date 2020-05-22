package com.project.pboard.repo;

import java.util.Optional;

import com.project.pboard.model.MemberEntity;

import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<MemberEntity, Long>{
    Optional<MemberEntity> findByEmail(String userEmail);
}