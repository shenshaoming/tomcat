package com.tomcat.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @Author: 申劭明
 * @Date: 2019/9/17 17:45
 */
public class RequestHandler extends Thread {

    private Socket socket;

    private InputStream is;

    private OutputStream outputStream;

    public RequestHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.is = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
    }

    @Override
    public void run() {
        //接收请求参数
        Request request = new Request(is);
        request.parse();
        //创建用于返回浏览器的对象
        Response response = new Response(outputStream);
        response.setRequest(request);
        try {
            response.sendStaticResource();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //如果请求的是/shutdown 则关闭服务器
        if (null != request){
            if ("/shutdown".equals(request.getUrl())){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
