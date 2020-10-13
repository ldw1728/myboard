package com.project.pboard.config.annotation;

import com.project.pboard.config.annotation.LoginUser;
import com.project.pboard.model.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final HttpSession httpSession;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        //컨트롤러 메서드의 특정 파라미터를 지원하는지 판단.

        boolean isLoginUserAnnotation = methodParameter.getParameterAnnotation(LoginUser.class) != null;
        //지정된 어노테이션의 메소드파라미터가 널이아닌지

        boolean isUserClass = UserInfo.class.equals(methodParameter.getParameterType());
        //UserInfo클래스가 맞는지

        return isLoginUserAnnotation && isUserClass;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        //파라미터에 전달할 객체를 생성합니다.
        return httpSession.getAttribute("user");
    }
}
