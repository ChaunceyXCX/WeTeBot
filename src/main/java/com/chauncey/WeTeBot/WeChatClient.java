package com.chauncey.WeTeBot;

//import com.chauncey.WeTeBot.service.LoginService;

import com.chauncey.WeTeBot.controller.LoginController;
import com.chauncey.WeTeBot.thread.MessageHandleThread;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class WeChatClient {

    @Autowired
    private LoginController loginController;

    //启动器
    public void start() {
        System.setProperty("jsse.enableSNIExtension", "false"); // 防止SSL错误
        loginController.login();
        log.info("开始处理消息");
        new Thread(new MessageHandleThread()).start();
    }
}