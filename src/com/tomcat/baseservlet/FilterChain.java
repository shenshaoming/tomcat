package com.tomcat.baseservlet;

import com.tomcat.core.HttpServer;
import com.tomcat.core.Request;
import com.tomcat.core.Response;

import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

/**
 * @author 申劭明
 * @date 2019/10/11 14:12
 */
public class FilterChain {

    /**
     * 记录所有的过滤器
     */
    public static final List<Filter> filters;
    /**
     * 当前指向的过滤器
     */
    private int nowFilter = -1;

    static{
        filters = getFilters("com.tomcat.filter");
    }

    public FilterChain() {
    }

    public void doFilter(Request request, Response response){
        if (nowFilter < filters.size() - 1){
            this.nowFilter++;
            filters.get(nowFilter).doFilterInternal(request,response,this);
        }
    }

    /**
     * @Description 扫描packageName包中的所有过滤器
     *
     * @param packageName 包名
     * @author 申劭明
     * @date 2019/10/11 14:38
     * @return
    */
    private static List<Filter> getFilters(String packageName){
        Set<Class<?>> classes = new HashSet<>();
        List<Filter> list;

        try {
            Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(packageName.replace(".", "/"));

            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    HttpServer.findAndAddClassesInPackageByFile(packageName, filePath, true, classes);
                } else if ("jar".equals(protocol)) {
                    //扫描JAR包
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        list = new ArrayList<>(classes.size());
        try{
            for (Class<?> aClass : classes) {
                Class<?>[] interfaces = aClass.getInterfaces();
                for (Class<?> anInterface : interfaces) {
                    if (Filter.class.equals(anInterface)){
                        list.add((Filter) aClass.newInstance());
                    }
                    System.out.println(list);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * @Description 用于判断过滤器是否都遍历过了
     *
     * @return boolean 是,否
     * @author 申劭明
     * @date 2019/10/11 15:39
    */
    public boolean hasNext() {
        return nowFilter < filters.size();
    }
}
