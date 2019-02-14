package com.example.demo.consumer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.test.context.junit4.SpringRunner;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WeirdOldTest {

    @LocalServerPort
    private int port;

    @Autowired
    private Source source;

    @Test
    public void contextLoads() {
        assertThat(this.source.output()).isNotNull();
    }

    @Test
    public void endpoint_createsAMessage() {
        MessageHandler messageHandler = new MessageHandler();

        String path = "http://localhost:" + port + "/createEvent";
        when().get(path).then().statusCode(200);

        // check that the message was enqueued
        assertThat(messageHandler.messageCount).isEqualTo(1);
        assertThat(messageHandler.message).isEqualTo("This is the message");
    }

    public class MessageHandler {
        String message = null;
        int messageCount = 0;

        @StreamListener(target = Sink.INPUT)
        public void handle(String message) {
            this.message = message;
            messageCount++;
        }
    }
}

