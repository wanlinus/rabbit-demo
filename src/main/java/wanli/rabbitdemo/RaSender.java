package wanli.rabbitdemo;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wanli
 * @date 2019-11-22 11:10
 */
@Component
public class RaSender {

    @Autowired
    private AmqpTemplate rabbitmqTemplate;

    public void send() {
        for (int i = 0; i < 10; i++) {
            System.out.println("SimpleSender : hello message");
            rabbitmqTemplate.convertAndSend("hello", "hello message");
        }
    }

}
