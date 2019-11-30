package wanli.rabbitdemo.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wanli.rabbitdemo.util.ConnectionUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static wanli.rabbitdemo.simple.SimpleSender.QUEUE_NAME;

/**
 * @author wanli
 * @date 2019-11-22 14:05
 */
public class SimpleReceiver {
    private static final Logger logger = LoggerFactory.getLogger(SimpleReceiver.class);
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = ConnectionUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //监听队列
        channel.basicConsume(QUEUE_NAME, true, (consumerTag, message) -> {
            logger.info("Received message:[{}]", new String(message.getBody(), StandardCharsets.UTF_8));
        }, consumerTag -> {
        });

        Thread.sleep(1000);
        channel.close();
        connection.close();
    }
}
