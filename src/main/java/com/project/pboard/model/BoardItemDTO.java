package com.project.pboard.model;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class BoardItemDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class BoardDetailDTO{
        private Long id;
        private MemberDto writer;
        private String title;
        private String contents;
        private LocalDateTime createdDate;
        private LocalDateTime modifiedDate;
        private int count;

        public BoardItemEntity toEntity(){
            return BoardItemEntity.builder()
            .id(id)
            .writer(writer.toEntity())
            .title(title)
            .contents(contents)
            .count(count)
            .build();
        }

        @Builder
        public BoardDetailDTO(Long id,MemberDto writer, String title, String contents, LocalDateTime createdDate, LocalDateTime modifiedDate, int count){
            this.writer = writer;
            this.id = id;
            this.title = title;
            this.contents = contents;
            this.createdDate = createdDate;
            this.modifiedDate = modifiedDate;
            this.count = count;
        }
    }
}