package com.example.demo.consumer;

import groovyjarjarantlr.debug.MessageAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.channel.AbstractMessageChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {

    private final MessageChannel outputChannel;

    public MessageService(@Qualifier("output")MessageChannel outputChannel) {
        this.outputChannel = outputChannel;
    }

    private List<String> messages = new ArrayList<>();

    @StreamListener(Sink.INPUT)
    public void getMessage(String message) throws InterruptedException {
        System.out.println("This was the message: "+message);
        Thread.sleep(1000);
        messages.add(message);

        outputChannel.send(new GenericMessage<String>("final event!"));

    }

    public List<String> getMessages() {
        return messages;
    }

    public void reset() {
        messages = new ArrayList<>();
    }
}
