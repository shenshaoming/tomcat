package com.tomcat.servlet;

import com.tomcat.annotations.Servlet;
import com.tomcat.baseservlet.AbstractServlet;
import com.tomcat.core.Request;
import com.tomcat.core.Response;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: 申劭明
 * @Date: 2019/9/18 17:02
 */
@Servlet(value = "/login")
public class LoginServlet extends AbstractServlet {
    @Override
    protected void doGet(Request request, Response response) {
        doPost(request,response);
    }

    @Override
    protected void doPost(Request request, Response response) {
        response.setResponseContent("You are requesting the loginServlet");
    }

    @Override
    public void init() throws Exception {

    }

    @Override
    public void destroy() {

    }
}
