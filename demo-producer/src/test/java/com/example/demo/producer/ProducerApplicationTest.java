package com.example.demo.producer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.messaging.Message;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.BlockingQueue;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.springframework.cloud.stream.test.matcher.MessageQueueMatcher.receivesPayloadThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ProducerApplication.class)
@DirtiesContext
public class ProducerApplicationTest {
    @LocalServerPort
    int port;





    @Autowired
    private MessageSource channels;

    @Autowired
    private MessageCollector collector;

    @Test
    public void testMessages() {
        BlockingQueue<Message<?>> messages = this.collector.forChannel(channels.messageChannel());



        String path = "http://localhost:" + port + "/makeMessage";
        given().when().body("Message 1").post(path).then().statusCode(200);
        given().when().body("Message 2").post(path).then().statusCode(200);
        given().when().body("Message 3").post(path).then().statusCode(200);

        assertThat(messages.size(), is(3));
        assertThat(messages, receivesPayloadThat(is("Message 1")));
        assertThat(messages, receivesPayloadThat(is("Message 2")));
        assertThat(messages, receivesPayloadThat(is("Message 3")));
    }
}