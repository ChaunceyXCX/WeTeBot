package com.chauncey.WeTeBot.model.weather;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author https://github.com/ChaunceyCX
 * @version 1.0
 * @className Update
 * @description TODO
 * @date 2019/6/29 上午10:19
 **/
@Component
@Data
public class Update {
    private String loc;
    private String utc;
}
