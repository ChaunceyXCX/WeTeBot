package com.chauncey.WeTeBot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @ClassName Member
 * @Description TODO
 * @Author https://github.com/ChaunceyCX
 * @Date 2019/6/27 下午3:12
 * @Version 1.0
 **/
@Entity
@Data
public class Member {
    @Id
    @GeneratedValue
    private long Id;
    @JsonProperty
    private long Uin;
    @JsonProperty
    private String UserName;
    @JsonProperty
    private String NickName;
    @JsonProperty
    private String HeadImgURL;
    @JsonProperty
    private long ContactFlag;
    @JsonProperty
    private long MemberCount;
    /*@JsonProperty
    private Object[] MemberList;*/
    @JsonProperty
    private String RemarkName;
    @JsonProperty
    private long HideInputBarFlag;
    @JsonProperty
    private long Sex;
    @JsonProperty
    private String Signature;
    @JsonProperty
    private long VerifyFlag;
    @JsonProperty
    private long OwnerUin;
    @JsonProperty
    private String PyInitial;
    @JsonProperty
    private String PyQuanPin;
    @JsonProperty
    private String RemarkPYInitial;
    @JsonProperty
    private String RemarkPYQuanPin;
    @JsonProperty
    private long StarFriend;
    @JsonProperty
    private long AppAccountFlag;
    @JsonProperty
    private long Statues;
    @JsonProperty
    private long AttrStatus;
    @JsonProperty
    private String Province;
    @JsonProperty
    private String City;
    @JsonProperty
    private String Alias;
    @JsonProperty
    private long SnsFlag;
    @JsonProperty
    private long UniFriend;
    @JsonProperty
    private String DisplayName;
    @JsonProperty
    private long ChatRoomId;
    @JsonProperty
    private String KeyWord;
    @JsonProperty
    private String EncryChatRoomID;
    @JsonProperty
    private long IsOwner;

    private int flag;
}
