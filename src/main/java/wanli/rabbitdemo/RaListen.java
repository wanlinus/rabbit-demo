package wanli.rabbitdemo;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author wanli
 * @date 2019-11-22 11:12
 */
@Component
@RabbitListener(queues = "hello")
public class RaListen {
    @RabbitHandler
    public void handle(String hello) {
        System.out.println("receviver message: " + hello);
    }
}
