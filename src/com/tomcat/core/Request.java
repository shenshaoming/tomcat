package com.tomcat.core;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: 申劭明
 * @Date: 2019/9/16 17:24
 */
public class Request {

    private InputStream is;
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

    public Request(InputStream inputStream){
        this.is = inputStream;
        //读取报文
        parse();
    }

    /**
     * @Description : 获取http请求中的相关参数
     *
     * @author : 申劭明
     * @date : 2019/9/17 10:26
    */
    public void parse() {
        /**
         * 一个包没有固定长度，以太网限制在46－1500字节，
         * 1500就是以太网的MTU，超过这个量，TCP会为IP数据报设置偏移量进行分片传输，
         * 现在一般可允许应用层设置8k（NTFS系统）的缓冲区，8k的数据由底层分片，
         * 而应用层看来只是一次发送。
         */
        //创建一个容量为2048的StringBuffer对象
        StringBuffer request = new StringBuffer(2048);
        //记录字节数量
        int i ;
        byte[] buffer = new byte[Response.BUFFER_SIZE];
        try {
            //从输入流中读取数据到buffer中,i表示读到了多少字节(多少个byte)
            i = is.read(buffer);

        } catch (Exception e) {
            e.printStackTrace();
            i = -1;
        }
        System.out.println(request);
        //i表示有读到了多少字节,所以此处要用<而不是<=
        for (int j = 0; j < i; j++) {
            request.append((char)buffer[j]);
        }
        System.err.println(request.toString());
        //获取请求uri
        uri = parseUri(request.toString());
        //获取请求类型
        method = parseMethod(request.toString());
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
