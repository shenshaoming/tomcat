package com.tomcat.core;

import com.tomcat.annotations.Servlet;
import com.tomcat.baseservlet.AbstractServlet;
import com.tomcat.exceptions.RequestMappingException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Netty版本的tomcat
 * 监听请求,调用request和response对请求作出反应
 * @author 申劭明
 * @date 2019/9/16 17:21
 * @version 5.3
 */
public class HttpServer {
    /**
     * 监听端口
     */
    private static int port = 8080;

    /**
     * Key值为Servlet的别名(uri),value为该Servlet对象
     * default权限
     */
    static HashMap<String, AbstractServlet> map;

    static {
        //包名,可以通过application.properties设置
        getServlets("com.tomcat.servlet");
    }
    /**
     * @Description : nio监听数据请求
     * @author : 申劭明
     * @date : 2019/9/17 10:29
     */
    public void acceptWait() {

        //监听请求
        EventLoopGroup listenGroup = new NioEventLoopGroup();
        //请求处理
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();

        //绑定监听请求和处理请求的group
        bootstrap.group(listenGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new RequestHandler());
                    }
                });
        ChannelFuture future = null;
        try {
            future = bootstrap.bind(port).sync();
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            listenGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
    /**
     * @param packageName 包名,如com.tomcat.servlet
     * @Description : 扫描packageName包下的所有带有@Servlet注解的类文件
     * @author : 申劭明
     * @date : 2019/9/18 10:36
     */
    private static void getServlets(String packageName) {

        //class类的集合
        Set<Class<?>> classes = new LinkedHashSet<>();

        try {
            //com.tomcat.servlet,com/tomcat/servelet
            String packageDirName = packageName.replace(".", "/");
            Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(packageDirName);

            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                System.out.println("****************" + url);
                System.out.println();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    findAndAddClassesInPackageByFile(packageName, filePath,true,classes);
                }else if("jar".equals(protocol)){
                    //扫描JAR包
                }
            }
            //遍历class集合
            if (map == null){
                map = new HashMap<>(classes.size());
            }
            for (Class<?> aClass : classes) {
                //如果该class有Servlet注解
                if (aClass.isAnnotationPresent(Servlet.class)){
                    try {
                        String value = aClass.getAnnotation(Servlet.class).value();
                        //如果已经包含有该key值,则抛出异常
                        if (map.containsKey(value)){
                            //当前正在扫描的Servlet
                            String now = aClass.getName();
                            //已经存在的Servlet
                            String old = map.get(value).getClass().getName();
                            throw new RequestMappingException(now,old);

                        }else{
                            Class<?> superclass = aClass.getSuperclass();
                            if (AbstractServlet.class != superclass){
                                System.err.println("带有Servlet注解的类'" + aClass.getName() + "'没有继承自AbstractServlet");
                                continue;
                            }
                        }
                        //添加至map集合中
                        map.put(value, (AbstractServlet) aClass.newInstance());
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (RequestMappingException e) {
                        e.printStackTrace();
                        System.exit(-1);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description : 对于file类型获取该类型的所有class
     *
     * @param packageName 包名,com.tomcat.servlet
     * @param packagePath 包路径,com/tomcat/servlet
     * @param recursive 是否循环遍历子包内的文件
     * @param classes class集合
     * @author : 申劭明
     * @date : 2019/9/18 16:55
    */
    public static void findAndAddClassesInPackageByFile(String packageName,
                                                        String packagePath, final boolean recursive, Set<Class<?>> classes) {
        // 获取此包的目录 建立一个File
        File dir = new File(packagePath);
        // 如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        // 如果存在 就获取包下的所有文件 包括目录
        // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
        File[] dirfiles = dir.listFiles(file -> (recursive && file.isDirectory())
                || (file.getName().endsWith(".class")));
        // 循环所有文件
        for (File file : dirfiles) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "."
                                + file.getName(), file.getAbsolutePath(), recursive,
                        classes);
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0,
                        file.getName().length() - 6);
                try {
                    // 添加到集合中去
                    // classes.add(Class.forName(packageName + '.' +
                    // className));
                    // 经过回复同学的提醒，这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
                    classes.add(Thread.currentThread().getContextClassLoader()
                            .loadClass(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    // log.error("添加用户自定义视图类错误 找不到此类的.class文件");
                    e.printStackTrace();
                }
            }
        }
    }
}
