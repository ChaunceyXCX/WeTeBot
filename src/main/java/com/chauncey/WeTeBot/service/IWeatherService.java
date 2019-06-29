package com.chauncey.WeTeBot.service;

import com.chauncey.WeTeBot.model.weather.Forecast;
import com.chauncey.WeTeBot.model.weather.LifeStyle;
import com.chauncey.WeTeBot.model.weather.Now;

import java.io.IOException;

/**
 * @ClassName IWeatherService
 * @Description TODO
 * @Author https://github.com/ChaunceyCX
 * @Date 2019/6/29 上午10:37
 * @Version 1.0
 **/
public interface IWeatherService {
    public Now getWeatherNow(String parameters) throws IOException;

    public Forecast getWeatherForecast(String parameters) throws IOException;

    public LifeStyle getWeatherLifeStyle(String parameters) throws IOException;
}
