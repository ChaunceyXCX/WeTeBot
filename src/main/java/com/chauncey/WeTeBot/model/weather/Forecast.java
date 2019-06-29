package com.chauncey.WeTeBot.model.weather;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author https://github.com/ChaunceyCX
 * @version 1.0
 * @className Foreast
 * @description TODO
 * @date 2019/6/29 上午10:30
 **/
@Data
@Component
public class Forecast {
    private String cond_code_d;
    private String cond_code_n;
    private String cond_txt_d;
    private String cond_txt_n;
    private String date;
    private String hum;
    private String pcpn;
    private String pop;
    private String pres;
    private String sr;
    private String ss;
    private String tmp_max;
    private String tmp_min;
    private String uv_index;
    private String vis;
    private String wind_deg;
    private String wind_dir;
    private String wind_sc;
    private String wind_spd;
}
