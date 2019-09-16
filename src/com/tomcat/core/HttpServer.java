package com.tomcat.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author: 申劭明
 * @Date: 2019/9/16 17:21
 */
public class HttpServer {
    private boolean shutdown = false;

    public void acceptWait(){
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8080, 1, InetAddress.getByName("127.0.0.1"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(!shutdown){
            try {
                Socket socket = serverSocket.accept();
                InputStream is = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();
                //接收请求参数
                Request request = new Request(is);
                request.parse();
                //创建用于返回浏览器的对象
                Response response = new Response(outputStream);
                response.setRequest(request);
                response.sendStaticResource();
                //关闭一次请求的socket,因为http去请求就是采用短连接的方式
                socket.close();
                //如果请求的是/shutdown 则关闭容器
                if (null != request){
                    shutdown = request.getUrl().equals("/shutdown");
                }
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    public static void main(String[] args) {
        HttpServer server = new HttpServer();
        server.acceptWait();
    }
}
