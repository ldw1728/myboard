package com.project.pboard.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import com.project.pboard.Role;
import com.project.pboard.UserInfo;
import com.project.pboard.model.MemberDto;
import com.project.pboard.model.MemberEntity;
import com.project.pboard.repo.MemberRepository;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.*;

@Service
@AllArgsConstructor // 모든 필드값을 파라미터로 받는 생성자
public class MemberService implements UserDetailsService {
    
    //public static UserInfo userInfo;
    private MemberRepository memberRepository;


    @Transactional 
    public Long joinUser(MemberDto memberDto){ //회원가입을 처리하는 메소드
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); //패스워드 암호화를 위한 객체
        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword())); //암호화

        return memberRepository.save(memberDto.toEntity()).getId(); //저장
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException { //유저메일을 이용하여 데이터 조회
        
        Optional<MemberEntity> userEntityWrapper = memberRepository.findByEmail(userEmail);
        
        MemberEntity userEntity = userEntityWrapper.get();

        List<GrantedAuthority> authorities = new ArrayList<>();

        if(("admin@naver.com").equals(userEmail)){
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        }
        else{
            authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
        }
        ModelMapper mm = new ModelMapper();
        /*userInfo = UserInfo.builder()
                            .username(userEntity.getEmail())
                            .password(userEntity.getPassword())
                            .authorities(authorities)
                            .memberDto(mm.map(userEntity, MemberDto.class))
                            .build();*/  

                             
        return new UserInfo(userEntity.getEmail(),userEntity.getPassword(),authorities,mm.map(userEntity, MemberDto.class));      
      
    } 
}