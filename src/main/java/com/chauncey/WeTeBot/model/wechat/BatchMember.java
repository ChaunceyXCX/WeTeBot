package com.chauncey.WeTeBot.model.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName BatchMember
 * @Description TODO
 * @Author https://github.com/ChaunceyCX
 * @Date 2019/6/27 下午4:34
 * @Version 1.0
 **/
@Data
@Component
public class BatchMember {
    @JsonProperty
    private BaseResponse BaseResponse;
    @JsonProperty
    private long MemberCount;
    @JsonProperty
    private List<WechatMember> MemberList;
    @JsonProperty
    private long Seq;
}
