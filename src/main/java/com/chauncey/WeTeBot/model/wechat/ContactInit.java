package com.chauncey.WeTeBot.model.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName ContactInit
 * @Description TODO
 * @Author https://github.com/ChaunceyCX
 * @Date 2019/6/28 下午6:43
 * @Version 1.0
 **/
@Data
@Component
public class ContactInit {
    @JsonProperty
    private BaseResponse BaseResponse;
    @JsonProperty
    private long Count;
    //会话列表
    @JsonProperty
    private List<WechatMember> ContactList;
    @JsonProperty
    private SyncKey SyncKey;
    //当前登录用户信息
    @JsonProperty
    private User User;
    //所有会话列表用户名字符串
    @JsonProperty
    private String ChatSet;
    @JsonProperty
    private String SKey;
    @JsonProperty
    private long ClientVersion;
    @JsonProperty
    private long SystemTime;
    @JsonProperty
    private long GrayScale;
    @JsonProperty
    private long InviteStartCount;
    //公众号未读消息数
    @JsonProperty
    private long MPSubscribeMsgCount;
    //公众号未读消息列表
    @JsonProperty
    private List<Object> MPSubscribeMsgList;
    @JsonProperty
    private long ClickReportInterval;
}
