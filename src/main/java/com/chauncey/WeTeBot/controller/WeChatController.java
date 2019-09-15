package com.chauncey.WeTeBot.controller;

import com.chauncey.WeTeBot.model.wechat.WechatMember;
import com.chauncey.WeTeBot.repository.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName WeChatController
 * @Description TODO
 * @Author https://github.com/ChaunceyCX
 * @Date 2019/9/4 下午5:55
 * @Version 1.0
 **/
@RestController
@CrossOrigin
@RequestMapping("/wechat")
@Log4j2
public class WeChatController {

    @Autowired
    private MemberRepository memberRepository;


    @GetMapping("/members")
    public List<WechatMember> getWeMembers() {
        log.info("获取好友列表");
        return memberRepository.findAll();
    }
}
