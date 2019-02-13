package com.example.demo2.consumer;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {

    private List<String> messages = new ArrayList<>();

    @StreamListener(Sink.INPUT)
    public void getMessage(String message) {
        System.out.println("This was the message: "+message);
        messages.add(message);
    }

    public List<String> getMessages() {
        return messages;
    }
}
