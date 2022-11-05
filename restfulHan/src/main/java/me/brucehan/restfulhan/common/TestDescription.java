package me.brucehan.restfulhan.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE) // 어노테이션을 얼마나 오래 가져갈 것이냐(범위)
public @interface TestDescription {
    String value(); // 항상 입력하도록 한다
}
