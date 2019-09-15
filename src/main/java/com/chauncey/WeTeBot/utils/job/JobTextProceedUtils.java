package com.chauncey.WeTeBot.utils.job;

import com.chauncey.WeTeBot.model.job.WeJob;
import com.chauncey.WeTeBot.model.wechat.BaseMsg;

/**
 * @ClassName JobTextProceedUtils
 * @Description TODO
 * @Author https://github.com/ChaunceyCX
 * @Date 2019/9/14 下午4:08
 * @Version 1.0
 **/
public class JobTextProceedUtils {

    //提醒任务
    public static WeJob proceedJobText(WeJob weJob) {

        StringBuilder text = new StringBuilder(weJob.getContent().replace("提醒", ""));
        String cronStr = "";
        if (text.indexOf("$") > 0) {
            weJob.setContent(text.substring(text.indexOf("$") + 1));
            if (text.length() > 3 && text.charAt(0) == '我') {
                text.deleteCharAt(0);
                if (text.charAt(0) == '@') {
                    text.deleteCharAt(0);
                    if (text.charAt(0) == '周') {
                        text.deleteCharAt(0);
                        cronStr = CronFormatUtils.getOneWeekCronStr(text);
                        weJob = setCorn(weJob, cronStr);
                        return weJob;
                    }
                    if (text.length() > 1 && text.substring(0, 1).equals("@周")) {
                        text.delete(0, 1);
                        cronStr = CronFormatUtils.getEveryWeekCronStr(text);
                        weJob = setCorn(weJob, cronStr);
                        return weJob;
                    }
                } else if (text.charAt(0) == '#') {
                    text.deleteCharAt(0);
                    if (text.length() > 3) {
                        if (text.substring(0, 2).equals("明天")) {
                            text.delete(0, 2);
                            cronStr = CronFormatUtils.getNearDayCronStr(text, 1);
                            weJob = setCorn(weJob, cronStr);
                            return weJob;
                        } else if (text.substring(0, 2).equals("后天")) {
                            text.delete(0, 2);
                            cronStr = CronFormatUtils.getNearDayCronStr(text, 2);
                            weJob = setCorn(weJob, cronStr);
                            return weJob;

                        } else if (text.indexOf("号") > 0 && text.indexOf("号") <= 2) {
                            cronStr = CronFormatUtils.getSimpleCronStr(text);
                            weJob = setCorn(weJob, cronStr);
                            return weJob;

                        } else if (text.charAt(0) == '#') {
                            text.deleteCharAt(0);
                            cronStr = CronFormatUtils.getToDayCronStr(text);
                            weJob = setCorn(weJob, cronStr);
                            return weJob;
                        }
                    }
                }
            }
        }

        return null;
    }

    //天气任务
    public static WeJob proceedWeatherText(WeJob weJob) {
        StringBuilder text = new StringBuilder(weJob.getContent().replace("天气", ""));
        String cronStr = "";
        if (text.indexOf("$") > 0 && text.length() > text.indexOf("$")) {
            weJob.setContent(text.substring(text.indexOf("$") + 1));
            if (text.charAt(0) == '#') {
                text.deleteCharAt(0);
                if (text.length() > 3) {
                    cronStr = CronFormatUtils.getWeatherCorn(text);
                    weJob = setCorn(weJob, cronStr);
                    return weJob;
                }

            }
        }

        return null;
    }

    public static WeJob setCorn(WeJob weJob, String cronStr) {
        if (null != cronStr) {
            weJob.setCronExpression(cronStr);
            return weJob;
        }
        return weJob;
    }
}
