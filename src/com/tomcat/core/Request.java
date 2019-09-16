package com.tomcat.core;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: 申劭明
 * @Date: 2019/9/16 17:24
 */
public class Request {

    private InputStream is;
    private String url;

    public Request(){

    }

    public Request(InputStream inputStream){
        this.is = inputStream;
    }

    public void parse() {
        //从socket中读取一个2048长度的字符串
        StringBuffer request = new StringBuffer(Response.BUFFER_SIZE);
        int i ;
        byte[] buffer = new byte[Response.BUFFER_SIZE];
        try {
            i = is.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            i = -1;
        }
        for (int j = 0; j < i; j++) {
            request.append((char)buffer[j]);
        }
        System.out.println(request.toString());
        url = parseUrl(request.toString());
    }

    private String parseUrl(String request) {
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

    public String getUrl() {
        return url;
    }
}
