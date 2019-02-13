package com.example.demo.redtest;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MessageSource {
    @Output("foodOrdersChannel")
    MessageChannel messageChannel();
}
