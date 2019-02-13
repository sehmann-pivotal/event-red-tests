package com.example.demo2.producer;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.stereotype.Service;

@Service
@EnableBinding(MessageSource.class)
public class MessageProducer {

}
