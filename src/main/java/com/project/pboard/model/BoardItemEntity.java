package com.project.pboard.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;
import lombok.Builder;

@Entity
@Getter
@Setter
@Table(name="boarditem")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardItemEntity extends TimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private MemberEntity writer;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    private int count = 0;

    @Builder
    public BoardItemEntity(Long id, MemberEntity writer, String title, String contents, int count){
        this.id = id;
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.count = count;
        
    }

    




}