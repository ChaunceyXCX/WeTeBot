package com.chauncey.WeTeBot.api;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @ClassName WeatherTools
 * @Description TODO
 * @Author https://github.com/ChaunceyCX
 * @Date 2019/6/29 上午10:41
 * @Version 1.0
 **/
@Component
public class WeatherTools {
    private OkHttpClient client = new OkHttpClient();
    @Value("${weather.url}")
    private String weatherUrl;
    @Value("${weather.type.now}")
    private String weatherTypeNow;
    @Value("${weather.type.forecast}")
    private String weatherTypeForecast;
    @Value("${weather.type.hourly}")
    private String weatherTypeHourly;
    @Value("${weather.type.lifestyle}")
    private String weatherTypeLifestyle;

    public String weatherNowAPI(String params) throws IOException {
        String url = String.format(weatherUrl, weatherTypeNow, params);
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }


    public String weatherForecastAPI(String params) throws IOException {
        String url = String.format(weatherUrl, weatherTypeForecast, params);
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }


    public String weatherLifestyleAPI(String params) throws IOException {
        String url = String.format(weatherUrl, weatherTypeLifestyle, params);
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
