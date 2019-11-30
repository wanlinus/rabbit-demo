package wanli.rabbitdemo.tx;

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
 * @date 2019-11-30 11:15
 */
public class TxSender {
    private static final Logger logger = LoggerFactory.getLogger(TxSender.class);
    static final String TX_QUEUE_NAME = "tx_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        final Connection connection = ConnectionUtils.getConnection();
        final Channel channel = connection.createChannel();
        channel.queueDeclare(TX_QUEUE_NAME, false, false, false, null);
        channel.txSelect();
        try {
            channel.basicPublish("", TX_QUEUE_NAME, null, "send tx message 1".getBytes(StandardCharsets.UTF_8));
            channel.basicPublish("", TX_QUEUE_NAME, null, "send tx message 2".getBytes(StandardCharsets.UTF_8));
            int x = 1 / 0;
            channel.txCommit();
        } catch (Exception e) {
            channel.txRollback();
        }
        channel.close();
        connection.close();
    }
}
