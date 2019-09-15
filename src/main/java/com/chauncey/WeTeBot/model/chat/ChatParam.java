package com.chauncey.WeTeBot.model.chat;

import lombok.Data;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @ClassName ChatParam
 * @Description TODO
 * @Author https://github.com/ChaunceyCX
 * @Date 2019/8/6 下午7:23
 * @Version 1.0
 **/
@Data
public class ChatParam {
    private Map<String, Object> params;


    public ChatParam(String appId, String appKey, String content) {
        this.params = new HashMap<>();
        params.put("app_id", appId);
        params.put("time_stamp", new Date().getTime() / 1000);
        params.put("nonce_str", getRandomString(16));
        params.put("session", "2119608286");
        params.put("question", content);

        try {
            params = getSignature(params, appKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String md5(String plainText) {
        //定义一个字节数组
        byte[] secretBytes = null;
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            //对字符串进行加密
            md.update(plainText.getBytes());
            //获得加密后的数据
            secretBytes = md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有md5这个算法！");
        }
        //将加密后的数据转换为16进制数字
        String md5code = new BigInteger(1, secretBytes).toString(16);// 16进制数字
        // 如果生成数字未满32位，需要前面补0
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }

        return md5code.toUpperCase();
    }

    public String getRandomString(int length) {
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public Map<String, Object> getSignature(Map<String, Object> params, String CONFIG) throws IOException {
        Map<String, Object> sortedParams = new TreeMap<>(params);
        Set<Map.Entry<String, Object>> entrys = sortedParams.entrySet();
        StringBuilder baseString = new StringBuilder();
        for (Map.Entry<String, Object> param : entrys) {
            if (param.getValue() != null && !"".equals(param.getKey().trim()) &&
                    !"sign".equals(param.getKey().trim()) && !"".equals(param.getValue())) {
                baseString.append(param.getKey().trim()).append("=")
                        .append(URLEncoder.encode(param.getValue().toString(), StandardCharsets.UTF_8)).append("&");
            }
        }
        if (baseString.length() > 0) {
            baseString.deleteCharAt(baseString.length() - 1).append("&app_key=")
                    .append(CONFIG);
        }
        try {
            String sign = md5(baseString.toString());
            System.out.println("sign:" + sign.toUpperCase());
            sortedParams.put("sign", sign);

        } catch (Exception ex) {
            throw new IOException(ex);
        }
        return sortedParams;
    }
}
