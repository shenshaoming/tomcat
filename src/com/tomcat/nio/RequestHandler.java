package com.tomcat.nio;

import com.tomcat.baseservlet.AbstractServlet;

import java.net.Socket;

/**
 * @Author: 申劭明
 * @Date: 2019/9/17 17:45
 */
public class RequestHandler implements Runnable {

    private Socket socket;

    public RequestHandler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            //接收请求参数
            Request request = new Request(socket.getInputStream());
            AbstractServlet abstractServlet = HttpServer.map.get(request.getUri());

            //创建用于返回浏览器的对象
            Response response = new Response(socket.getOutputStream());
            response.setRequest(request);

            if (abstractServlet != null){
                //request和response包名问题
//                abstractServlet.service(request,response);
            }else{
                //找不到对应的Servlet则直接访问文件
                response.sendStaticResource();
            }
            //如果http短连接则关闭socket
            //socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
