package com.project.pboard.controller;

import javax.persistence.criteria.CriteriaBuilder.Case;

import com.project.pboard.model.MemberDto;
import com.project.pboard.service.MemberService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class MemberController {
   
    private MemberService memberService;
    
    static Logger logger = LoggerFactory.getLogger(MemberController.class);

    @GetMapping("/login")
    public String showLoginPage(Authentication authentication){
       try{
            if(authentication.getPrincipal() != null){
            return "redirect:/board/main";
        }
       }catch(NullPointerException n){
           //logger.debug("authentication is null");
            return "login";
       }
       return "login";
    
    }

    @GetMapping("/login/successPage")
    public String successPage(){
   
        return "redirect:/board/main";
    }

    @GetMapping("/login/failedPage")
    public String failedPage(Model model){
        model.addAttribute("error", true);
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public String execSignUp(MemberDto memberDto, Model model) {
        long r=0;
        if(( r= memberService.joinUser(memberDto)) < 0){
            String temp = "";
            switch(Long.toString(r)){
                case "-1" : 
                temp = "nameerr";
                break;

                case "-2" : 
                temp = "emailerr";
                break;

                case "-3" :
                temp = "empty";
                break;
            }

            model.addAttribute(temp, true);
            return "signup";
        }
        return "redirect:/login";
    }
    
}