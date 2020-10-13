package com.project.pboard.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import com.project.pboard.Role;
import com.project.pboard.config.auth.OAuthAttributes;
import com.project.pboard.model.MemberDto;
import com.project.pboard.model.MemberEntity;
import com.project.pboard.model.UserInfo;
import com.project.pboard.repo.MemberRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.*;


@Service
@AllArgsConstructor // 모든 필드값을 파라미터로 받는 생성자
public class MemberService implements UserDetailsService, OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    
    private MemberRepository memberRepository;

    private HttpSession session;



    @Transactional 
    public Long joinUser(MemberDto memberDto){ //회원가입을 처리하는 메소드

        if(memberDto.getName().equals("") || 
            memberDto.getEmail().equals("") ||
            memberDto.getPassword().equals(""))
            return (long)-3;

        if(memberRepository.findByName(memberDto.getName()).isPresent()){
            return (long) -1;
        }
        if(memberRepository.findByEmail(memberDto.getEmail()).isPresent()){
            return (long)-2;
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); //패스워드 암호화를 위한 객체
        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword())); //암호화
        memberDto.setRole(Role.MEMBER);
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
        session.setAttribute("user", new UserInfo(mm.map(userEntity, MemberDto.class)));
               
        return new User(userEntity.getEmail(),userEntity.getPassword(),authorities);
      
    }

    @Override //소셜로그인이 성공하고 나서 받아온 사용자의 정보를 가입, 정보수정, 세션저장의 역할을 수행.
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(oAuth2UserRequest);

        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = oAuth2UserRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName
        , oAuth2User.getAttributes());
        //oAuth2User.getAttributes()는 사용자의 name,email등의 정보를 담고있다.
        //userNameAttributeName는 키와 같은 의미를 가진다.
        //registrationId 현재 사용중인 서비스를 의미한다.
        ModelMapper mm = new ModelMapper();
        MemberEntity member = saveOrUpdate(attributes);
        session.setAttribute("user", new UserInfo(mm.map(member, MemberDto.class)));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(Role.MEMBER.getValue())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    private MemberEntity saveOrUpdate(OAuthAttributes attributes){
        MemberEntity member = memberRepository.findByEmail(attributes.getEmail())
                .map(m -> m.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return memberRepository.save(member);
    }

}