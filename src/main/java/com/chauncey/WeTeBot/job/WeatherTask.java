package com.chauncey.WeTeBot.job;

import com.chauncey.WeTeBot.api.WeatherApi;
import com.chauncey.WeTeBot.model.wechat.WechatMember;
import com.chauncey.WeTeBot.repository.MemberRepository;
import com.chauncey.WeTeBot.service.IWeChatComponentService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author https://github.com/ChaunceyCX
 * @version 1.0.0
 * @className WeatherTask
 * @description TODO
 * @date 2019/6/30 上午11:43
 **/
@Log4j2
@Component
public class WeatherTask {

    @Value("${message.default.tousername}")
    private String defaltToUserName;
    @Value("${api.weather.location.start}")
    private String doStartArea;
    @Value("${api.weather.location.end}")
    private String doEndArea;

    @Autowired
    private IWeChatComponentService weChatComponentService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private WeatherApi weatherApi;

    private void sendWeatherMessage() {
        WechatMember wechatMember = memberRepository.findByNickName(defaltToUserName);
        String weatherStr = "";
        if (wechatMember == null) {
            log.info("没有查询到联系人:" + "Chauncey");
        }
        weatherStr = weatherApi.batchWeather(doEndArea);
        // MessageTools.sendMsgById(weatherStr, member.getUserName());
        // member = memberRepository.findByNickName("Chauncey");
        weChatComponentService.sendTextMsgByWeId(weatherStr, wechatMember.getUserName());
    }

}
