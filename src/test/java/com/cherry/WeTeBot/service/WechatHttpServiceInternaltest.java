package com.cherry.WeTeBot.service;

import com.cherry.WeTeBot.component.WeChat;
import com.cherry.WeTeBot.domain.response.FileUploadResponse;
import com.cherry.WeTeBot.utils.HeaderUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @ClassName WechatHttpServiceInternal
 * @Description TODO
 * @Author https://github.com/ChaunceyCX
 * @Date 19-4-28 上午11:58
 * @Version 1.0
 **/
public class WechatHttpServiceInternaltest {

    @Value("${wechat.url.upload_media}")
    private String WECHAT_URL_UPLOAD_MEDIA;
    @Autowired
    WechatHttpService wechatHttpService = new WechatHttpService();

    private final Logger logger =  LoggerFactory.getLogger(WechatHttpServiceInternaltest.class);
    /*WebClient webClient = WebClient.builder()
            .defaultHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36")
            .defaultHeader("Refer","https://wx.qq.com/?&lang=zh_CN")
            .defaultHeader(HttpHeaders.ORIGIN,"https://wx.qq.com/")
            .build();*/

/*    WebClient webClient = WebClient.builder()
            .baseUrl("https://api.github.com")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/vnd.github.v3+json")
            .defaultHeader(HttpHeaders.USER_AGENT, "Spring 5 WebClient")
            .build();*/

    @Test
    public void testOptions(){

        WebClient webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36")
                .defaultHeader("Refer","https://wx.qq.com/?&lang=zh_CN")
                .defaultHeader(HttpHeaders.ORIGIN,"https://wx.qq.com/")
                .build();
        // final String url = WECHAT_URL_UPLOAD_MEDIA;
        final String url = "https://file.wx.qq.com/cgi-bin/mmwebwx-bin/webwxuploadmedia?f=json";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccessControlRequestMethod(HttpMethod.POST);
        Mono<String> resp = webClient.options()
                .uri(url,"json")
                .retrieve()
                .bodyToMono(String.class);
        String fileUploadResponse = resp.block();
        logger.info(fileUploadResponse);
        // wechatHttpService.fileUpload(new WeChat(),"xx",true);
    }

    @Test
    public void testOpen(){

        HttpHeaders customHeader = new HttpHeaders();
        customHeader.setPragma("no-cache");
        customHeader.setCacheControl("no-cache");
        customHeader.set("Upgrade-Insecure-Requests", "1");
        customHeader.set(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        HttpHeaders getHeader = new HttpHeaders();

        getHeader.set(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
        getHeader.set(HttpHeaders.ACCEPT_LANGUAGE, "en,zh-CN;q=0.8,zh;q=0.6,ja;q=0.4,zh-TW;q=0.2");
        getHeader.set(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, br");
        HeaderUtils.assign(customHeader, getHeader);
        Mono<String> resp = WebClient.create().get()
                .uri("https://wx.qq.com/")
                .retrieve()
                .bodyToMono(String.class);
        String ss = resp.block();
        logger.info(ss);
    }





}
