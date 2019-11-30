package wanli.rabbitdemo.work;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wanli.rabbitdemo.util.ConnectionUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static wanli.rabbitdemo.work.WorkerSender.WORKER_QUEUE;

/**
 * @author wanli
 * @date 2019-11-22 15:56
 */
public class WorkerReceiver1 {

    private static final Logger logger = LoggerFactory.getLogger(WorkerReceiver1.class);

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        final Connection connection = ConnectionUtils.getConnection();
        final Channel channel = connection.createChannel();
        channel.queueDeclare(WORKER_QUEUE, false, false, false, null);
        channel.basicQos(1);
        channel.basicConsume(WORKER_QUEUE, false, (consumerTag, message) -> {
            logger.info("worker received 1 message [{}]", new String(message.getBody(), StandardCharsets.UTF_8));
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, consumerTag -> {
        });
        Thread.sleep(10000000);
        channel.close();
        connection.close();
    }
}
