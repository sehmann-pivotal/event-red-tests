package com.example.demo.redtest;

import com.jayway.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.jayway.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class RedTest {
    @Before
    public void setup() throws InterruptedException {
        given().get("http://localhost:4321/reset").then().statusCode(200);
    }

    @After
    public void tearDown() throws InterruptedException {
        given().get("http://localhost:4321/reset").then().statusCode(200);
    }

    @Test
    public void testsWholeThing() throws InterruptedException {

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
    }

}