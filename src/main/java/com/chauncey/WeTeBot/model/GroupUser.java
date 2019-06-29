package com.chauncey.WeTeBot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * @author https://github.com/ChaunceyCX
 * @version 1.0
 * @className GroupUser
 * @description TODO
 * @date 2019/6/28 下午6:04
 **/
@Data
//@Entity
public class GroupUser {
    @Id
    @GeneratedValue
    @Column(name = "group_user_id")
    private long Id;
    @JsonProperty
    private long Uin;
    @JsonProperty
    private String UserName;
    @JsonProperty
    private String NickName;
    @JsonProperty
    private long AttrStatus;
    @JsonProperty
    private String PyInitial;
    @JsonProperty
    private String PyQuanPin;
    @JsonProperty
    private String RemarkPYInitial;
    @JsonProperty
    private String RemarkPYQuanPin;
    @JsonProperty
    private long MemberStatus;
    @JsonProperty
    private String DisplayName;
    @JsonProperty
    private String KeyWord;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
    @JoinColumn(name = "group_id")
    private Group group;
}
