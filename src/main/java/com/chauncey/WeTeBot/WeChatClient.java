package com.chauncey.WeTeBot;

//import com.chauncey.WeTeBot.service.LoginService;

import com.chauncey.WeTeBot.controller.LoginController;
import com.chauncey.WeTeBot.service.IMsgHandlerService;
import com.chauncey.WeTeBot.service.MsgCenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WeChatClient {

    private static final Logger logger = LoggerFactory.getLogger(WeChatClient.class);
    @Autowired
    private IMsgHandlerService msgHandler;
    @Autowired
    private LoginController loginController;

    //启动器
    public void start() {
        System.setProperty("jsse.enableSNIExtension", "false"); // 防止SSL错误
        loginController.login();
        logger.info("开始处理消息");
        new Thread(new Runnable() {
            @Override
            public void run() {
                MsgCenter.handleMsg(msgHandler);
            }
        }).start();
    }
}