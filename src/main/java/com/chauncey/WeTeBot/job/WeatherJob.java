package com.chauncey.WeTeBot.job;

import com.chauncey.WeTeBot.api.WeatherApi;
import com.chauncey.WeTeBot.model.job.WeJob;
import com.chauncey.WeTeBot.model.wechat.WechatMember;
import com.chauncey.WeTeBot.repository.MemberRepository;
import com.chauncey.WeTeBot.repository.WeJobRepository;
import com.chauncey.WeTeBot.service.IWeChatComponentService;
import lombok.extern.log4j.Log4j2;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.QuartzJobBean;
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
public class WeatherJob extends QuartzJobBean {

    @Autowired
    private IWeChatComponentService weChatComponentService;
    @Autowired
    private WeJobRepository weJobRepository;
    @Autowired
    private WeatherApi weatherApi;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        String jobName = context.getJobDetail().getKey().getName();
        WeJob weJob = weJobRepository.getWeJobByJobName(jobName);
        String weatherStr = "";
        weatherStr = weatherApi.batchWeather(weJob.getContent());
        // MessageTools.sendMsgById(weatherStr, member.getUserName());
        // member = memberRepository.findByNickName("Chauncey");
        weChatComponentService.sendTextMsgByWeId(weatherStr, weJob.getToWeId());
    }
}
