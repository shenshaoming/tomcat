import com.tomcat.core.HttpServer;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: 申劭明
 * @date: 2019-09-16
 */
public class Main {

    private static volatile int intI = 1;

    public static void main(String[] args) {
        HttpServer server = new HttpServer();
        server.acceptWait();
    }
}
