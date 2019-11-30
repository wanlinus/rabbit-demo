package wanli.rabbitdemo.rpc;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wanli.rabbitdemo.util.ConnectionUtils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @author wanli
 * @date 2019-11-30 16:21
 */
public class RpcSender {
    private static final Logger logger = LoggerFactory.getLogger(RpcSender.class);

    public static final String RPC_QUEUE_NAME = "rpc_queue";

    public static void main(String[] args) throws Exception {
        final Connection connection = ConnectionUtils.getConnection();
        final Channel channel = connection.createChannel();

        channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
        final String callbackQueue = channel.queueDeclare().getQueue();

        final String corrId = UUID.randomUUID().toString();

        AMQP.BasicProperties props = new AMQP.BasicProperties()
                .builder()
                .correlationId(corrId)
                .replyTo(callbackQueue)
                .build();

        channel.basicPublish("", RPC_QUEUE_NAME, props, "rpc".getBytes(StandardCharsets.UTF_8));
        channel.basicConsume(callbackQueue, true, (consumerTag, message) -> {
            if (message.getProperties().getCorrelationId().equals(corrId)) {
                logger.info("callback queue:[{}] msg:[{}]", callbackQueue, new String(message.getBody(), StandardCharsets.UTF_8));
            }
        }, consumerTag -> {
        });

        //等待返回, 这里简单处理了
        Thread.sleep(1000);
        channel.close();
        connection.close();
    }
}
