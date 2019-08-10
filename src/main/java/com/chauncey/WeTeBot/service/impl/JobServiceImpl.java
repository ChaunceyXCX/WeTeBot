package com.chauncey.WeTeBot.service.impl;

import com.chauncey.WeTeBot.config.Result;
import com.chauncey.WeTeBot.enums.job.JobStatus;
import com.chauncey.WeTeBot.model.job.WeJob;
import com.chauncey.WeTeBot.repository.WeJobRepository;
import com.chauncey.WeTeBot.service.IJobService;
import lombok.extern.log4j.Log4j2;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName JobServiceImpl
 * @Description TODO
 * @Author https://github.com/ChaunceyCX
 * @Date 2019/8/8 下午5:40
 * @Version 1.0
 **/
@Service
@Log4j2
public class JobServiceImpl implements IJobService {

    @Autowired
    private Scheduler scheduler;
    @Autowired
    private WeJobRepository weJobRepository;


    @Override
    public Result saveJob(WeJob weJob) {

        try {
            schedulerJob(weJob);
            weJob.setTriggerName(JobStatus.RUNNING.getStatus());
            weJobRepository.save(weJob);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
        return Result.ok();
    }

    @Override
    public Result triggerJob(String jobName, String jobGroup) {
        JobKey jobKey = new JobKey(jobName, jobGroup);
        try {
            scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return Result.ok();
    }

    @Override
    public Result pauseJob(String jobName, String jobGroup) {
        JobKey jobKey = new JobKey(jobName, jobGroup);
        try {
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return Result.error();
        }
        return Result.ok();
    }

    @Override
    public Result resumeJob(String jobName, String jobGroup) {
        JobKey jobKey = new JobKey(jobName, jobGroup);
        try {
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return Result.error();
        }
        return Result.ok();
    }

    @Override
    public Result removeJob(String jobName, String jobGroup) {
        JobKey jobKey = new JobKey(jobName, jobGroup);
        try {
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return Result.error();
        }
        return Result.ok();
    }

    @Override
    public WeJob getJob(String jobName, String jobGroup) {

        return null;
    }

    @Override
    public Result updateJob(WeJob weJob) {
        WeJob oldWeJob = weJobRepository.getWeJobById(weJob.getId());
        try {
            scheduler.deleteJob(new JobKey(oldWeJob.getJobName(), oldWeJob.getJobGroup()));
            schedulerJob(weJob);
            weJobRepository.save(weJob);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
        return Result.ok();
    }

    @Override
    public void schedulerJob(WeJob weJob) throws Exception {
        //构建job信息
        Class cls = Class.forName(weJob.getJobClassName());
        //检验类是否存在
        //cls.newInstance();

        JobDetail jobDetail = JobBuilder.newJob(cls)
                .withIdentity(weJob.getJobName(), weJob.getJobGroup())
                .withDescription(weJob.getDescription())
                .build();
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(weJob.getCronExpression().trim());
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(weJob.getJobName(), weJob.getJobGroup())
                .withSchedule(cronScheduleBuilder)
                .startNow()
                .build();


        scheduler.scheduleJob(jobDetail, trigger);
    }
}
