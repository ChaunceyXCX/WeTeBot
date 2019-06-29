package com.chauncey.WeTeBot.service.impl;

import com.chauncey.WeTeBot.api.WeatherTools;
import com.chauncey.WeTeBot.model.weather.Forecast;
import com.chauncey.WeTeBot.model.weather.LifeStyle;
import com.chauncey.WeTeBot.model.weather.Now;
import com.chauncey.WeTeBot.service.IWeatherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @ClassName WeatherServiceImpl
 * @Description TODO
 * @Author https://github.com/ChaunceyCX
 * @Date 2019/6/29 上午10:59
 * @Version 1.0
 **/
@Service
@Log4j2
public class WeatherServiceImpl implements IWeatherService {
    @Autowired
    private WeatherTools weatherTools;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Now getWeatherNow(String parameters) throws IOException {
        String nowStr = weatherTools.weatherNowAPI(parameters);
        log.info(nowStr);
        return objectMapper.readValue(nowStr, Now.class);
    }

    @Override
    public Forecast getWeatherForecast(String parameters) throws IOException {
        String forecactStr = weatherTools.weatherForecastAPI(parameters);
        log.info(forecactStr);
        return objectMapper.readValue(forecactStr, Forecast.class);
    }

    @Override
    public LifeStyle getWeatherLifeStyle(String parameters) throws IOException {
        String lifeStyleStr = weatherTools.weatherLifestyleAPI(parameters);
        log.info(lifeStyleStr);
        return objectMapper.readValue(lifeStyleStr, LifeStyle.class);
    }
}
