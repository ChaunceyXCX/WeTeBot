package com.chauncey.WeTeBot.model.weather;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author https://github.com/ChaunceyCX
 * @version 1.0
 * @className LifeStyle
 * @description TODO
 * @date 2019/6/29 上午10:35
 **/
@Data
@Component
public class LifeStyle {
    // 生活指数类型 comf：舒适度指数、cw：洗车指数、drsg：穿衣指数、flu：感冒指数、sport：运动指数、trav：旅游指数、uv：紫外线指数、air：空气污染扩散条件指数、ac：空调开启指数、ag：过敏指数、gl：太阳镜指数、mu：化妆指数、airc：晾晒指数、ptfc：交通指数、fsh：钓鱼指数、spi：防晒指数
    private String type;
    // 生活指数简介
    private String brf;
    // 生活指数详细描述
    private String txt;
}
