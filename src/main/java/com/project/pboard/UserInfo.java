package com.project.pboard;

import java.util.Collection;
import com.project.pboard.model.MemberDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import lombok.Data;
import lombok.EqualsAndHashCode;



@Data
@EqualsAndHashCode(callSuper = true)
public class UserInfo extends User {

    private static final long serialVersionUID = 1999406089190871126L;
    
    private  MemberDto memberDto;
    private String clientIp;
    

    public UserInfo(String username, String password,
    Collection<? extends GrantedAuthority> authorities, MemberDto memberDto){
        super(username, password, authorities);
        this.memberDto = memberDto;
    }

}