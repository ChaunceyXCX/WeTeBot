package com.chauncey.WeTeBot.job;

import com.chauncey.WeTeBot.model.job.WeJob;
import com.chauncey.WeTeBot.repository.WeJobRepository;
import com.chauncey.WeTeBot.service.IWeChatComponentService;
import lombok.extern.log4j.Log4j2;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * @ClassName WeAlarmJob
 * @Description TODO
 * @Author https://github.com/ChaunceyCX
 * @Date 2019/8/11 下午6:41
 * @Version 1.0
 **/
@Component
@Log4j2
public class WeAlarmJob extends QuartzJobBean {

    @Autowired
    private IWeChatComponentService weChatComponentService;
    @Autowired
    private WeJobRepository weJobRepository;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        String jobName = context.getJobDetail().getKey().getName();
        WeJob weJob = weJobRepository.getWeJobByJobName(jobName);
        if (jobName.contains("&")) {
            jobName = jobName.split("&")[0];
        }
        if (weJob.getCronExpression().contains("/")){

            weChatComponentService.sendTextMsgByWeId(context.getJobDetail().getDescription()+"\n回复:知道了:"+weJob.getId(),jobName);
        }else {

            weChatComponentService.sendTextMsgByWeId(context.getJobDetail().getDescription()+"\n回复:知道了:"+weJob.getId(),jobName);
        }
    }
}
