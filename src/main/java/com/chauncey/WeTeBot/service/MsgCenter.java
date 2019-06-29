package com.chauncey.WeTeBot.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chauncey.WeTeBot.api.MessageTools;
import com.chauncey.WeTeBot.enums.MsgCodeEnum;
import com.chauncey.WeTeBot.enums.MsgTypeEnum;
import com.chauncey.WeTeBot.model.BaseMsg;
import com.chauncey.WeTeBot.model.Core;
import com.chauncey.WeTeBot.utils.tools.CommonTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;

/**
 * @Author https://github.com/ChaunceyCX
 * @Description //消息处理中心
 * @Date 下午3:57 2019/6/22
 * @Param
 * @return
 **/
public class MsgCenter {
    private static Logger LOG = LoggerFactory.getLogger(MsgCenter.class);

    private static Core core = Core.getInstance();

    /**
     * @return com.alibaba.fastjson.JSONArray
     * @Author https://github.com/ChaunceyCX
     * @Description //接收消息，放入队列
     * @Date 下午3:58 2019/6/22
     * @Param [msgList]
     **/
    public static JSONArray produceMsg(JSONArray msgList) {
        JSONArray result = new JSONArray();
        for (int i = 0; i < msgList.size(); i++) {
            JSONObject msg = new JSONObject();
            JSONObject m = msgList.getJSONObject(i);
            m.put("groupMsg", false);// 是否是群消息
            if (m.getString("FromUserName").contains("@@") || m.getString("ToUserName").contains("@@")) { // 群聊消息
                if (m.getString("FromUserName").contains("@@")
                        && !core.getGroupIdList().contains(m.getString("FromUserName"))) {
                    core.getGroupIdList().add((m.getString("FromUserName")));
                } else if (m.getString("ToUserName").contains("@@")
                        && !core.getGroupIdList().contains(m.getString("ToUserName"))) {
                    core.getGroupIdList().add((m.getString("ToUserName")));
                }
                // 群消息与普通消息不同的是在其消息体（Content）中会包含发送者id及":<br/>"消息，这里需要处理一下，去掉多余信息，只保留消息内容
                if (m.getString("Content").contains("<br/>")) {
                    String content = m.getString("Content").substring(m.getString("Content").indexOf("<br/>") + 5);
                    m.put("Content", content);
                    m.put("groupMsg", true);
                }
            } else {
                CommonTools.msgFormatter(m, "Content");
            }
            if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_TEXT.getCode())) { // words
                // 文本消息
                if (m.getString("Url").length() != 0) {
                    String regEx = "(.+?\\(.+?\\))";
                    Matcher matcher = CommonTools.getMatcher(regEx, m.getString("Content"));
                    String data = "Map";
                    if (matcher.find()) {
                        data = matcher.group(1);
                    }
                    msg.put("Type", "Map");
                    msg.put("Text", data);
                } else {
                    msg.put("Type", MsgTypeEnum.TEXT.getType());
                    msg.put("Text", m.getString("Content"));
                }
                m.put("Type", msg.getString("Type"));
                m.put("Text", msg.getString("Text"));
            } else if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_IMAGE.getCode())
                    || m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_EMOTICON.getCode())) { // 图片消息
                m.put("Type", MsgTypeEnum.PIC.getType());
            } else if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_VOICE.getCode())) { // 语音消息
                m.put("Type", MsgTypeEnum.VOICE.getType());
            } else if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_VERIFYMSG.getCode())) {// friends
                // 好友确认消息
                // MessageTools.addFriend(core, userName, 3, ticket); // 确认添加好友
                m.put("Type", MsgTypeEnum.VERIFYMSG.getType());

            } else if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_SHARECARD.getCode())) { // 共享名片
                m.put("Type", MsgTypeEnum.NAMECARD.getType());

            } else if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_VIDEO.getCode())
                    || m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_MICROVIDEO.getCode())) {// viedo
                m.put("Type", MsgTypeEnum.VIEDO.getType());
            } else if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_MEDIA.getCode())) { // 多媒体消息
                m.put("Type", MsgTypeEnum.MEDIA.getType());
            } else if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_STATUSNOTIFY.getCode())) {// phone
                // init
                // 微信初始化消息

            } else if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_SYS.getCode())) {// 系统消息
                m.put("Type", MsgTypeEnum.SYS.getType());
            } else if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_RECALLED.getCode())) { // 撤回消息

            } else {
                LOG.info("Useless msg");
            }
            LOG.info("收到消息一条，来自: " + m.getString("FromUserName"));
            result.add(m);
        }
        return result;
    }

    /**
     * @return void
     * @Author https://github.com/ChaunceyCX
     * @Description //消息处理
     * @Date 下午3:58 2019/6/22
     * @Param [msgHandler]
     **/
    public static void handleMsg(IMsgHandlerService msgHandler) {
        while (true) {
            if (core.getMsgList().size() > 0 && core.getMsgList().get(0).getContent() != null) {
                if (core.getMsgList().get(0).getContent().length() > 0) {
                    BaseMsg msg = core.getMsgList().get(0);
                    if (msg.getType() != null) {
                        try {
                            if (msg.getType().equals(MsgTypeEnum.TEXT.getType())) {
                                String result = msgHandler.textMsgHandle(msg);
                                MessageTools.sendMsgById(result, core.getMsgList().get(0).getFromUserName());
                            } else if (msg.getType().equals(MsgTypeEnum.PIC.getType())) {

                                String result = msgHandler.picMsgHandle(msg);
                                MessageTools.sendMsgById(result, core.getMsgList().get(0).getFromUserName());
                            } else if (msg.getType().equals(MsgTypeEnum.VOICE.getType())) {
                                String result = msgHandler.voiceMsgHandle(msg);
                                MessageTools.sendMsgById(result, core.getMsgList().get(0).getFromUserName());
                            } else if (msg.getType().equals(MsgTypeEnum.VIEDO.getType())) {
                                String result = msgHandler.viedoMsgHandle(msg);
                                MessageTools.sendMsgById(result, core.getMsgList().get(0).getFromUserName());
                            } else if (msg.getType().equals(MsgTypeEnum.NAMECARD.getType())) {
                                String result = msgHandler.nameCardMsgHandle(msg);
                                MessageTools.sendMsgById(result, core.getMsgList().get(0).getFromUserName());
                            } else if (msg.getType().equals(MsgTypeEnum.SYS.getType())) { // 系统消息
                                msgHandler.sysMsgHandle(msg);
                            } else if (msg.getType().equals(MsgTypeEnum.VERIFYMSG.getType())) { // 确认添加好友消息
                                String result = msgHandler.verifyAddFriendMsgHandle(msg);
                                MessageTools.sendMsgById(result,
                                        core.getMsgList().get(0).getRecommendInfo().getUserName());
                            } else if (msg.getType().equals(MsgTypeEnum.MEDIA.getType())) { // 多媒体消息
                                String result = msgHandler.mediaMsgHandle(msg);
                                MessageTools.sendMsgById(result, core.getMsgList().get(0).getFromUserName());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                core.getMsgList().remove(0);
            }
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}