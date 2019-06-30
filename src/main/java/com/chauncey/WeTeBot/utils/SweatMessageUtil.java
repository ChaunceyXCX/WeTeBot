package com.chauncey.WeTeBot.utils;

import com.chauncey.WeTeBot.model.weather.*;
import lombok.extern.log4j.Log4j2;

import java.util.List;

/**
 * @author https://github.com/ChaunceyCX
 * @version 1.0.0
 * @className SweatMessageUtil
 * @description TODO
 * @date 2019/6/30 下午2:51
 **/
@Log4j2
public class SweatMessageUtil {

    public static String sweatMessageForLover(HeWeather6 weather) {
        Now now = weather.getNow();
        List<LifeStyle> lifeStyle = weather.getLifestyle();
        List<Forecast> forecast = weather.getDaily_forecast();
        Basic basic = weather.getBasic();
        Update update = weather.getUpdate();
        Forecast forecastToDay = forecast.get(0);
        String loverMessage = "早上好: 主人的豆奶奶, " + basic.getLocation() + ":\n"
                + "\n"
                + " 今天的天气是: " + forecastToDay.getCond_txt_d() + " 转 " + forecastToDay.getCond_txt_n() + "\n"
                + " 今天的温度是: " + forecastToDay.getTmp_min() + "°C~" + forecastToDay.getTmp_max() + "°C" + "\n"
                + " 风向: " + forecastToDay.getWind_dir() + " " + forecastToDay.getWind_sc() + "级" + forecastToDay.getWind_spd() + "公里/小时" + "\n"
                + " 相对湿度: " + forecastToDay.getHum() + '%' + "\n"
                + " 降水量: " + forecastToDay.getPcpn() + "ml" + "   降水概率: " + forecastToDay.getPop() + '%' + "\n"
                + (Integer.valueOf(forecastToDay.getPop()) > 0 ? "------------------------------------------\n  今天可能会下雨,别再忘了带伞了,大猪头\n------------------------------------------" : "")
                + " 能见度: " + forecastToDay.getVis() + "公里" + "\n"
                + "------------------------------------------" + "\n"
                + " 当前气温: " + now.getTmp() + "°C" + "  体感温度: " + now.getFl() + "°C" + "\n"
                + "记得适量增减衣物哦！！！！"
                + "------------------------------------------\n"
                + " 今天的生活指数: \n"
                + "1、" + lifeStyle.get(0).getTxt() + "\n\n"
                + "2、" + lifeStyle.get(1).getTxt() + "\n\n"
                + "3、" + lifeStyle.get(2).getTxt() + "\n\n"
                + "😄😊😉😍😘😚😜😝😳😁" + "\n\n";
        return loverMessage;
    }
}
