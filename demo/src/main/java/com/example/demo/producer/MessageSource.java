package com.example.demo.producer;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MessageSource {
    @Output("foodOrdersChannel")
    MessageChannel messageChannel();
}
