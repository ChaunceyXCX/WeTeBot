package com.chauncey.WeTeBot.model.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author https://github.com/ChaunceyCX
 * @version 1.0
 * @className User
 * @description TODO
 * @date 2019/6/28 下午5:43
 **/
@Data
public class User {
    @JsonProperty
    private long Uin;
    @JsonProperty
    private String UserName;
    @JsonProperty
    private String NickName;
    @JsonProperty
    private String HeadImgURL;
    @JsonProperty
    private String RemarkName;
    @JsonProperty
    private String PyInitial;
    @JsonProperty
    private String PyQuanPin;
    @JsonProperty
    private String RemarkPYInitial;
    @JsonProperty
    private String RemarkPYQuanPin;
    @JsonProperty
    private long HideInputBarFlag;
    @JsonProperty
    private long StarFriend;
    @JsonProperty
    private long Sex;
    @JsonProperty
    private String Signature;
    @JsonProperty
    private long AppAccountFlag;
    @JsonProperty
    private long VerifyFlag;
    @JsonProperty
    private long ContactFlag;
    @JsonProperty
    private long WebWxPluginSwitch;
    @JsonProperty
    private long HeadImgFlag;
    @JsonProperty
    private long SnsFlag;
}
