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

import static wanli.rabbitdemo.topics.TopicsSender.TOPICS_EXCHANGE_NAME;

/**
 * @author wanli
 * @date 2019-11-26 15:26
 */
public class TopicReceiver1 {

    private static final Logger logger = LoggerFactory.getLogger(TopicReceiver1.class);
    private static final String TOPIC_RECEIVER_1 = "topic_1";

    public static void main(String[] args) throws IOException, TimeoutException {
        final Connection connection = ConnectionUtils.getConnection();
        final Channel channel = connection.createChannel();

        channel.exchangeDeclare(TOPICS_EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        channel.queueDeclare(TOPIC_RECEIVER_1, false, false, false, null);
        // 接收以wanli.路由键开头的消息
        channel.queueBind(TOPIC_RECEIVER_1, TOPICS_EXCHANGE_NAME, "wanli.#");

        channel.basicConsume(TOPIC_RECEIVER_1, false, (consumerTag, message) -> {
            logger.info("receive wanli.# msg [{}]", new String(message.getBody(), StandardCharsets.UTF_8));
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        }, consumerTag -> {
        });
    }
}
