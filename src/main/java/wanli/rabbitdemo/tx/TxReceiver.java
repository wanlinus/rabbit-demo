package wanli.rabbitdemo.tx;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wanli.rabbitdemo.util.ConnectionUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static wanli.rabbitdemo.tx.TxSender.TX_QUEUE_NAME;

/**
 * @author wanli
 * @date 2019-11-30 11:19
 */
public class TxReceiver {
    private static final Logger logger = LoggerFactory.getLogger(TxReceiver.class);

    public static void main(String[] args) throws IOException, TimeoutException {
        final Connection connection = ConnectionUtils.getConnection();
        final Channel channel = connection.createChannel();
        channel.queueDeclare(TX_QUEUE_NAME, false, false, false, null);

        channel.basicConsume(TX_QUEUE_NAME, false, (consumerTag, message) -> {
            logger.info("Receive tx message:[{}]", new String(message.getBody(), StandardCharsets.UTF_8));
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        }, consumerTag -> {
        });
    }
}
