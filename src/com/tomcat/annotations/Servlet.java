package com.tomcat.annotations;

import java.lang.annotation.*;

/**
 * @Author: 申劭明
 * @Date: 2019/9/18 10:37
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
//编译,运行
public @interface Servlet {
    String value() default "/";
}
