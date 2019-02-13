package com.example.demo.consumer;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {

    private List<String> messages = new ArrayList<>();

    @StreamListener(Sink.INPUT)
    public void getMessage(String message) throws InterruptedException {
        System.out.println("This was the message: "+message);
        Thread.sleep(1000);
        messages.add(message);
    }

    public List<String> getMessages() {
        return messages;
    }
}
