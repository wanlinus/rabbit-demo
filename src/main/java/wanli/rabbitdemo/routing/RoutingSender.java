package wanli.rabbitdemo.routing;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wanli.rabbitdemo.util.ConnectionUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 *
 * 同时发送error 和 info两种消息
 * @author wanli
 * @date 2019-11-26 11:19
 */
public class RoutingSender {

    private static final Logger logger = LoggerFactory.getLogger(RoutingSender.class);
    static final String ROUTING_EXCHANGE_NAME = "routing_exchange_name";
    static final String ROUTING_KEY_ERROR = "error";
    static final String ROUTING_KEY_INFO = "info";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        final Connection connection = ConnectionUtils.getConnection();
        final Channel channel = connection.createChannel();
        channel.exchangeDeclare(ROUTING_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        for (int i = 0; i < 10; i++) {
            channel.basicPublish(ROUTING_EXCHANGE_NAME, ROUTING_KEY_ERROR, null,
                    "发送error消息".getBytes(StandardCharsets.UTF_8));
            logger.info("send error msg");
            channel.basicPublish(ROUTING_EXCHANGE_NAME, ROUTING_KEY_INFO, null,
                    "发送info消息".getBytes(StandardCharsets.UTF_8));
            logger.info("send info msg");
            Thread.sleep(1000);
        }
        channel.close();
        connection.close();
    }
}
