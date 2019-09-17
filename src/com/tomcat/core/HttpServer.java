package com.tomcat.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 监听请求,调用request和response对请求作出反应
 * @Author: 申劭明
 * @Date: 2019/9/16 17:21
 */
public class HttpServer {

    public static ServerSocket serverSocket = null;

    /**
     * @Description : 多线程bio监听数据请求
     *
     * @author : 申劭明
     * @date : 2019/9/17 10:29
    */
    public void acceptWait(){
        try {
            if (serverSocket == null){
                synchronized (HttpServer.class){
                    if (serverSocket == null) {
                        serverSocket = new ServerSocket(8080);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(!serverSocket.isClosed()){
            try {
                //单线程,阻塞式监听(bio)
                Socket socket = serverSocket.accept();
                RequestHandler handler = new RequestHandler(socket);
                handler.start();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }
}
