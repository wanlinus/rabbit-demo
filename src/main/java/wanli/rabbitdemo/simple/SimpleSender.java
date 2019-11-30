package wanli.rabbitdemo.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wanli.rabbitdemo.util.ConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author wanli
 * @date 2019-11-22 13:02
 */
public class SimpleSender {
    private static final Logger logger = LoggerFactory.getLogger(SimpleSender.class);
    static final String QUEUE_NAME = "queue_1";

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建一个通道
        try (Connection connection = ConnectionUtils.getConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String msg = "hello simple";
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
            logger.info("send rabbit message:[{}]", msg);
        }
    }
}
