package com.tomcat.core;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

/**
 * @Author: 申劭明
 * @Date: 2019/9/16 17:24
 */
public class Request {
    /**
     * 通过Netty读取到的数据
     */
    private ByteBuf byteBuf;
    /**
     * 请求路径,如:/test.txt
     */
    private String uri;

    /**
     * 请求类型,GET或POST等
     */
    private String method;

    public String getMethod() {
        return method;
    }
    public Request(){

    }

    @Override
    public String toString() {
        return "Request{" +
                "uri='" + uri + '\'' +
                ", method='" + method + '\'' +
                '}';
    }

    public Request(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
        parse();
    }

    /**
     * @Description : 获取http请求中的相关参数
     *
     * @author : 申劭明
     * @date : 2019/9/17 10:26
    */
    private void parse() {
        String requestStr = byteBuf.toString(CharsetUtil.UTF_8);

        //获取请求uri
        uri = parseUri(requestStr);
        //获取请求类型
        method = parseMethod(requestStr);
    }

    /**
     * @Description : 获取请求路径,如http://localhost:8080/test.txt,截取/test.txt(URI)
     *
     * @param request 请求头
     * @return : 请求路径
     * @author : 申劭明
     * @date : 2019/9/17 9:33
    */
    private String parseUri(String request) {
        int index1,index2;
        //查看socket获取的请求头是否有值
        index1 = request.indexOf(' ');
        if (index1 != -1){
            index2 = request.indexOf(' ', index1 + 1);
            if (index2 > index1){
                return request.substring(index1 + 1,index2);
            }
        }
        return null;
    }

    /**
     * @Description : 获取请求类型
     *
     * @param request 请求报文
     * @return : GET,POST...
     * @author : 申劭明
     * @date : 2019/9/18 9:51
    */
    private String parseMethod(String request){
        int index = request.indexOf(' ');
        if (index != -1){
            return request.substring(0,index);
        }
        return null;
    }

    public String getUri() {
        return uri;
    }
}
