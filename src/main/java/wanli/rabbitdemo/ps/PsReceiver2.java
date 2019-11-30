package wanli.rabbitdemo.ps;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wanli.rabbitdemo.util.ConnectionUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static wanli.rabbitdemo.ps.PsSender.EXCHANGE_NAME;

/**
 * @author wanli
 * @date 2019-11-22 17:15
 */
public class PsReceiver2 {

    private static final Logger logger = LoggerFactory.getLogger(PsReceiver2.class);
    private static final String PS_QUEUE_NAME = "publish_subscribe_queue_name2";

    public static void main(String[] args) throws IOException, TimeoutException {

        final Connection connection = ConnectionUtils.getConnection();

        final Channel channel = connection.createChannel();
        //队列申明
        channel.queueDeclare(PS_QUEUE_NAME, false, false, false, null);
        //绑定队列到交换机
        channel.queueBind(PS_QUEUE_NAME, EXCHANGE_NAME, "");

        channel.basicQos(1);
        channel.basicConsume(PS_QUEUE_NAME, false, (consumerTag, message) -> {
            logger.info("ps receiver2 message:[{}]", new String(message.getBody(), StandardCharsets.UTF_8));
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        }, consumerTag -> {
        });


    }
}
