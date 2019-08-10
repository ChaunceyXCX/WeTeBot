package com.chauncey.WeTeBot.model.chat;

import lombok.Data;

import java.util.Map;

/**
 * @ClassName ChatReturn
 * @Description TODO
 * @Author https://github.com/ChaunceyCX
 * @Date 2019/8/6 下午7:27
 * @Version 1.0
 **/
@Data
public class ChatReturn {
    private int ret;
    private String msg;
    private Map<String, String> data;
    private String session;
    private String answer;
}
