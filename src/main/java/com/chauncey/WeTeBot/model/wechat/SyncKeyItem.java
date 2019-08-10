package com.chauncey.WeTeBot.model.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author https://github.com/ChaunceyCX
 * @version 1.0.0
 * @className SyncKeyItem
 * @description TODO
 * @date 2019/6/29 下午7:12
 **/
@Data
public class SyncKeyItem {
    @JsonProperty(value = "Val")
    private Long val;
    @JsonProperty(value = "Key")
    private Long key;
}
