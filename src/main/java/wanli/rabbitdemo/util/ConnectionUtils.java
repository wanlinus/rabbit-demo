package wanli.rabbitdemo.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author wanli
 * @date 2019-11-22 12:55
 */
public final class ConnectionUtils {
    private ConnectionUtils() {
        throw new AssertionError("工具类不能实例化");
    }

    public static Connection getConnection() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("172.16.1.20");
        factory.setPort(5672);
        factory.setVirtualHost("/hello");
        factory.setUsername("wanli");
        factory.setPassword("123456");
        return factory.newConnection();
    }
}
