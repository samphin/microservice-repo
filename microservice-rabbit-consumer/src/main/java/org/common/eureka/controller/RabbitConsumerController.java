package org.common.eureka.controller;

import org.common.eureka.vo.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@RestController
public class RabbitConsumerController {

    @Autowired
    private Customer customer;

    @GetMapping("/queryMessage/{queueName}")
    public void queryMessage(@PathVariable String queueName) {
        try {
            customer.receiverMessage(queueName);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
