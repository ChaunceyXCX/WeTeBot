package com.chauncey.WeTeBot.task;

import com.chauncey.WeTeBot.api.MessageTools;
import com.chauncey.WeTeBot.api.WeatherTools;
import com.chauncey.WeTeBot.model.Member;
import com.chauncey.WeTeBot.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
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

    @Value("${sheduler.message.default.tousername}")
    private String defaultToUserName;
    @Value("weather.location.start")
    private String douStartArea;
    @Value("weather.location.end")
    private String douEndArea;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private WeatherTools weatherTools;
    @Autowired
    private ObjectMapper objectMapper;

    @Scheduled(cron = "0 0 7 * * ?")
    private void sendStartWeatherMessage() {
        Member member = memberRepository.findByNickName(defaultToUserName);
        String weatherStr = "";
        if (member == null) {
            log.info("没有查询到联系人:" + defaultToUserName);
        }
        weatherStr = weatherTools.batchWeather(douStartArea);
        MessageTools.sendMsgById(weatherStr, member.getUserName());
        member = memberRepository.findByNickName("Chauncey");
        MessageTools.sendMsgById(weatherStr, member.getUserName());
    }

    @Scheduled(cron = "20 0 7 * * ?")
    private void sendEndWeatherMessage() {
        Member member = memberRepository.findByNickName(defaultToUserName);
        String weatherStr = "";
        if (member == null) {
            log.info("没有查询到联系人:" + defaultToUserName);
        }
        weatherStr = weatherTools.batchWeather(douEndArea);
        MessageTools.sendMsgById(weatherStr, member.getUserName());
    }


}
