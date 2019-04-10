package com.cherry.WeTeBot.domain.response;

import com.cherry.WeTeBot.domain.response.component.WechatHttpResponseBase;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpLogResponse extends WechatHttpResponseBase {
}