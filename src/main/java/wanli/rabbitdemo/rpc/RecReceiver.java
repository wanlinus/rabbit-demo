package wanli.rabbitdemo.rpc;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wanli.rabbitdemo.util.ConnectionUtils;

import java.nio.charset.StandardCharsets;

import static wanli.rabbitdemo.rpc.RpcSender.RPC_QUEUE_NAME;

/**
 * @author wanli
 * @date 2019-11-30 16:42
 */
public class RecReceiver {

    private static final Logger logger = LoggerFactory.getLogger(RecReceiver.class);

    public static void main(String[] args) throws Exception {
        final Connection connection = ConnectionUtils.getConnection();
        final Channel channel = connection.createChannel();

        channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
        channel.basicQos(1);

        channel.basicConsume(RPC_QUEUE_NAME, false, (consumerTag, message) -> {
            AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder()
                    .correlationId(message.getProperties().getCorrelationId()).build();
            logger.info("receive msg: [{}]", new String(message.getBody(), StandardCharsets.UTF_8));
            channel.basicPublish("", message.getProperties().getReplyTo(), replyProps, "response".getBytes(StandardCharsets.UTF_8));
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        }, consumerTag -> {
        });
    }
}
