import com.tomcat.core.HttpServer;

/**
 * @author: 申劭明
 * @date: 2019-09-16
 */
public class Main {

    public static void main(String[] args) {
        HttpServer server = new HttpServer();
        server.acceptWait();
    }
}
