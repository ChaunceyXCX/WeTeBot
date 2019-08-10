package com.chauncey.WeTeBot.api;

import com.chauncey.WeTeBot.model.chat.ChatParam;
import com.chauncey.WeTeBot.model.chat.ChatReturn;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @ClassName AiChatTools
 * @Description TODO
 * @Author https://github.com/ChaunceyCX
 * @Date 2019/8/6 下午6:14
 * @Version 1.0
 **/
@Component
@Log4j2
public class AiChatApi {
    private OkHttpClient client = new OkHttpClient();
    @Autowired
    private ObjectMapper objectMapper;

    public String chat(ChatParam chatParam) {
        String url = "https://api.ai.qq.com/fcgi-bin/nlp/nlp_textchat";
        String paramStr = "";
        try {
            paramStr = objectMapper.writeValueAsString(chatParam);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        FormBody.Builder builder = new FormBody.Builder();
        builder.add("app_id", String.valueOf(chatParam.getParams().get("app_id")));
        builder.add("time_stamp", String.valueOf(chatParam.getParams().get("time_stamp")));
        builder.add("nonce_str", String.valueOf(chatParam.getParams().get("nonce_str")));
        builder.add("sign", String.valueOf(chatParam.getParams().get("sign")));
        builder.add("session", String.valueOf(chatParam.getParams().get("session")));
        builder.add("question", String.valueOf(chatParam.getParams().get("question")));
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = null;
        String backStr = "";
        ChatReturn chatReturn = new ChatReturn();
        try {
            response = client.newCall(request).execute();
            backStr = response.body().string();
            chatReturn = objectMapper.readValue(backStr, ChatReturn.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return chatReturn.getData().get("answer");
    }
}
