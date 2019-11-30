package wanli.rabbitdemo.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wanli.rabbitdemo.util.ConnectionUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

/**
 * @author wanli
 * @date 2019-11-30 14:09
 */
public class ConfirmSender {
    private static final Logger logger = LoggerFactory.getLogger(ConfirmSender.class);
    private static final String CONFIRM_QUEUE = "confirm_queue_name";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        async();
    }

    public static void indivially() throws IOException, TimeoutException, InterruptedException {
        final Connection connection = ConnectionUtils.getConnection();
        final Channel channel = connection.createChannel();
        channel.queueDeclare(CONFIRM_QUEUE, false, false, false, null);
        channel.confirmSelect();
        channel.basicPublish("", CONFIRM_QUEUE, null, "cf ms".getBytes(StandardCharsets.UTF_8));
        channel.waitForConfirmsOrDie(1000);
        channel.close();
        connection.close();
    }

    public static void batch() throws IOException, InterruptedException, TimeoutException {
        final Connection connection = ConnectionUtils.getConnection();
        final Channel channel = connection.createChannel();
        channel.queueDeclare(CONFIRM_QUEUE, false, false, false, null);
        channel.confirmSelect();
        channel.basicPublish("", CONFIRM_QUEUE, null, "cf msg1".getBytes(StandardCharsets.UTF_8));
        channel.basicPublish("", CONFIRM_QUEUE, null, "cf msg2".getBytes(StandardCharsets.UTF_8));
        channel.basicPublish("", CONFIRM_QUEUE, null, "cf msg3".getBytes(StandardCharsets.UTF_8));
        if (!channel.waitForConfirms()) {
            logger.info("msg send failed");
        }
        channel.close();
        connection.close();
    }

    public static void async() throws IOException, TimeoutException {
        final Connection connection = ConnectionUtils.getConnection();
        final Channel ch = connection.createChannel();

        ConcurrentNavigableMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();
        ConfirmCallback cleanOutstandingConfirms = (deliveryTag, multiple) -> {
            if (multiple) {
                //清除tag以下的值
                ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(deliveryTag, true);
                confirmed.clear();
            } else {
                outstandingConfirms.remove(deliveryTag);
            }
        };
        String sendMsg = "async msg";
        //用一个容器记录需要发送的内容
        outstandingConfirms.put(ch.getNextPublishSeqNo(), sendMsg);
        ch.basicPublish("", CONFIRM_QUEUE, null, sendMsg.getBytes(StandardCharsets.UTF_8));
        ch.addConfirmListener(cleanOutstandingConfirms, (deliveryTag, multiple) -> {
            //这里只打印日志, 通常情况还会有重试
            logger.error("Message with body [{}] has been nack-ed, Sequence number: [{}], multiple:[{}]",
                    sendMsg, deliveryTag, multiple);
            cleanOutstandingConfirms.handle(deliveryTag, multiple);
        });
        ch.close();
        connection.close();
    }
}
