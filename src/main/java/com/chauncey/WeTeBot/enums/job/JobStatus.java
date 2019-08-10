package com.chauncey.WeTeBot.enums.job;

/**
 * @ClassName JobStatus
 * @Description TODO
 * @Author https://github.com/ChaunceyCX
 * @Date 2019/8/8 下午6:51
 * @Version 1.0
 **/
public enum JobStatus {
    RUNNING("RUNNING"),
    COMPLETE("COMPLETE"),
    PAUSED("PAUSED");

    private String status;


    JobStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
