package com.chauncey.WeTeBot.service;


import com.alibaba.fastjson.JSONArray;

/**
 * @ClassName IMessageProcessService
 * @Description TODO
 * @Author https://github.com/ChaunceyCX
 * @Date 2019/8/9 下午3:58
 * @Version 1.0
 **/
public interface IMessageProcessService {

    JSONArray produceMsg(JSONArray msgList);

    void handleMsg(IMsgHandlerService msgHandlerService);
}
