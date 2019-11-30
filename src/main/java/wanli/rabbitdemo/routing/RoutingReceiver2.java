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

import static wanli.rabbitdemo.routing.RoutingSender.*;

/**
 * 同时接受info和error消息
 *
 * @author wanli
 * @date 2019-11-26 11:28
 */
public class RoutingReceiver2 {
    private static final Logger logger = LoggerFactory.getLogger(RoutingReceiver2.class);
    private static final String ROUTING_QUEUE_2 = "routing_queue_2";

    public static void main(String[] args) throws IOException, TimeoutException {
        final Connection connection = ConnectionUtils.getConnection();
        final Channel channel = connection.createChannel();
        channel.exchangeDeclare(ROUTING_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        channel.queueDeclare(ROUTING_QUEUE_2, false, false, false, null);
        channel.queueBind(ROUTING_QUEUE_2, ROUTING_EXCHANGE_NAME, ROUTING_KEY_ERROR);
        channel.queueBind(ROUTING_QUEUE_2, ROUTING_EXCHANGE_NAME, ROUTING_KEY_INFO);

        channel.basicQos(1);
        channel.basicConsume(ROUTING_QUEUE_2, false, (consumerTag, message) -> {
            logger.info("receive2 message {}", new String(message.getBody(), StandardCharsets.UTF_8));
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        }, consumerTag -> {
        });
    }
}
