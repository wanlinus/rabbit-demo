package wanli.rabbitdemo.ps;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wanli.rabbitdemo.util.ConnectionUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author wanli
 * @date 2019-11-22 16:59
 */
public class PsSender {
    private static final Logger logger = LoggerFactory.getLogger(PsSender.class);
    /**
     * 定义交换机
     */
    static final String EXCHANGE_NAME = "demo_exchange";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = ConnectionUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        final String msg = "send public subscribe message";
        for (int i = 0; i < 50; i++) {
            channel.basicPublish(EXCHANGE_NAME, "", null, msg.getBytes(StandardCharsets.UTF_8));
            logger.info("public/subscribe send message:[{}]", msg);
            Thread.sleep(100);
        }

        channel.close();
        connection.close();
    }
}
