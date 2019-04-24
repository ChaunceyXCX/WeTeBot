package com.cherry.WeTeBot.enums;

public enum SpecialContact {

    FILE_HELPER("filehelper"),
    NEWSAPP("newsapp"),
    RECOMMEND_HELP("fmessage");

    private final String code;

    public String getCode() {
        return code;
    }

    SpecialContact(String code) {
        this.code = code;
    }

    /*'newsapp', 'fmessage', 'filehelper', 'weibo', 'qqmail',
            'fmessage', 'tmessage', 'qmessage', 'qqsync', 'floatbottle',
            'lbsapp', 'shakeapp', 'medianote', 'qqfriend', 'readerapp',
            'blogapp', 'facebookapp', 'masssendapp', 'meishiapp',
            'feedsapp', 'voip', 'blogappweixin', 'weixin', 'brandsessionholder',
            'weixinreminder', 'wxid_novlwrv3lqwv11', 'gh_22b87fa7cb3c',
            'officialaccounts', 'notification_messages', 'wxid_novlwrv3lqwv11',
            'gh_22b87fa7cb3c', 'wxitil', 'userexperience_alarm', 'notification_messages'*/

}
