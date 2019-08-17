package com.chauncey.WeTeBot.service.impl;

import com.chauncey.WeTeBot.api.AiChatApi;
import com.chauncey.WeTeBot.enums.MsgTypeEnum;
import com.chauncey.WeTeBot.model.chat.ChatParam;
import com.chauncey.WeTeBot.model.job.WeJob;
import com.chauncey.WeTeBot.model.wechat.BaseMsg;
import com.chauncey.WeTeBot.model.wechat.Core;
import com.chauncey.WeTeBot.model.wechat.RecommendInfo;
import com.chauncey.WeTeBot.model.wechat.WechatMember;
import com.chauncey.WeTeBot.repository.MemberRepository;
import com.chauncey.WeTeBot.repository.WeJobRepository;
import com.chauncey.WeTeBot.service.IJobService;
import com.chauncey.WeTeBot.service.IMsgHandlerService;
import com.chauncey.WeTeBot.service.IWeChatComponentService;
import com.chauncey.WeTeBot.utils.CronFormatUtils;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    @Autowired
    private IJobService iJobService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AiChatApi aiChatApi;
    @Autowired
    private IWeChatComponentService weChatComponentService;
    @Autowired
    private Core core;
    @Autowired
    private WeJobRepository weJobRepository;
    @Value("${job.root-package}")
    private String jobPackage;

    public String textMsgHandle(BaseMsg msg) {
        if (!msg.isGroupMsg()) { // 群消息不处理
            if (msg.getContent().length()>2&&msg.getContent().startsWith("提醒")) {
                WeJob weJob = proceedJobText(msg);
                if (null != weJob){
                    iJobService.saveJob(weJob);
                    return "提醒已保存";
                }else {
                    return "请正确输入指令!\n"
                            +"指令格式如下:\n"
                            +"单次提醒:\n"
                            +"提醒我@周一6点30分30秒$起床\n"
                            +"提醒我#明天6点30分$起床\n"
                            +"提醒我##12点30分$干什么\n"
                            +"提醒我#12号14点30分30秒$看电视\n"
                            +"多次提醒:\n"
                            +"提醒我@@周一6点30分30秒$起床\n"
                            +"另:如果时间低于当前时间,将不会收到提醒( 每周提醒除外 )\n";
                }
            } else if (msg.getContent().equals("知道了")) {
                WeJob weJob = weJobRepository.getWeJobById(Integer.valueOf(msg.getContent().split(":")[1]));
                if (null!=weJob) {
                    iJobService.pauseJob(weJob.getJobName(),weJob.getJobGroup());
                }
            }
            return aiChatApi.chat(new ChatParam(msg.getContent()));
        }
        return null;
    }

    private WeJob proceedJobText(BaseMsg baseMsg) {
        WeJob weJob = new WeJob();
        weJob.setJobName(baseMsg.getFromUserName()+"&"+new Date().getTime());
        weJob.setJobGroup("Alarm");
        weJob.setJobClassName(jobPackage+"WeAlarmJob");
        StringBuilder text = new StringBuilder(baseMsg.getContent().replace("提醒",""));
        String cronStr = "";
        if ( text.indexOf("$")>0 ) {
            weJob.setDescription(text.substring(text.indexOf("$") + 1));
            if (text.length() > 3 && text.charAt(0) == '我') {
                text.deleteCharAt(0);
                if (text.charAt(0) == '@') {
                    text.deleteCharAt(0);
                    if (text.charAt(0) == '周') {
                        text.deleteCharAt(0);
                        cronStr = CronFormatUtils.getOneWeekCronStr(text);
                        if (null!=cronStr){
                            weJob.setCronExpression(cronStr);
                            return weJob;
                        }
                    }
                    if (text.length() > 1 && text.substring(0, 1).equals("@周")) {
                        text.delete(0, 1);
                        cronStr = CronFormatUtils.getEveryWeekCronStr(text);
                        if (null!=cronStr){
                            weJob.setCronExpression(cronStr);
                            return weJob;
                        }
                    }
                } else if (text.charAt(0) == '#') {
                    text.deleteCharAt(0);
                    if (text.length() > 3) {
                        if (text.substring(0, 1).equals("明天")) {
                            text.delete(0, 1);
                            cronStr = CronFormatUtils.getNearDayCronStr(text,1);
                            if (null!=cronStr){
                                weJob.setCronExpression(cronStr);
                                return weJob;
                            }
                        } else if (text.substring(0, 1).equals("后天")) {
                            text.delete(0, 1);
                            cronStr = CronFormatUtils.getNearDayCronStr(text,2);
                            if (null!=cronStr){
                                weJob.setCronExpression(cronStr);
                                return weJob;
                            }

                        } else if (text.indexOf("号") > 0 && text.indexOf("号") <= 2) {
                            cronStr = CronFormatUtils.getSimpleCronStr(text);
                            if (null!=cronStr){
                                weJob.setCronExpression(cronStr);
                                return weJob;
                            }

                        } else if (text.charAt(0) == '#') {
                            text.deleteCharAt(0);
                            cronStr = CronFormatUtils.getToDayCronStr(text);
                            if (null!=cronStr){
                                weJob.setCronExpression(cronStr);
                                return weJob;
                            }
                        }
                    }
                }
            }
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
