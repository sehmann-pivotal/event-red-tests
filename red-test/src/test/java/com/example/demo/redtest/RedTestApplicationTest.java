package com.example.demo.redtest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;

@SpringBootTest(classes = RedTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class RedTestApplicationTest {

    @Before
    public void startUp() {
        // check to see that they're running
    }

    @Test
    public void testsWholeThing() throws InterruptedException {

        Thread.sleep(5000L);
    // call endpoint in producer
    // Maybe wait?
    // call endpoint in consumer & verify messages got processed correctly
    }

}