package com.chauncey.WeTeBot.enums.job;

/**
 * @ClassName JobClass
 * @Description TODO
 * @Author https://github.com/ChaunceyCX
 * @Date 2019/9/14 下午3:42
 * @Version 1.0
 **/
public enum JobClassEnum {

    Alarm("WeAlarmJob"),
    Weather("WeatherJob");

    JobClassEnum(String classStr) {
        this.classStr = classStr;
    }

    private String classStr;

    public String getClassStr() {
        return classStr;
    }
}