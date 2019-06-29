package com.chauncey.WeTeBot.enums;

/**
 * @author https://github.com/ChaunceyCX
 * @description 确认添加好友Enum
 * @date 下午2:51 2019/6/29
 **/
public enum VerifyFriendEnum {

    ADD(2, "添加"),
    ACCEPT(3, "接受");

    private int code;
    private String desc;

    private VerifyFriendEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

}
