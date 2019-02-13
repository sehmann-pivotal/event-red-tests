package com.example.demo.redtest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController("/")
@EnableBinding(MessageSource.class)
public class ProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProducerApplication.class, args);
	}

	@Autowired
	MessageSource messageSource;

	@PostMapping(value = "/makeMessage")
	public String enqueue(@RequestBody String body) {
		System.out.println(body);
		messageSource.messageChannel().send(MessageBuilder.withPayload(body).build());
		return "message sent";
	}
}

