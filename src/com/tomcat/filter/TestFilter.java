package com.tomcat.filter;

import com.tomcat.baseservlet.Filter;
import com.tomcat.baseservlet.FilterChain;
import com.tomcat.core.Request;
import com.tomcat.core.Response;

/**
 * 过滤器测试
 * @author 申劭明
 * @date 2019/10/11 14:46
 */
public class TestFilter implements Filter {
    @Override
    public void doFilterInternal(Request request, Response response, FilterChain filterChain) {
        if (request.getUri().contains("filter")){
            response.setResponseContent("My name is BeryAllen,you catch the filter now.");
            return;
        }
        filterChain.doFilter(request,response);
    }
}
