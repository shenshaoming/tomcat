package com.tomcat.exceptions;

/**
 * 映射URL冲突异常
 * 当Servlet注解中的value值相同时抛出该异常
 * @Author: 申劭明
 * @Date: 2019/9/26 16:35
 */
public class RequestMappingException extends Exception {

    public RequestMappingException(){
        super("RequestMapping Conflict");
    }

    public RequestMappingException(String now, String old) {
        super("RequestMapping Conflict,that may be happened between '" + now + "' and '" + old + "'");
    }
}
