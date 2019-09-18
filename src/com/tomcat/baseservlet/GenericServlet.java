package com.tomcat.baseservlet;

import com.tomcat.core.Request;
import com.tomcat.core.Response;

/**
 * Servlet约束,子类必须拥有这些方法
 * @Author: 申劭明
 * @Date: 2019/9/18 9:43
 */
public interface GenericServlet {
    /**
     * @Description : Servlet初始化时执行的方法
     *
     * @author : 申劭明
     * @date : 2019/9/18 10:10
    */
    void init() throws Exception;

    /**
     * @Description : Servlet销毁时执行的方法
     *
     * @return : void
     * @author : 申劭明
     * @date : 2019/9/18 10:10
    */
    void destroy();

    /**
     * @Description : Servlet分发请求时用到的方法
     *
     * @param request 请求
     * @param response 响应
     * @author : 申劭明
     * @date : 2019/9/18 10:11
    */
    void service(Request request, Response response) throws Exception;

}
