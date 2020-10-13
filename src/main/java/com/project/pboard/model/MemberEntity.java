package com.project.pboard.model;
import javax.persistence.*;

import com.project.pboard.Role;
import org.springframework.util.Assert;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
@Entity
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(length = 30, nullable = false)
    private String name;

    @Column(length = 30, nullable = false)
    private String email;

    @Column(length = 100, nullable = true)
    private String password;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public MemberEntity(Long id, String name, String email, String password,
                        String picture, Role role) throws NullPointerException{
        Assert.notNull(name, "name must not be null");
        Assert.notNull(email, "email must not be null");
        
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.picture = picture;
        this.role = role;
    }

    public MemberEntity update(String name, String picture){
        this.name = name;
        this.picture = picture;
        return this;
    }
    
}