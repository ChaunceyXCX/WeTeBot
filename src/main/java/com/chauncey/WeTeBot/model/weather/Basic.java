package com.chauncey.WeTeBot.model.weather;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author https://github.com/ChaunceyCX
 * @version 1.0
 * @className Basic
 * @description TODO
 * @date 2019/6/29 上午10:15
 **/
@Component
@Data
public class Basic {
    // 地区／城市ID
    private String cid;
    // 地区／城市名称
    private String location;
    // 该地区／城市的上级城市
    private String parent_city;
    // 该地区／城市所属行政区域
    private String admin_area;
    // 该地区／城市所属国家名称
    private String cnty;
    // 地区／城市纬度
    private String lat;
    // 地区／城市经度
    private String lon;
    // 该地区／城市所在时区
    private String tz;
}
