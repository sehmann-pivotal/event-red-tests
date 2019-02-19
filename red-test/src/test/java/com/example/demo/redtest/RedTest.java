package com.example.demo.redtest;

import com.jayway.restassured.response.Response;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static com.jayway.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RedTest {

    private Channel channel;
    private Connection connection;

    @Before
    public void setup() throws InterruptedException, IOException, TimeoutException {
        given().get("http://localhost:4321/reset").then().statusCode(200);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        connection = factory.newConnection();
        channel = connection.createChannel();

        channel.queueDeclare("thingResultQueue", false, false, false, null);
//        String message = "Hello World!";
//        channel.basicPublish("thingTopicSlashExchange", "thingQueue", null, message.getBytes());
//        System.out.println(" [x] Sent '" + message + "'");
    }

    @After
    public void tearDown() throws InterruptedException {
        given().get("http://localhost:4321/reset").then().statusCode(200);
    }

    @Test
    public void testsWholeThing() throws InterruptedException, IOException {

        String path = "http://localhost:1234/makeMessage";
        given().body("message 1").post(path).then().statusCode(200);
        given().body("message 2").post(path).then().statusCode(200);
        given().body("message 3").post(path).then().statusCode(200);
        given().body("message 4").post(path).then().statusCode(200);

        Thread.sleep(5000L);

        Response response = given().get("http://localhost:4321/getMessages");
        assertThat(response.statusCode()).isEqualTo(200);
        List<String> results = response.getBody().jsonPath().getList("$");
        assertThat(results).hasSize(4);
        assertThat(results).containsExactly("message 1", "message 2", "message 3", "message 4");

        assertThat(channel.messageCount("thingResultQueue")).isEqualTo(1);
        assertThat(new String(channel.basicGet("thingResultQueue", true).getBody())).isEqualTo("final event!");
    }

}