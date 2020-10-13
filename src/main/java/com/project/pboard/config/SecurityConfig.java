package com.project.pboard.config;

import com.project.pboard.config.auth.CustomLoginSuccessHandler;
import com.project.pboard.service.MemberService;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    //secutiry 관련 config 구현
    private MemberService membereService;

    //UserDetalService를 implement한 memberService를 참조

    @Bean 
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean 
    public AuthenticationSuccessHandler successHandler(){
        return new CustomLoginSuccessHandler("/");
    }

    @Override
    public void configure(WebSecurity web) throws Exception
    {
        // static 디렉터리의 하위 파일 목록은 인증 무시 ( = 항상통과 )
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/lib/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/user/**").hasRole("MEMBER")
            .antMatchers("/board/**").hasRole("MEMBER")
            .antMatchers("/signup").permitAll()
            .and()
            .formLogin()
            .successHandler(successHandler())
            .loginPage("/mylogin")
            .failureUrl("/login/failedPage")
            .permitAll()
            .and()
            .logout()
            .logoutUrl("/mylogin")
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .invalidateHttpSession(true)
            .and()
                .sessionManagement().invalidSessionUrl("/mylogin")
                .and()
                .csrf().disable()
                .oauth2Login().userInfoEndpoint().userService(membereService);

    }



    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(membereService).passwordEncoder(passwordEncoder());
        //password를 인코딩
    }
}