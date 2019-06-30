package com.chauncey.WeTeBot.model.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author https://github.com/ChaunceyCX
 * @version 1.0.0
 * @className Weather
 * @description TODO
 * @date 2019/6/30 下午3:42
 **/
@Data
@Component
public class Weather {
    @JsonProperty(value = "HeWeather6")
    private List<HeWeather6> heWeather6;
}
