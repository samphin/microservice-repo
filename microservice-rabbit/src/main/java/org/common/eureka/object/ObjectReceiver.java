package org.common.eureka.object;

import org.common.eureka.vo.UserVo;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "object")
public class ObjectReceiver {

    @RabbitHandler
    public void process(UserVo user) {
        System.out.println("Receiver object : " + user);
    }

}
