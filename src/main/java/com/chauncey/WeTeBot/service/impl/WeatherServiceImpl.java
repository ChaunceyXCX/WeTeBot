package com.chauncey.WeTeBot.service.impl;

import com.chauncey.WeTeBot.api.WeatherApi;
import com.chauncey.WeTeBot.model.weather.Forecast;
import com.chauncey.WeTeBot.model.weather.HeWeather6;
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
    private WeatherApi weatherApi;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private HeWeather6 heWeather6;

    @Override
    public Now getWeatherNow(String parameters) throws IOException {
        heWeather6 = weatherApi.weatherNowAPI(parameters);
        log.info(heWeather6.toString());
        return objectMapper.readValue(heWeather6.toString(), Now.class);
    }

    @Override
    public Forecast getWeatherForecast(String parameters) throws IOException {
        heWeather6 = weatherApi.weatherForecastAPI(parameters);
        log.info(heWeather6.toString());
        return objectMapper.readValue(heWeather6.toString(), Forecast.class);
    }

    @Override
    public LifeStyle getWeatherLifeStyle(String parameters) throws IOException {
        heWeather6 = weatherApi.weatherLifestyleAPI(parameters);
        log.info(heWeather6.toString());
        return objectMapper.readValue(heWeather6.toString(), LifeStyle.class);
    }
}
