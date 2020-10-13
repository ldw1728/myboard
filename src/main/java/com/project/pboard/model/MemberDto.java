package com.project.pboard.model;

import com.project.pboard.Role;
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
    private Role role;
    private String picture;

    public MemberEntity toEntity() throws NullPointerException{
        return MemberEntity.builder()
        .id(id)
        .name(name)
        .email(email)
        .password(password)
        .build();
    }

    @Builder
    public MemberDto(Long id, String name, String email,String password, String picture) throws NullPointerException{
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "MemberDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", picture='" + picture + '\'' +
                '}';
    }
}