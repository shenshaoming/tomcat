package com.tomcat.core;

import java.io.*;

/**
 * @Author: 申劭明
 * @Date: 2019/9/16 17:28
 */
public class Response {

    public static final int BUFFER_SIZE = 2048;

    private static final String WEB_ROOT = "D:";

    private Request request;

    private OutputStream output;

    public Response(OutputStream outputStream) {
        this.output = outputStream;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public void sendStaticResource() throws IOException {
        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fis = null;
        File file = new File(WEB_ROOT, request.getUrl());
        //如果文件存在,且不是个目录

        try {
            if (file.exists() && !file.isDirectory()) {
                fis = new FileInputStream(file);
                int ch = fis.read(bytes, 0, BUFFER_SIZE);
                while (ch != -1) {
                    output.write(bytes, 0, ch);
                    ch = fis.read(bytes, 0, BUFFER_SIZE);
                }
            }else {
                //文件不存在,返回给浏览器响应提示,这里可以拼接HTML任何元素
                String retMessage = "<h1>" + file.getName() + " file or directory not exists</h1>";
                String returnMessage = "HTTP/1.1 404 File Not Fount\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: " + retMessage.length() + "\r\n" +
                        "\r\n" +
                        retMessage;
                output.write(returnMessage.getBytes());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fis != null){
                fis.close();
            }
        }
    }
}
