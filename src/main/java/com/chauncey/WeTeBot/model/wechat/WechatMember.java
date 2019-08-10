package com.chauncey.WeTeBot.model.wechat;

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
public class WechatMember {
    @Id
    @GeneratedValue
    private long id;
    @JsonProperty(value = "Uin")
    private long Uin;
    @JsonProperty(value = "UserName")
    private String userName;
    @JsonProperty(value = "NickName")
    private String nickName;
    @JsonProperty(value = "HeadImgURL")
    private String headImgURL;
    @JsonProperty(value = "ContactFlag")
    private long contactFlag;
    @JsonProperty(value = "MemberCount")
    private long memberCount;
    /*@JsonProperty
    private Object[] MemberList;*/
    @JsonProperty(value = "RemarkName")
    private String remarkName;
    @JsonProperty(value = "HideInputBarFlag")
    private long hideInputBarFlag;
    @JsonProperty(value = "Sex")
    private long sex;
    @JsonProperty(value = "Signature")
    private String signature;
    @JsonProperty(value = "VerifyFlag")
    private long verifyFlag;
    @JsonProperty(value = "OwnerUin")
    private long ownerUin;
    @JsonProperty(value = "PyInitial")
    private String pyInitial;
    @JsonProperty(value = "PyQuanPin")
    private String pyQuanPin;
    @JsonProperty(value = "RemarkPYInitial")
    private String remarkPYInitial;
    @JsonProperty(value = "RemarkPYQuanPin")
    private String remarkPYQuanPin;
    @JsonProperty(value = "StarFriend")
    private long starFriend;
    @JsonProperty(value = "AppAccountFlag")
    private long appAccountFlag;
    @JsonProperty(value = "Statues")
    private long statues;
    @JsonProperty(value = "AttrStatus")
    private long attrStatus;
    @JsonProperty(value = "Province")
    private String province;
    @JsonProperty(value = "City")
    private String city;
    @JsonProperty(value = "Alias")
    private String alias;
    @JsonProperty(value = "SnsFlag")
    private long snsFlag;
    @JsonProperty(value = "UniFriend")
    private long uniFriend;
    @JsonProperty(value = "DisplayName")
    private String displayName;
    @JsonProperty(value = "ChatRoomId")
    private long chatRoomId;
    @JsonProperty(value = "KeyWord")
    private String keyWord;
    @JsonProperty(value = "EncryChatRoomID")
    private String encryChatRoomID;
    @JsonProperty(value = "IsOwner")
    private long isOwner;
    private int flag;

    private boolean receive;
}
