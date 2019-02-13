package com.example.demo.redtest;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.concurrent.Executors;

@SpringBootApplication
public class RedTestApplication {
    public static void main(String[] args) throws IOException, InterruptedException {

        // Doesn't work
        Process pwd = Runtime.getRuntime()
                .exec("sh -c pwd");
        Process process1 = Runtime.getRuntime()
                .exec("sh -c ./bootApplications.sh");
        Executors.newSingleThreadExecutor().submit(new StreamGobbler(process1.getInputStream(), System.out::println));
        process1.waitFor();
        Executors.newSingleThreadExecutor().submit(new StreamGobbler(pwd.getInputStream(), System.out::println));
    }
}
