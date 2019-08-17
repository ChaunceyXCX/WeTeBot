package com.chauncey.WeTeBot.thread;

import com.chauncey.WeTeBot.service.IMessageProcessService;
import com.chauncey.WeTeBot.service.IMsgHandlerService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName MessageHandleThread
 * @Description TODO
 * @Author https://github.com/ChaunceyCX
 * @Date 2019/8/9 下午3:55
 * @Version 1.0
 **/
public class MessageHandleThread implements Runnable {

    private IMessageProcessService messageProcessService;
    private IMsgHandlerService msgHandler;

    public MessageHandleThread(IMessageProcessService messageProcessService, IMsgHandlerService msgHandler) {
        this.messageProcessService = messageProcessService;
        this.msgHandler = msgHandler;
    }

    @Override
    public void run() {
        messageProcessService.handleMsg(msgHandler);
    }
}
