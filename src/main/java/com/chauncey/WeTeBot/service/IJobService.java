package com.chauncey.WeTeBot.service;

import com.chauncey.WeTeBot.config.Result;
import com.chauncey.WeTeBot.model.job.WeJob;

/**
 * @ClassName IJobService
 * @Description TODO
 * @Author https://github.com/ChaunceyCX
 * @Date 2019/8/8 下午4:25
 * @Version 1.0
 **/
public interface IJobService {
    /**
     * @return com.chauncey.WeTeBot.common.Result
     * @Author https://github.com/ChaunceyCX
     * @Description //新增job
     * @Date 下午4:43 2019/8/8
     * @Param weJob
     **/
    Result saveJob(WeJob weJob);

    /**
     * @param jobGroup
     * @return com.chauncey.WeTeBot.common.Result
     * @Author https://github.com/ChaunceyCX
     * @Description 触发job
     * @Date 下午4:43 2019/8/8
     * @Param jobName
     **/
    Result triggerJob(String jobName, String jobGroup);

    /**
     * @param jobGroup
     * @return com.chauncey.WeTeBot.common.Result
     * @Author https://github.com/ChaunceyCX
     * @Description // 暂停job
     * @Date 下午4:45 2019/8/8
     * @Param jobName
     **/
    Result pauseJob(String jobName, String jobGroup);

    /**
     * @param jobGroup
     * @return com.chauncey.WeTeBot.common.Result
     * @Author https://github.com/ChaunceyCX
     * @Description // 恢复job
     * @Date 下午4:49 2019/8/8
     * @Param * @param jobName
     **/
    Result resumeJob(String jobName, String jobGroup);


    /**
     * 移除job
     *
     * @param jobName
     * @param jobGroup
     * @return com.chauncey.WeTeBot.common.Result
     * @throws
     * @Author https://github.com/ChaunceyCX
     * @Date 2019/8/8 下午5:22
     */
    Result removeJob(String jobName, String jobGroup);

    /**
     * 获取job
     *
     * @param jobName
     * @param jobGroup
     * @return com.chauncey.WeTeBot.model.job.WeJob
     * @throws
     * @Author https://github.com/ChaunceyCX
     * @Date 2019/8/8 下午5:22
     */
    WeJob getJob(String jobName, String jobGroup);

    /**
     * 更新job
     *
     * @param weJob
     * @return com.chauncey.WeTeBot.common.Result
     * @throws
     * @Author https://github.com/ChaunceyCX
     * @Date 2019/8/8 下午5:21
     */
    Result updateJob(WeJob weJob);


    void schedulerJob(WeJob weJob) throws Exception;
}
