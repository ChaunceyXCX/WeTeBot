package com.cherry.WeTeBot.enums;

public enum RetCode {
    NORMAL(0),
    // 微信客户端登出(app点击退出网页微信)
    LOGOUT1(1100),
    // 从其它设备上登了网页微信
    LOGOUT2(1101),

    LOGOUT3(1102);

    private final int code;

    RetCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
