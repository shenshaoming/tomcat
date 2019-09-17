package com.tomcat.core;

import java.io.*;

/**
 * @Author: 申劭明
 * @Date: 2019/9/16 17:28
 */
public class Response {

    /**
     * 传输数组的最大字节数
     */
    public static final int BUFFER_SIZE = 2048;

    /**
     * 访问的文件的路径,即tomcat中部署项目的目录
     */
    private static final String WEB_ROOT = "D:";

    /**
     * 请求
     */
    private Request request;

    /**
     * 返回页面的数据
     */
    private OutputStream output;

    public Response(OutputStream outputStream) {
        this.output = outputStream;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public void sendStaticResource() throws IOException {
        //返回数据时所用的字节流
        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fis = null;
        File file = new File(WEB_ROOT, request.getUrl());
        String returnMessage = "";
        try {
            //如果文件存在,且不是个目录
            if (file.exists() && !file.isDirectory()) {
                fis = new FileInputStream(file);
                //读文件
                int ch = -1;

                StringBuilder sb = new StringBuilder(BUFFER_SIZE);
                //写文件
                while ((ch = fis.read(bytes,0,bytes.length)) != -1) {
                    sb.append(new String(bytes,0,ch,"GBK"));
                }
                returnMessage = "HTTP/1.1 404 File Not Fount\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: " + sb.length() + "\r\n" +
                        "\r\n" +
                        sb;
            }else {
                //文件不存在,返回给浏览器响应提示,这里可以拼接HTML任何元素
                String retMessage = "<h1>" + file.getName() + " file or directory not exists</h1>";
                returnMessage = "HTTP/1.1 404 File Not Fount\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: " + retMessage.length() + "\r\n" +
                        "\r\n" +
                        retMessage;
            }
            output.write(returnMessage.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fis != null){
                fis.close();
            }
        }
    }
}
