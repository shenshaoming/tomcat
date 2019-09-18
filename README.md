# tomcat
手写tomcat
version1.0:实现了监听端口,但时不时会服务器就会崩溃.<br>
version2.0:通过增加访问队列修复了崩溃的bug.<br>
version3.0:能够通过服务器访问本地(服务器)的文件,默认是D盘下的文件,D盘就相当于WEB_ROOT路径
version3.1:实现多线程bio监听端口
version4.0:能够通过服务器访问Servlet程序
version4.1:通过扫描包和注解的形式,实现了类似SpringMvc的机制