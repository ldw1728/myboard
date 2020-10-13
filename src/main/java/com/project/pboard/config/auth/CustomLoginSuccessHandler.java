package com.project.pboard.config.auth;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.project.pboard.model.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler{

    static Logger logger = LoggerFactory.getLogger(CustomLoginSuccessHandler.class);

    public CustomLoginSuccessHandler(String defaultTargetUrl){
        setDefaultTargetUrl(defaultTargetUrl);
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
    Authentication authentication)throws ServletException, IOException{

        //인증 success 시 실행되는 메소드.
        UserInfo user = (UserInfo) request.getSession().getAttribute("user");
        logger.debug(String.valueOf(user));

        user.setClientIP(getClientIp(request));
        request.getSession().setAttribute("user", user);

        logger.debug(user.getMemberDto().getEmail()+" : "+user.getMemberDto().getName()+" : "+user.getClientIP());
        String redirectUrl = "/login/successPage";
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);

    }

    public static String getClientIp(HttpServletRequest request) { //ip정보를 가져오기 위한 메소드
        String ip = request.getHeader("X-Forwarded-For");
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
             ip = request.getHeader("Proxy-Client-IP");
         }
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
             ip = request.getHeader("WL-Proxy-Client-IP");
         }
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
             ip = request.getHeader("HTTP_CLIENT_IP");
         }
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
             ip = request.getHeader("HTTP_X_FORWARDED_FOR");
         }
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
             ip = request.getRemoteAddr();
         }
         return ip;
    }
    
}