package com.cherry.WeTeBot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class WeTeBotApplication {

    private static final Logger logger = LoggerFactory.getLogger(WeTeBotApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(WeTeBotApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(WeChatClient weChatClient) throws Exception {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            logger.error(e.getMessage(), e);
            System.exit(1);
        });
        return args -> weChatClient.start();
    }
}
