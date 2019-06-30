package com.chauncey.WeTeBot.model.weather;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author https://github.com/ChaunceyCX
 * @version 1.0
 * @className Foreast
 * @description 天气预报
 * @date 2019/6/29 上午10:30
 **/
@Data
@Component
public class Forecast {
    // 白天天气状况代码
    private String cond_code_d;
    // 夜间天气状况代码
    private String cond_code_n;
    // 白天天气状况描述
    private String cond_txt_d;
    // 晚间天气状况描述
    private String cond_txt_n;
    // 预报日期
    private String date;
    // 相对湿度
    private String hum;
    // 降水量
    private String pcpn;
    // 降水概率
    private String pop;
    // 大气压强
    private String pres;
    // 日出时间
    private String sr;
    // 日落时间
    private String ss;
    // 最高温度
    private String tmp_max;
    // 最低温度
    private String tmp_min;
    // 紫外线强度指数
    private String uv_index;
    // 能见度，单位：公里
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
