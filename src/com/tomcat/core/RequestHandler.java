package com.tomcat.core;

import com.tomcat.baseservlet.AbstractServlet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Author: 申劭明
 * @Date: 2019/9/17 17:45
 */
public class RequestHandler extends SimpleChannelInboundHandler {

    public RequestHandler(){
    }
    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, Object o) {
        //接收数据
        Request request = new Request((ByteBuf) o);
        //写入数据
        Response response = new Response(channelHandlerContext);
        //将请求对象放入响应对象中
        response.setRequest(request);

        AbstractServlet abstractServlet = HttpServer.map.get(request.getUri());
        try{
            if (abstractServlet != null){
                abstractServlet.service(request,response);
            }else{
                //找不到对应的Servlet则直接访问文件
                response.sendStaticResource();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channelHandlerContext.close();
        }
    }
}
