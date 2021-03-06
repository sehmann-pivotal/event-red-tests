package com.example.demo.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@EnableBinding(Processor.class)
@SpringBootApplication
@RestController("/")
public class ConsumerApplication {

    @Autowired
    private MessageService messageService;

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

    @GetMapping("/getMessages")
    public List<String> getMessages() {
        return messageService.getMessages();
    }

    @GetMapping("/reset")
    public void reset() {
        messageService.reset();
    }
}

