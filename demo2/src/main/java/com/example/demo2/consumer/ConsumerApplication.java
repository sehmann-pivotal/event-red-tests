package com.example.demo2.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@EnableBinding(Sink.class)
@SpringBootApplication
@RestController
@EnableWebSecurity
public class ConsumerApplication extends WebSecurityConfigurerAdapter {

	@Autowired
	private MessageService messageService;

	public static void main(String[] args) {
		SpringApplication.run(ConsumerApplication.class, args);
	}

	@GetMapping("/getMessages")
	public List<String> getMessages() {
		return messageService.getMessages();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().anyRequest().permitAll();
	}
}

