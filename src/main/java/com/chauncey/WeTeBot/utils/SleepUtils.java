package com.chauncey.WeTeBot.utils;

/**
 * @author https://github.com/ChaunceyCX
 * @description 线程休眠
 * @date 下午2:56 2019/6/29
 **/
public class SleepUtils {

    /**
     * @param time
     * @return void
     * @author https://github.com/ChaunceyCX
     * @description 毫秒为单位
     * @date 下午2:55 2019/6/29
     **/
    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
