package com.chauncey.WeTeBot.api;

import com.chauncey.WeTeBot.model.weather.Forecast;
import com.chauncey.WeTeBot.model.weather.HeWeather6;
import com.chauncey.WeTeBot.model.weather.Now;
import com.chauncey.WeTeBot.model.weather.Weather;
import com.chauncey.WeTeBot.utils.SweatMessageUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @ClassName WeatherTools
 * @Description TODO
 * @Author https://github.com/ChaunceyCX
 * @Date 2019/6/29 上午10:41
 * @Version 1.0
 **/
@Component
@Log4j2
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
    @Value("${weather.location.default}")
    private String wetherLocationDefault;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private Weather weather;

    public String batchWeather(String params) {
        String weatherStr = "";
        try {
            HeWeather6 heWeather6 = weatherLifestyleAPI(params);
            Now now = weatherNowAPI(params).getNow();
            List<Forecast> forecast = weatherForecastAPI(params).getDaily_forecast();
            heWeather6.setNow(now);
            heWeather6.setDaily_forecast(forecast);
            weatherStr = SweatMessageUtil.sweatMessageForLover(heWeather6);
            log.info(weatherStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weatherStr;
    }

    public HeWeather6 weatherNowAPI(String params) throws IOException {
        return weatherAPI(weatherTypeNow, params);
    }


    public HeWeather6 weatherForecastAPI(String params) throws IOException {
        return weatherAPI(weatherTypeForecast, params);
    }


    public HeWeather6 weatherLifestyleAPI(String params) throws IOException {
        return weatherAPI(weatherTypeLifestyle, params);
    }

    HeWeather6 weatherAPI(String weatherType, String params) {
        String url = String.format(weatherUrl, weatherType, getParam(params));
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String weatherStr = response.body().string();
            weather = objectMapper.readValue(weatherStr, Weather.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weather.getHeWeather6().get(0);
    }

    String getParam(String params) {
        if (params == null || params.equals("")) {
            return wetherLocationDefault;
        }
        return params;
    }
}
