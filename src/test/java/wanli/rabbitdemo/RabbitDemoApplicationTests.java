package wanli.rabbitdemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RabbitDemoApplicationTests {
    @Autowired
    private RaSender sender;

    @Test
    public void hello() {
        sender.send();
    }
}
