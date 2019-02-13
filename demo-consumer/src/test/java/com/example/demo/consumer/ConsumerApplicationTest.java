package com.example.demo.consumer;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ResponseBody;
import com.rabbitmq.client.impl.AMQChannel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.channel.AbstractMessageChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static com.jayway.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.verify;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class ConsumerApplicationTest {
    @Autowired
    private Sink channels;
    private AbstractMessageChannel input;

    @LocalServerPort
    private int port;


    @Before
    public void setup() {
        input = (AbstractMessageChannel) this.channels.input();
    }
    @Test
    public void queueIsConfigured() {
        final AtomicReference<String> messageAtomicReference = new AtomicReference<>();
        ChannelInterceptorAdapter assertionInterceptor = new ChannelInterceptorAdapter() {

            @Override
            public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
                messageAtomicReference.set((String) message.getPayload());
                super.afterSendCompletion(message, channel, sent, ex);
            }

        };
        input.addInterceptor(assertionInterceptor);
        input.send(new GenericMessage<>("foo"));
        input.removeInterceptor(assertionInterceptor);


        String message1 = messageAtomicReference.get();
        assertThat(message1).isNotNull();
        assertThat(message1).isEqualTo("foo");
    }

    @Test
    public void consumesMessages() {
        // This actually waits for the message to be consumed before moving on to the next command.
        input.send(new GenericMessage<>("foo"));
        input.send(new GenericMessage<>("bar"));

        Response response = given().when().get("http://localhost:" + port + "/getMessages").andReturn();
        assertThat(response.statusCode()).isEqualTo(200);
        ResponseBody body = response.body();
        JsonPath bodyJson = body.jsonPath();
        List<String> messages = bodyJson.getList("$");
        assertThat(messages).hasSize(9);

        // Two different ways to make these assertions
        assertThat(messages.get(0)).isEqualTo("foo");
        assertThat(messages.get(1)).isEqualTo("bar");
        assertThat(bodyJson.getString("[0]")).isEqualTo("foo");
        assertThat(bodyJson.getString("[1]")).isEqualTo("bar");
    }

}