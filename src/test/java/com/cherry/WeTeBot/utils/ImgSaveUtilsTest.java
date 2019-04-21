package com.cherry.WeTeBot.utils;

import com.cherry.WeTeBot.service.WechatHttpService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/**
 * @ClassName ImgSaveUtilsTest
 * @Description TODO
 * @Author https://github.com/ChaunceyCX
 * @Date 19-4-20 下午5:32
 * @Version 1.0
 **/
public class ImgSaveUtilsTest {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    BufferedImage bufferedImage;
    @Autowired
    WechatHttpService wechatHttpService;

    @Test
    public void getImgInputStream(){
        String url = "https://chaunceycx.github.io/Chauncey.github.io/img/19%E5%B9%B43%E6%9C%8827%E6%97%A5-21%E6%97%B638%E5%88%86.png";
        HttpHeaders customHeader = new HttpHeaders();
        customHeader.set("Accept", "image/webp,image/apng,image/*,*/*;q=0.8");
        ResponseEntity<Resource> responseEntity = restTemplate.exchange(url,HttpMethod.GET,new HttpEntity<>(customHeader),Resource.class);
        try {
            bufferedImage = ImageIO.read(responseEntity.getBody().getInputStream());
            ImageIO.write(bufferedImage,"png",new File("./test.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void getDown() throws IOException {
        String strUrl = "https://chaunceycx.github.io/Chauncey.github.io/img/19%E5%B9%B43%E6%9C%8827%E6%97%A5-21%E6%97%B638%E5%88%86.png";

        URL url = new URL(strUrl);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        try {
            conn.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        conn.setConnectTimeout(5 * 1000);
        try {
            InputStream inStream = conn.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        byte[] data = wechatHttpService.downloadImage("https://chaunceycx.github.io/Chauncey.github.io/img/19%E5%B9%B43%E6%9C%8827%E6%97%A5-21%E6%97%B638%E5%88%86.png");
        System.out.printf("xxx");
    }

    @Test
    public void fileCheck(){
        ImgSaveUtils imgSaveUtils = new ImgSaveUtils();
        System.out.println(imgSaveUtils.isPathExis("@xxx"));
        System.out.println("...............");
        System.out.println(imgSaveUtils.isPathExis("@xxx"));

    }
}
