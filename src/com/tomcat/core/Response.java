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
     * 响应头信息
     */
    private static final String RESPONSE_HEADER = "HTTP/1.1 200 Read File Success\r\n" +
            "Content-Type: text/html\r\n" + "\r\n";

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

    /**
     * @Description : 向页面返回数据
     *
     * @author : 申劭明
     * @date : 2019/9/17 10:27
    */
    public void sendStaticResource() throws IOException {
        //返回数据时所用的字节流
        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fis = null;
        File file = new File(WEB_ROOT, request.getUri());
        String returnMessage = null;
        try {
            //如果文件存在,且不是个目录
            if (file.exists() && !file.isDirectory()) {
                fis = new FileInputStream(file);
                //读文件
                int ch ;

                StringBuilder sb = new StringBuilder(BUFFER_SIZE);
                //写文件
                while ((ch = fis.read(bytes,0,bytes.length)) != -1) {
                    sb.append(new String(bytes,0,ch));
                }
                returnMessage =  RESPONSE_HEADER + sb;

            }else {
                //文件不存在,返回给浏览器响应提示,这里可以拼接HTML任何元素
                String retMessage = "<h1>" + file.getName() + " file or directory not exists</h1>";
                returnMessage = "HTTP/1.1 404 File Not Fount\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: " + retMessage.length() + "\r\n" +
                        "\r\n" +
                        retMessage;
            }
            //用输出流返回数据给页面
            if (checkImage(request.getUri())){
                output.write(returnMessage.replaceAll("text/html","image/jpeg;charset=UTF-8").getBytes());
            }else{
                output.write(returnMessage.getBytes());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fis != null){
                fis.close();
            }
            if (output != null){
                //清空缓存区,调用close方法时会有flush操作
//                output.flush();
                output.close();
            }
        }
    }

    /**
     * @Description : 判断请求是否为图片类型
     *
     * @param uri 请求的uri
     * @return : 是/否
     * @author : 申劭明
     * @date : 2019/9/18 14:21
    */
    private boolean checkImage(String uri) {
        boolean flag = uri.endsWith(".jpg") || uri.endsWith(".png") || uri.endsWith(".jpeg");
        return flag;
    }

    /**
     * @Description : 设置返回数据
     *
     * @param message 返回给页面的数据
     * @author : 申劭明
     * @date : 2019/9/18 10:19
    */
    public void setResponseContent(StringBuilder message){
        try {
            output.write(new StringBuilder(RESPONSE_HEADER).append(message).toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setResponseContent(String message){
        setResponseContent(new StringBuilder(message));
    }
}
