package com.cherry.WeTeBot;

import com.cherry.WeTeBot.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WeChatClient {
    @Autowired
    private LoginService loginService;
    @Value("${wechat.url.login}")
    private String url;
    @Value("${jeeves.instance-id}")
    private String instanceId;

    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    public void start() {
        logger.info("Jeeves starts");
        logger.info("Jeeves id = " + instanceId);
        System.setProperty("jsse.enableSNIExtension", "false");
        loginService.login();
    }
}