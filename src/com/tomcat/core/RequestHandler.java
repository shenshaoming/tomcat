package com.tomcat.core;

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

            //如果请求的是/shutdown 则关闭服务器
            if (HttpServer.CLOSE_URI.equals(request.getUri())){
                HttpServer.serverSocket.close();
                return;
            }
            //创建用于返回浏览器的对象
            Response response = new Response(socket.getOutputStream());
            response.setRequest(request);

            if (abstractServlet != null){
                abstractServlet.service(request,response);
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
