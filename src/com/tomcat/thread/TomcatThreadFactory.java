package com.tomcat.thread;


import java.util.concurrent.ThreadFactory;

/**
 * @Author: 申劭明
 * @Date: 2019/9/26 17:37
 */
public class TomcatThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r);
    }
}
