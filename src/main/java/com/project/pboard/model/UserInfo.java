package com.project.pboard.model;

import lombok.Builder;
import lombok.Data;

@Data
public class UserInfo {
    private MemberDto memberDto;
    private String clientIP;

    public UserInfo(MemberDto memberDto){
        this.memberDto = memberDto;
    }
}
