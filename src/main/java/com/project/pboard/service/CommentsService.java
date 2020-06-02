package com.project.pboard.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.project.pboard.model.CommentsDTO;
import com.project.pboard.repo.CommentsRepository;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CommentsService {
    private CommentsRepository commentsRepository;

    public Long saveComment(CommentsDTO commentsDTO){
        return commentsRepository.save(commentsDTO.toEntity()).getId();
    }

    public List<CommentsDTO> getComments(Long bid){
        List<CommentsDTO> comments = new ArrayList<>();

        ModelMapper mm = new ModelMapper();
        comments = mm.map(commentsRepository.findByBid(bid), new TypeToken<List<CommentsDTO>>(){}.getType());

        return comments;

    }
    @Transactional
    public void deleteComments(Long bid){
        commentsRepository.deleteByBid(bid);
    }

    @Transactional
    public void deleteCommnet(Long cid){
        commentsRepository.deleteById(cid);
    }
}