package com.tomcat.servlet;

import com.tomcat.annotations.Servlet;
import com.tomcat.baseservlet.AbstractServlet;
import com.tomcat.core.Request;
import com.tomcat.core.Response;

/**
 * @Author: 申劭明
 * @Date: 2019/9/18 10:09
 */
@Servlet("/user")
public class UserServlet extends AbstractServlet {

    @Override
    protected void doGet(Request request, Response response) {
        this.doPost(request,response);
    }

    @Override
    protected void doPost(Request request, Response response) {
        //处理
        //响应
        response.setResponseContent(new StringBuilder("<h1>Your are requesting the UserServlet</h1>"));
    }

    @Override
    public void init() throws Exception {

    }

    @Override
    public void destroy() {

    }
}
