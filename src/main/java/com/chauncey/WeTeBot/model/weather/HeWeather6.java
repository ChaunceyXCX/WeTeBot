package com.chauncey.WeTeBot.model.weather;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author https://github.com/ChaunceyCX
 * @version 1.0.0
 * @className HeWeather6
 * @description TODO
 * @date 2019/6/30 下午2:53
 **/
@Data
@Component
public class HeWeather6 {
    private Basic basic;
    private Update update;
    private String status;
    private Now now;
    private List<Forecast> daily_forecast;
    private List<LifeStyle> lifestyle;
}
