package com.tomcat.baseservlet;

import com.tomcat.core.Request;
import com.tomcat.core.Response;

/**
 * GenericServlet的抽象实现
 * @Author: 申劭明
 * @Date: 2019/9/18 10:03
 */
public abstract class AbstractServlet implements GenericServlet{

    private static final String GET_METHOD = "GET";
    
    private static final String POST_METHOD = "POST";

    /**
     * @Description : 在抽象类中实现请求的分发
     *
     * @param request 请求
     * @param response 响应
     * @author : 申劭明
     * @date : 2019/9/18 10:13
    */
    @Override
    public void service(Request request, Response response) throws Exception {
        if (GET_METHOD.equalsIgnoreCase(request.getMethod())){
            this.doGet(request,response);
        }else if (POST_METHOD.equalsIgnoreCase(request.getMethod())){
            this.doPost(request,response);
        }
    }

    /**
     * @Description : 请求类型为GET时执行的方法
     *
     * @param request 请求
     * @param response 响应
     * @author : 申劭明
     * @date : 2019/9/18 10:12
    */
    protected abstract void doGet(Request request, Response response);

    /**
     * @Description : 请求类型为POST时执行的方法
     *
     * @param request 请求
     * @param response 响应
     * @author : 申劭明
     * @date : 2019/9/18 10:12
    */
    protected abstract void doPost(Request request, Response response);


}
