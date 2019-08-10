package com.chauncey.WeTeBot.job;

import com.chauncey.WeTeBot.model.wechat.WechatMember;
import com.chauncey.WeTeBot.repository.MemberRepository;
import com.chauncey.WeTeBot.service.IWeChatComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * @ClassName JobForLover
 * @Description TODO
 * @Author https://github.com/ChaunceyCX
 * @Date 2019/8/6 下午3:02
 * @Version 1.0
 **/
@Component
public class JobForLover {
    @Value("${message.default.tousername}")
    private String defaultToUserName;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private IWeChatComponentService weChatComponentService;


    //@Scheduled(cron = "0 5/5 * 7 8 ?")
    //@Scheduled(cron = "0 0 7 * * ?")
    public void gift() {
        WechatMember wechatMember = memberRepository.findByNickName("wishes");
        if (wechatMember.isReceive()) {
            return;
        }
        weChatComponentService.sendTextMsgByWeId("亲爱的豆奶奶七夕快乐！！！" + "\n" + "今天小bot会作为生日礼物陪在豆奶奶身边哦！！！！" + "\n回复:(知道了)代表你已经顺利收到解叔叔的礼物", wechatMember.getUserName());
    }
}
