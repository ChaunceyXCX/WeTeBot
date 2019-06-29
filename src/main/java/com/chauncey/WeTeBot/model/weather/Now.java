package com.chauncey.WeTeBot.model.weather;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author https://github.com/ChaunceyCX
 * @version 1.0
 * @className Now
 * @description TODO
 * @date 2019/6/29 上午10:27
 **/
@Data
@Component
public class Now {
    private String cloud;
    private String cond_code;
    private String cond_txt;
    private String fl;
    private String hum;
    private String pcpn;
    private String pres;
    private String tmp;
    private String vis;
    private String wind_deg;
    private String wind_dir;
    private String wind_sc;
    private String wind_spd;
}
