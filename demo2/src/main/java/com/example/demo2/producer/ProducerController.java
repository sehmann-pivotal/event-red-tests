package com.example.demo2.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class ProducerController {
    @Autowired
    private MessageProducer messageProducer;

    @Autowired
    MessageSource messageSource;

    @GetMapping(value = "/makeMessage")
    public String enqueue() {
        String message = "THe message";
        messageSource.messageChannel().send(MessageBuilder.withPayload(message).build());
        System.out.println(message);
        return "message sent";
    }

}
