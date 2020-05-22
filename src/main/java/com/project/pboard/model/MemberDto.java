package com.project.pboard.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberDto {
    private Long id;
    private String name;
    private String email;
    private String password;

    public MemberEntity toEntity(){
        return MemberEntity.builder()
        .id(id)
        .name(name)
        .email(email)
        .password(password)
        .build();
    }

    @Builder
    public MemberDto(Long id, String name, String email,String password){
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }
}