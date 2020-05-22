package com.project.pboard.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.util.Assert;

import lombok.*;

@Data
@Entity
@Table(name="comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentsEntity extends TimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="CONTENS_ID")
    private Long id;

    @Column(name="BOARD_ID", nullable=false)
    private Long bid;

    @Column(nullable=false)
    private String comment;

    @Column(nullable=false)
    private String writer;

    @Builder
    public CommentsEntity(Long id, long bid, String comment, String writer){

        Assert.notNull(bid, "bi must not be null");
        Assert.notNull(comment, "comment must not be null");
        Assert.notNull(writer, "writer must not be null");

        this.id = id;
        this.bid = bid;
        this.comment = comment;
        this.writer = writer;
    }
}