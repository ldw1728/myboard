package com.project.pboard.model;

import java.time.LocalDateTime;

import org.springframework.util.Assert;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentsDTO {
    private Long id;
    private Long bid;
    private String comment;
    private String writer;
    private LocalDateTime createdDate;

    public CommentsEntity toEntity(){
        return CommentsEntity.builder()
                            .id(id)
                            .bid(bid)
                            .comment(comment)
                            .writer(writer)
                            .build();
    }

    @Builder
    public CommentsDTO(Long id, Long bid, String  comment, String writer, LocalDateTime createdDate){

        Assert.notNull(bid, "bi must not be null");
        Assert.notNull(comment, "comment must not be null");
        Assert.notNull(writer, "writer must not be null");
        
        this.id = id;
        this. bid = bid;
        this.comment = comment;
        this.writer = writer;
        this.createdDate = createdDate;
    }


}