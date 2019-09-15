package com.chauncey.WeTeBot.service.impl;

import com.chauncey.WeTeBot.config.Result;
import com.chauncey.WeTeBot.enums.job.JobStatusEnum;
import com.chauncey.WeTeBot.model.job.WeJob;
import com.chauncey.WeTeBot.repository.WeJobRepository;
import com.chauncey.WeTeBot.service.IJobService;
import lombok.extern.log4j.Log4j2;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Value("${job.root-package}")
    private String jobRoot;


    @Override
    public Result saveJob(WeJob weJob) {

        try {
            if (weJob.getJobGroup().equals("admin")){
                Date date = new Date();
                weJob.setJobName(Long.toString(date.getTime()));
            }
            schedulerJob(weJob);
            weJob.setTriggerState(JobStatusEnum.RUNNING.getStatus());
            weJob = weJobRepository.save(weJob);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
        Map<String,Object> map = new HashMap<>();
        map.put("weJob",weJob);
        return Result.ok(map);
    }

    @Override
    public Result triggerJob(WeJob weJob) {
        JobKey jobKey = new JobKey(weJob.getJobName(), weJob.getJobGroup());
        try {
            weJobRepository.save(weJob);
            scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return Result.ok();
    }

    @Override
    public Result pauseJob(WeJob weJob) {
        TriggerKey triggerKey = new TriggerKey(weJob.getJobName(),weJob.getJobGroup());
        try {
            weJob.setTriggerState(JobStatusEnum.PAUSED.getStatus());
            weJobRepository.save(weJob);
            scheduler.pauseTrigger(triggerKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return Result.error();
        }
        return Result.ok();
    }

    @Override
    public Result resumeJob(WeJob weJob) {
        TriggerKey triggerKey = new TriggerKey(weJob.getJobName(),weJob.getJobGroup());
        try {
            //scheduler.resumeJob();
            scheduler.resumeTrigger(triggerKey);
            weJob.setTriggerState(JobStatusEnum.RUNNING.getStatus());
            weJobRepository.save(weJob);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return Result.error();
        }
        return Result.ok();
    }

    @Override
    public Result removeJob(WeJob weJob) {
        JobKey jobKey = new JobKey(weJob.getJobName(), weJob.getJobGroup());
        try {
            weJobRepository.delete(weJob);
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
            if (weJob.getTriggerState().equals(JobStatusEnum.PAUSED.getStatus())){
                TriggerKey triggerKey = new TriggerKey(weJob.getJobName(), weJob.getJobGroup());
                scheduler.pauseTrigger(triggerKey);
            }
            weJobRepository.save(weJob);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.toString());
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
                .withDescription(weJob.getContent())
                .build();
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(weJob.getCronExpression().trim());
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(weJob.getJobName(), weJob.getJobGroup())
                .withSchedule(cronScheduleBuilder)
                .startNow()
                .build();


        scheduler.scheduleJob(jobDetail, trigger);
    }

    @Override
    public List<WeJob> getAllJob() {

        return weJobRepository.findAll();
    }


}
