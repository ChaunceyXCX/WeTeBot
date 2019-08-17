package com.chauncey.WeTeBot.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * @ClassName CronFormatUtils
 * @Description TODO
 * @Author https://github.com/ChaunceyCX
 * @Date 2019/8/11 下午6:23
 * @Version 1.0
 **/
public class CronFormatUtils {

    //提醒我@周一6点30分30秒:起床
    public static String getOneWeekCronStr(StringBuilder stringBuilder) {
        //0 0 0 0 0 ?
        StringBuilder strCron = new StringBuilder();
        StringBuilder temp = new StringBuilder();
        if (stringBuilder.length()>0) {
            String dateKey = Character.toString(stringBuilder.charAt(0));
            stringBuilder.deleteCharAt(0);
            int dateKeyNum = weekFormat(dateKey);
            Calendar calendar = Calendar.getInstance();
            Calendar nowerDay = Calendar.getInstance();
            Date date = new Date();
            calendar.setTime(date);
            nowerDay.setTime(date);
            if (dateKeyNum>0&&dateKeyNum<8) {
                int nowWeekDay = calendar.get(Calendar.DAY_OF_WEEK)-1;
                if (nowWeekDay==0){
                    nowWeekDay = 7;
                }
                if (dateKeyNum>=nowWeekDay&&dateKeyNum<7) {
                    calendar.set(Calendar.DAY_OF_WEEK,dateKeyNum+1);
                }else if (dateKeyNum==7){
                    calendar.set(Calendar.DAY_OF_WEEK,1);
                }else {
                    return null;
                }
                strCron.append("0/10 0 0 "+calendar.get(Calendar.DAY_OF_MONTH)+' '+(calendar.get(Calendar.MONTH)+1)+" ?");
                if (stringBuilder.length()>2&&stringBuilder.indexOf("点")>0&&stringBuilder.indexOf("点")<=2) {
                    dateKey = stringBuilder.substring(0,stringBuilder.indexOf("点"));
                    stringBuilder.delete(0,stringBuilder.indexOf("点")+1);
                    if (dateKey.length()>0&& StringUtils.isNumeric(dateKey)&&Integer.valueOf(dateKey)>=nowerDay.get(Calendar.HOUR_OF_DAY)&&Integer.valueOf(dateKey)<=24) {
                        strCron.replace(7,8,dateKey);
                        if (stringBuilder.length()>2&&stringBuilder.indexOf("分")>0&&stringBuilder.indexOf("分")<=2) {
                            dateKey = stringBuilder.substring(0,stringBuilder.indexOf("分"));
                            stringBuilder.delete(0,stringBuilder.indexOf("分")+1);
                            if (dateKey.length()>0&&StringUtils.isNumeric(dateKey)&&Integer.valueOf(dateKey)>=0&&Integer.valueOf(dateKey)<=60) {
                                strCron.replace(5,6,dateKey);
                                if (stringBuilder.length()>2&&stringBuilder.indexOf("秒")>0&&stringBuilder.indexOf("秒")<=2) {
                                    dateKey = stringBuilder.substring(0,stringBuilder.indexOf("秒"));
                                    stringBuilder.delete(0,stringBuilder.indexOf("秒")+1);
                                    if (dateKey.length()>0&&StringUtils.isNumeric(dateKey)&&Integer.valueOf(dateKey)>=0&&Integer.valueOf(dateKey)<=60) {
                                        strCron.replace(0,1,dateKey);
                                        return strCron.toString();
                                    }
                                }
                            }
                        }
                    }
                }
                return strCron.toString();
            }
        }
        return null;
    }

    //提醒我@@周一6点30分30秒:起床
    public static String getEveryWeekCronStr(StringBuilder stringBuilder) {
        StringBuilder strCron = new StringBuilder("* * * ? * *");
        //0 0 0 ? * 0
        StringBuilder temp = new StringBuilder();
        if (stringBuilder.length()>0) {
            String dateKey = Character.toString(stringBuilder.charAt(0));
            stringBuilder.deleteCharAt(0);
            int dateKeyNum = weekFormat(dateKey);
            Calendar calendar = Calendar.getInstance();

            if (dateKeyNum>0&&dateKeyNum<8) {

                if (dateKeyNum<7) {
                    calendar.set(Calendar.DAY_OF_WEEK,dateKeyNum+1);
                }else if (dateKeyNum==7){
                    calendar.set(Calendar.DAY_OF_WEEK,1);
                }
                strCron.append("0 0 0 ? "+"* "+dateKeyNum);
                if (stringBuilder.length()>2&&stringBuilder.indexOf("点")>0&&stringBuilder.indexOf("点")<=2) {
                    dateKey = stringBuilder.substring(0,stringBuilder.indexOf("点"));
                    stringBuilder.delete(0,stringBuilder.indexOf("点")+1);
                    if (dateKey.length()>0&&StringUtils.isNumeric(dateKey)&&Integer.valueOf(dateKey)>=0&&Integer.valueOf(dateKey)<=24) {
                        strCron.replace(4,5,dateKey);
                        if (stringBuilder.length()>2&&stringBuilder.indexOf("分")>0&&stringBuilder.indexOf("分")<=2) {
                            dateKey = stringBuilder.substring(0,stringBuilder.indexOf("分"));
                            stringBuilder.delete(0,stringBuilder.indexOf("分")+1);
                            if (dateKey.length()>0&&StringUtils.isNumeric(dateKey)&&Integer.valueOf(dateKey)>=0&&Integer.valueOf(dateKey)<=60) {
                                strCron.replace(2,3,dateKey);
                                if (stringBuilder.length()>2&&stringBuilder.indexOf("秒")>0&&stringBuilder.indexOf("秒")<=2) {
                                    dateKey = stringBuilder.substring(0,stringBuilder.indexOf("秒"));
                                    stringBuilder.delete(0,stringBuilder.indexOf("秒")+1);
                                    if (dateKey.length()>0&&StringUtils.isNumeric(dateKey)&&Integer.valueOf(dateKey)>=0&&Integer.valueOf(dateKey)<=60) {
                                        strCron.replace(0,1,dateKey);
                                        return strCron.toString();
                                    }
                                }
                            }
                        }
                    }
                }
                return strCron.toString();
            }
        }
        return null;
    }

    //提醒我#明天6点30分:起床
    public static String getNearDayCronStr(StringBuilder stringBuilder,int offSet) {
        StringBuilder strCron = new StringBuilder();
        //0/10 0 0 0 0 ?
        StringBuilder temp = new StringBuilder();
        if (stringBuilder.length()>0) {
            Calendar calendar = Calendar.getInstance();
            Date date = new Date();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH,offSet);
            int dateKeyNum = 0;
            String dateKey = "";
            strCron.append("0/10 0 0 "+calendar.get(Calendar.DAY_OF_MONTH)+' '+(calendar.get(Calendar.MONTH)+1)+" ?");
            if (stringBuilder.length()>2&&stringBuilder.indexOf("点")>0&&stringBuilder.indexOf("点")<=2) {
                dateKey = stringBuilder.substring(0,stringBuilder.indexOf("点"));
                stringBuilder.delete(0,stringBuilder.indexOf("点")+1);
                if (dateKey.length()>0&&StringUtils.isNumeric(dateKey)&&Integer.valueOf(dateKey)>=0&&Integer.valueOf(dateKey)<=24) {
                    strCron.replace(7,8,dateKey);
                    if (stringBuilder.length()>2&&stringBuilder.indexOf("分")>0&&stringBuilder.indexOf("分")<=2) {
                        dateKey = stringBuilder.substring(0,stringBuilder.indexOf("分"));
                        stringBuilder.delete(0,stringBuilder.indexOf("分")+1);
                        if (dateKey.length()>0&&StringUtils.isNumeric(dateKey)&&Integer.valueOf(dateKey)>=0&&Integer.valueOf(dateKey)<=60) {
                            strCron.replace(5,6,dateKey);
                            if (stringBuilder.length()>2&&stringBuilder.indexOf("秒")>0&&stringBuilder.indexOf("秒")<=2) {
                                dateKey = stringBuilder.substring(0,stringBuilder.indexOf("秒"));
                                stringBuilder.delete(0,stringBuilder.indexOf("秒")+1);
                                if (dateKey.length()>0&&StringUtils.isNumeric(dateKey)&&Integer.valueOf(dateKey)>=0&&Integer.valueOf(dateKey)<=60) {
                                    strCron.replace(0,1,dateKey);
                                    return strCron.toString();
                                }
                            }
                        }
                    }
                }
            }
            return strCron.toString();
        }
        return null;
    }

    //提醒我#12号14点30分30秒#看电视
    public static String getSimpleCronStr(StringBuilder stringBuilder) {
        StringBuilder strCron = new StringBuilder();
        //0 0 0 0 0 ?
        StringBuilder temp = new StringBuilder();
        if (stringBuilder.length()>0) {
            String dateKey = "";
            Calendar calendar = Calendar.getInstance();
            if (stringBuilder.length()>2&&stringBuilder.indexOf("号")>0&&stringBuilder.indexOf("号")<=2) {
                dateKey = stringBuilder.substring(0,stringBuilder.indexOf("号"));
                calendar.set(Calendar.DAY_OF_MONTH,Integer.valueOf(dateKey));
                stringBuilder.delete(0,stringBuilder.indexOf("号")+1);
                if (dateKey.length()>0&&StringUtils.isNumeric(dateKey)&&Integer.valueOf(dateKey)>=0&&Integer.valueOf(dateKey)<=30) {
                    strCron.append("0 0 0 " + dateKey + ' ' + (calendar.get(Calendar.MONTH) + 1) + " ?");
                    if (stringBuilder.length() > 2 && stringBuilder.indexOf("点") > 0 && stringBuilder.indexOf("点") <= 2) {
                        dateKey = stringBuilder.substring(0, stringBuilder.indexOf("点"));
                        stringBuilder.delete(0, stringBuilder.indexOf("点")+1);
                        if (dateKey.length() > 0 && StringUtils.isNumeric(dateKey)&&Integer.valueOf(dateKey)>=0&&Integer.valueOf(dateKey)<=24) {
                            strCron.replace(4, 5, dateKey);
                            if (stringBuilder.length() > 2 && stringBuilder.indexOf("分") > 0 && stringBuilder.indexOf("分") <= 2) {
                                dateKey = stringBuilder.substring(0, stringBuilder.indexOf("分"));
                                stringBuilder.delete(0, stringBuilder.indexOf("分")+1);
                                if (dateKey.length() > 0 && StringUtils.isNumeric(dateKey)&&Integer.valueOf(dateKey)>=0&&Integer.valueOf(dateKey)<=60) {
                                    strCron.replace(2, 3, dateKey);
                                    if (stringBuilder.length() > 2 && stringBuilder.indexOf("秒") > 0 && stringBuilder.indexOf("秒") <= 2) {
                                        dateKey = stringBuilder.substring(0, stringBuilder.indexOf("秒"));
                                        stringBuilder.delete(0, stringBuilder.indexOf("秒")+1);
                                        if (dateKey.length() > 0 && StringUtils.isNumeric(dateKey)&&Integer.valueOf(dateKey)>=0&&Integer.valueOf(dateKey)<=60) {
                                            strCron.replace(0, 1, dateKey);
                                            return strCron.toString();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                return strCron.toString();
            }
        }
        return null;
    }


    //提醒我##12点30分:干什么
    public static String getToDayCronStr(StringBuilder stringBuilder) {
        StringBuilder strCron = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(date);
        String dateKey = "";
        if (stringBuilder.length() > 2 && stringBuilder.indexOf("点") > 0 && stringBuilder.indexOf("点") <= 2) {
            dateKey = stringBuilder.substring(0, stringBuilder.indexOf("点"));
            stringBuilder.delete(0, stringBuilder.indexOf("点")+1);
            if (dateKey.length() > 0 && StringUtils.isNumeric(dateKey)) {
                strCron.append("0/10 0 "+dateKey+" "+ calendar.get(Calendar.DAY_OF_MONTH)+" "+(calendar.get(Calendar.MONTH)+1)+" ?");
                if (stringBuilder.length() > 2 && stringBuilder.indexOf("分") > 0 && stringBuilder.indexOf("分") <= 2) {
                    dateKey = stringBuilder.substring(0, stringBuilder.indexOf("分"));
                    stringBuilder.delete(0, stringBuilder.indexOf("分"));
                    if (dateKey.length() > 0 && StringUtils.isNumeric(dateKey)) {
                        strCron.replace(5, 6, dateKey);
                        if (stringBuilder.length() > 2 && stringBuilder.indexOf("秒") > 0 && stringBuilder.indexOf("秒") <= 2) {
                            dateKey = stringBuilder.substring(0, stringBuilder.indexOf("秒"));
                            stringBuilder.delete(0, stringBuilder.indexOf("秒"));
                            if (dateKey.length() > 0 && StringUtils.isNumeric(dateKey)) {
                                strCron.replace(0, 1, dateKey);
                                return strCron.toString();
                            }
                        }
                    }
                }
                return strCron.toString();
            }
        }
        return null;
    }

    public static int weekFormat(String weekKey) {
        int dateKeyNum = 0;
        //周日:1,周一:2...
        switch (weekKey) {
            case "日":
                dateKeyNum = 7;
                break;
            case "一":
                dateKeyNum = 1;
                break;
            case "二":
                dateKeyNum = 2;
                break;
            case "三":
                dateKeyNum = 3;
                break;
            case "四":
                dateKeyNum = 4;
                break;
            case "五":
                dateKeyNum = 5;
                break;
            case "六":
                dateKeyNum = 6;
                break;
        }
        return dateKeyNum;
    }
}
