package com.chauncey.WeTeBot.model.job;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @ClassName WeJob
 * @Description TODO
 * @Author https://github.com/ChaunceyCX
 * @Date 2019/8/8 下午4:36
 * @Version 1.0
 **/
@Data
@Entity
public class WeJob {
    @Id
    @GeneratedValue
    private Integer id;
    private String jobName;//任务名称(时间戳保证唯一)
    private String jobGroup;//任务分组(创建任务者的昵称)
    private String toNickName;//昵称
    private String toWeId;//微信username
    private String content;//提醒内容
    private String jobClassName;//执行类
    private String cronExpression;//执行时间
    private String triggerState;//任务状态

    public WeJob(String jobName, String jobGroup, String toNickName, String toWeId, String content, String jobClassName, String cronExpression, String triggerState) {
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.toNickName = toNickName;
        this.toWeId = toWeId;
        this.content = content;
        this.jobClassName = jobClassName;
        this.cronExpression = cronExpression;
        this.triggerState = triggerState;
    }

    public WeJob(String jobName, String jobGroup, String toNickName, String toWeId, String jobClassName, String triggerState, String content) {
        this.setJobName(jobName);
        this.setJobGroup(jobGroup);
        this.setToNickName(toNickName);
        this.setToWeId(toWeId);
        this.setJobClassName(jobClassName);
        this.setTriggerState(triggerState);
        this.setContent(content);
    }

    public WeJob() {
    }
}
