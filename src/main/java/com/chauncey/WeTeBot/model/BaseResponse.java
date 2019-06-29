package com.chauncey.WeTeBot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author https://github.com/ChaunceyCX
 * @version 1.0
 * @className BaseResponse
 * @description BaseResponse
 * @date 2019/6/27 下午2:59
 **/
@Data
public class BaseResponse {
    @JsonProperty
    int Ret;
    @JsonProperty
    String ErrMsg;
}
