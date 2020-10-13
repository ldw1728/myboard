package com.project.pboard.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) //해당 어노테이션은 메소드의 파라미터로 선언된 객체에서만 사용
@Retention(RetentionPolicy.RUNTIME)
//컴파일 이후에도 JVM에 의해 참조가 가능.
public @interface LoginUser {
}
