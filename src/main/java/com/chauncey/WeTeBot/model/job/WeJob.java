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
    private String jobName;//任务名称
    private String jobGroup;//任务分组
    private String description;//任务描述
    private String jobClassName;//执行类
    private String cronExpression;//执行时间
    private String triggerName;//执行时间
    private String triggerState;//任务状态

    public WeJob(String jobName, String jobGroup, String description, String jobClassName, String cronExpression, String triggerName) {
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.description = description;
        this.jobClassName = jobClassName;
        this.cronExpression = cronExpression;
        this.triggerName = triggerName;
    }

    public WeJob() {

    }
}
