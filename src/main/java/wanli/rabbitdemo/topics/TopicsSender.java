package wanli.rabbitdemo.topics;

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
 * @author wanli
 * @date 2019-11-26 14:55
 */
public class TopicsSender {

    private static final Logger logger = LoggerFactory.getLogger(TopicsSender.class);
    static final String TOPICS_EXCHANGE_NAME = "topics_exchange_name";

    public static void main(String[] args) throws IOException, TimeoutException {
        final Connection connection = ConnectionUtils.getConnection();
        final Channel channel = connection.createChannel();
        channel.basicQos(1);

        channel.exchangeDeclare(TOPICS_EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        channel.basicPublish(TOPICS_EXCHANGE_NAME, "wanli.haha",null, "topic wanli.haha msg".getBytes(StandardCharsets.UTF_8) );
        channel.basicPublish(TOPICS_EXCHANGE_NAME, "vhsj.haha", null, "topic vhsj.haha msg".getBytes(StandardCharsets.UTF_8));
        channel.basicPublish(TOPICS_EXCHANGE_NAME, "wanli.hehe", null, "topic wanli.hehe msg".getBytes(StandardCharsets.UTF_8));
        logger.info("Three pieces of message were sent");
        channel.close();
        connection.close();
    }
}
