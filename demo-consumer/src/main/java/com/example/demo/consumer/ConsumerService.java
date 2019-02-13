package com.example.demo.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/")
public class ConsumerService {
    @Autowired
    private MessageService messageService;

    @GetMapping(value = "/getEvents")
    public List<String> getEvents() {
        return messageService.getMessages();
    }

}
