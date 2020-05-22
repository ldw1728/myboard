package com.project.pboard.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "files")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;
    
    @Column(nullable = false)
    private String fileType;

    @Lob
    private byte[] data;

    @Column(name = "BOARD_ID", nullable = false)
    private Long bid;

    @Builder
    public FileEntity(Long id, String fileName, String filetype, byte[] data, Long bid){
        this.id = id;
        this.fileName = fileName;
        this.fileType = filetype;
        this.data = data;
        this.bid = bid;
    }

}