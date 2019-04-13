package com.cherry.WeTeBot.enums;

/**
 * @ClassName MessageType
 * @Description 接收消息类型
 * @Author chauncey
 * @Date 19-4-10 下午6:24
 * @Version 1.0
 **/

public enum MessageType {
    TEXT(1),
    IMAGE(3),
    VOICE(34),
    VIDEO(43),
    MICROVIDEO(62),
    EMOTICON(47),
    APP(49),
    VOIPMSG(50),
    VOIPNOTIFY(52),
    VOIPINVITE(53),
    LOCATION(48),
    //状态通知登录后会发送此消息
    STATUSNOTIFY(51),
    SYSNOTICE(9999),
    POSSIBLEFRIEND_MSG(40),
    VERIFYMSG(37),
    SHARECARD(42),
    SYS(10000),
    RECALLED(10002);

    private final int code;

    MessageType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
