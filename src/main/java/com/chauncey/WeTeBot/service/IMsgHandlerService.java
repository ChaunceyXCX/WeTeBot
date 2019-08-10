package com.chauncey.WeTeBot.service;

import com.chauncey.WeTeBot.model.wechat.BaseMsg;

/**
 * @author https://github.com/ChaunceyCX
 * @description 消息处理接口
 * @date 下午2:46 2019/6/29
 **/
public interface IMsgHandlerService {

    /**
     * @param msg
     * @return java.lang.String
     * @author https://github.com/ChaunceyCX
     * @description 文本消息
     * @date 下午2:46 2019/6/29
     **/
    String textMsgHandle(BaseMsg msg);

    /**
     * @param msg
     * @return java.lang.String
     * @author https://github.com/ChaunceyCX
     * @description 处理图片消息
     * @date 下午2:46 2019/6/29
     **/
    String picMsgHandle(BaseMsg msg);

    /**
     * @param msg
     * @return java.lang.String
     * @author https://github.com/ChaunceyCX
     * @description 处理声音消息
     * @date 下午2:47 2019/6/29
     **/
    String voiceMsgHandle(BaseMsg msg);

    /**
     * @param msg
     * @return java.lang.String
     * @author https://github.com/ChaunceyCX
     * @description 处理小视频消息
     * @date 下午2:47 2019/6/29
     **/
    String viedoMsgHandle(BaseMsg msg);

    /**
     * @param msg
     * @return java.lang.String
     * @author https://github.com/ChaunceyCX
     * @description 处理名片消息
     * @date 下午2:47 2019/6/29
     **/
    String nameCardMsgHandle(BaseMsg msg);

    /**
     * @param msg
     * @return void
     * @author https://github.com/ChaunceyCX
     * @description 处理系统消息
     * @date 下午2:47 2019/6/29
     **/
    void sysMsgHandle(BaseMsg msg);

    /**
     * @param msg
     * @return java.lang.String
     * @author https://github.com/ChaunceyCX
     * @description 处理确认添加好友消息
     * @date 下午2:48 2019/6/29
     **/
    String verifyAddFriendMsgHandle(BaseMsg msg);

    /**
     * @param msg
     * @return java.lang.String
     * @author https://github.com/ChaunceyCX
     * @description 处理收到的文件消息
     * @date 下午2:53 2019/6/29
     **/
    String mediaMsgHandle(BaseMsg msg);

}
