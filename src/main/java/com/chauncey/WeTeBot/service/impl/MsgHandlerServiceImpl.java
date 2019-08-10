package com.chauncey.WeTeBot.service.impl;

import com.chauncey.WeTeBot.api.AiChatApi;
import com.chauncey.WeTeBot.enums.MsgTypeEnum;
import com.chauncey.WeTeBot.model.chat.ChatParam;
import com.chauncey.WeTeBot.model.wechat.BaseMsg;
import com.chauncey.WeTeBot.model.wechat.RecommendInfo;
import com.chauncey.WeTeBot.model.wechat.WechatMember;
import com.chauncey.WeTeBot.repository.MemberRepository;
import com.chauncey.WeTeBot.service.IMsgHandlerService;
import com.chauncey.WeTeBot.service.IWeChatComponentService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 简单示例程序，收到文本信息自动回复原信息，收到图片、语音、小视频后根据路径自动保存
 *
 * @author https://github.com/yaphone
 * @version 1.0
 * @date 创建时间：2017年4月25日 上午12:18:09
 */
@Service
@Log4j2
public class MsgHandlerServiceImpl implements IMsgHandlerService {
    boolean flag = false;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AiChatApi aiChatApi;
    @Autowired
    private IWeChatComponentService weChatComponentService;

    public String textMsgHandle(BaseMsg msg) {
        if (!msg.isGroupMsg()) { // 群消息不处理
            WechatMember wechatMember = memberRepository.findByUserName(msg.getFromUserName());
            if (wechatMember.getNickName().equals("wishes")) {
                if (msg.getContent().equals("知道了") && flag == false) {
                    wechatMember.setReceive(true);
                    memberRepository.save(wechatMember);
                    flag = true;
                    return "礼物已经被豆奶奶签收,签收提醒已经关闭,爱你哦！！！" + "\n对了告诉你一个小秘密,解叔叔会用小bot陪你一天哦,现在给我发送消息体验吧！！！";
                }
            }
            return aiChatApi.chat(new ChatParam(msg.getContent()));
        }
        return null;
    }


    public String picMsgHandle(BaseMsg msg) {
        String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());// 这里使用收到图片的时间作为文件名
        String picPath = "./" + File.separator + fileName + ".jpg"; // 调用此方法来保存图片
        weChatComponentService.sendPicMsgByWeId(msg.getFromUserName(), "./QR.jpg");
        // DownloadTools.getDownloadFn(msg, MsgTypeEnum.PIC.getType(), picPath); // 保存图片的路径
        // return "图片保存成功";
        return null;
    }


    public String voiceMsgHandle(BaseMsg msg) {
        String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
        String voicePath = "D://itchat4j/voice" + File.separator + fileName + ".mp3";
        // DownloadTools.getDownloadFn(msg, MsgTypeEnum.VOICE.getType(), voicePath);
        return null;
    }


    public String viedoMsgHandle(BaseMsg msg) {
        String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
        String viedoPath = "D://itchat4j/viedo" + File.separator + fileName + ".mp4";
        // DownloadTools.getDownloadFn(msg, MsgTypeEnum.VIEDO.getType(), viedoPath);
        return null;
    }


    public String nameCardMsgHandle(BaseMsg msg) {
        return null;
    }


    public void sysMsgHandle(BaseMsg msg) { // 收到系统消息
        String text = msg.getContent();
        log.info(text);
    }


    public String verifyAddFriendMsgHandle(BaseMsg msg) {
        weChatComponentService.addFriend(msg, true); // 同意好友请求，false为不接受好友请求
        RecommendInfo recommendInfo = msg.getRecommendInfo();
        String nickName = recommendInfo.getNickName();
        String province = recommendInfo.getProvince();
        String city = recommendInfo.getCity();
        String text = "你好，来自" + province + city + "的" + nickName + "， 欢迎添加我为好友！";
        return text;
    }


    public String mediaMsgHandle(BaseMsg msg) {
        String fileName = msg.getFileName();
        String filePath = "D://itchat4j/file" + File.separator + fileName; // 这里是需要保存收到的文件路径，文件可以是任何格式如PDF，WORD，EXCEL等。
        weChatComponentService.downloadFile(msg, MsgTypeEnum.MEDIA.getType(), filePath);
        return "文件" + fileName + "保存成功";
    }

}
