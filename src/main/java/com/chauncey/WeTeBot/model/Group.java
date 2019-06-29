package com.chauncey.WeTeBot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

/**
 * @ClassName Group
 * @Description TODO
 * @Author https://github.com/ChaunceyCX
 * @Date 2019/6/27 下午3:19
 * @Version 1.0
 **/
@Data
//@Entity
public class Group {
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
    //    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonProperty
    private List<GroupUser> MemberList;
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
    private long ChatRoomID;
    @JsonProperty
    private String KeyWord;
    @JsonProperty
    private String EncryChatRoomID;
    @JsonProperty
    private long IsOwner;
}
