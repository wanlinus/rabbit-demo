package wanli.rabbitdemo.work;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import wanli.rabbitdemo.util.ConnectionUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author wanli
 * @date 2019-11-22 15:13
 */
public class WorkerSender {
    static final String WORKER_QUEUE = "worker_queue";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = ConnectionUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(WORKER_QUEUE, false, false, false, null);
        //每个消费者发送确认消息之前, 消息队列不发送下一个消息到消息队列, 一次只处理一个消息
        channel.basicQos(1);
        for (int i = 0; i < 500; i++) {
            final String s = "worker: " + i;
            channel.basicPublish("", WORKER_QUEUE, null, s.getBytes(StandardCharsets.UTF_8));
            Thread.sleep(100);
        }
        channel.close();
        connection.close();
    }
}
