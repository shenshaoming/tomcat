package com.tomcat.core;

import com.tomcat.baseservlet.AbstractServlet;
import com.tomcat.servlet.UserServlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * 监听请求,调用request和response对请求作出反应
 * @Author: 申劭明
 * @Date: 2019/9/16 17:21
 * @version: 4.0
 */
public class HttpServer {

    /**
     * 监听端口
     */
    public static int port = 8080;

    /**
     * 关闭服务器的请求URI
     */
    public static final String CLOSE_URI = "/shutdown";

    /**
     * version:4.1将会采用SpringMvc的形式扫描获得所有的servlet对象
     * Key值为Servlet的别名(uri),value为该Servlet对象
     */
    public static final HashMap<String, AbstractServlet> map ;

    static{
        map = new HashMap<>(8);
        map.put("/user",new UserServlet());
    }

    /**
     * 单例,因为是通过主函数启动,不涉及多进程启动的问题,所以不需要做多线程方面的考虑
     */
    public static ServerSocket serverSocket = null;

    /**
     * @Description : 多线程bio监听数据请求
     *
     * @author : 申劭明
     * @date : 2019/9/17 10:29
    */
    public void acceptWait(){
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //扫描包,读取包下的所有Servlet

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

    /**
     * @Description : 扫描packageName包下的所有带有@Servlet注解的类文件
     *
     * @param packageName 包名,如com.tomcat.servlet
     * @return : void
     * @author : 申劭明
     * @date : 2019/9/18 10:36
    */
//    private void getServlets(String packageName) {
//        try {
//            Thread.currentThread().getContextClassLoader().getResources(packageName);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
