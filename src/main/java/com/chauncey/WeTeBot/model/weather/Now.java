package com.chauncey.WeTeBot.model.weather;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author https://github.com/ChaunceyCX
 * @version 1.0
 * @className Now
 * @description 实况天气
 * @date 2019/6/29 上午10:27
 **/
@Data
@Component
public class Now {
    // 云量
    private String cloud;
    // 实况天气状况代码
    private String cond_code;
    // 实况天气状况描述
    private String cond_txt;
    // 体感温度，默认单位：摄氏度
    private String fl;
    // 相对湿度
    private String hum;
    // 降水量
    private String pcpn;
    // 大气压强
    private String pres;
    // 温度，默认单位：摄氏度
    private String tmp;
    // 能见度，默认单位：公里
    private String vis;
    // 风向360角度
    private String wind_deg;
    // 风向
    private String wind_dir;
    // 风力
    private String wind_sc;
    // 风速，公里/小时
    private String wind_spd;
}
