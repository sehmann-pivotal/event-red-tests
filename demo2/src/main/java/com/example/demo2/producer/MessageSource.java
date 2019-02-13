package com.example.demo2.producer;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MessageSource {
    @Output("foodOrdersChannel")
    MessageChannel messageChannel();
}
