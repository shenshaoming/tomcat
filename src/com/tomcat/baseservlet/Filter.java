package com.tomcat.baseservlet;

import com.tomcat.core.Request;
import com.tomcat.core.Response;

/**
 * 过滤器接口
 * @author 申劭明
 * @date 2019/10/11 14:10
 */
public interface Filter {

    /**
     * @Description 调用过滤器
     *
     * @param request 请求对象
     * @param response 响应对象
     * @return void
     * @author 申劭明
     * @date 2019/10/11 14:47
    */
    void doFilterInternal(Request request, Response response,FilterChain filterChain);

}
