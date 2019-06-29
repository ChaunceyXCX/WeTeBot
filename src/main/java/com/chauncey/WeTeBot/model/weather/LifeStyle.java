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
    private String type;
    private String brf;
    private String txt;
}
