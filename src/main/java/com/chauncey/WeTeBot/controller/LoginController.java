package com.chauncey.WeTeBot.controller;

import com.chauncey.WeTeBot.model.job.WeJob;
import com.chauncey.WeTeBot.model.wechat.Core;
import com.chauncey.WeTeBot.service.IJobService;
import com.chauncey.WeTeBot.service.ILoginService;
import com.chauncey.WeTeBot.thread.CheckLoginStatusThread;
import com.chauncey.WeTeBot.utils.SleepUtils;
import com.chauncey.WeTeBot.utils.tools.CommonTools;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author https://github.com/ChaunceyCX
 * @Description //登陆控制器
 * @Date 下午5:07 19-6-17
 * @Param
 * @return
 **/
@Component
@Log4j2
public class LoginController {
    @Autowired
    private ILoginService loginService;
    @Autowired
    private  Core core;
    @Value("${img.qr-path}")
    private String qrPath;
    @Value("${job.root-package}")
    private String rootPackage;

    @Autowired
    private IJobService jobService;

    public void login() {
        if (core.isAlive()) { // 已登陆
            log.info("itchat4j已登陆");
            return;
        }
        while (true) {
            for (int count = 0; count < 10; count++) {
                log.info("获取UUID");
                while (loginService.getUuid() == null) {
                    log.info("1. 获取微信UUID");
                    while (loginService.getUuid() == null) {
                        log.warn("1.1. 获取微信UUID失败，两秒后重新获取");
                        SleepUtils.sleep(2000);
                    }
                }
                log.info("2. 获取登陆二维码图片");
                if (loginService.getQR(qrPath)) {
                    break;
                } else if (count == 10) {
                    log.error("2.2. 获取登陆二维码图片失败，系统退出");
                    System.exit(0);
                }
            }
            log.info("3. 请扫描二维码图片，并在手机上确认");
            if (!core.isAlive()) {
                loginService.login();
                core.setAlive(true);
                log.info(("登陆成功"));
                break;
            }
            log.info("4. 登陆超时，请重新扫描二维码图片");
        }

        log.info("5. 登陆成功，微信初始化");
        if (!loginService.webWxInit()) {
            log.info("6. 微信初始化异常");
            System.exit(0);
        }

        log.info("6. 开启微信状态通知");
        loginService.wxStatusNotify();

        log.info("7. 清除。。。。");
        CommonTools.clearScreen();
        log.info(String.format("欢迎回来， %s", core.getUserSelf().getNickName()));

        log.info("8. 开始接收消息");
        loginService.startReceiving();

        log.info("9. 获取联系人信息");
        loginService.webWxGetContact();

        log.info("10. 获取群好友及群好友列表");
        loginService.WebWxBatchGetContact();

        log.info("11. 缓存本次登陆好友相关消息");
        //WeChatComponentService.setUserInfo(); // 登陆成功后缓存本次登陆好友相关消息（NickName, UserName）

        log.info("12.开启微信状态检测线程");
        //new Thread(new CheckLoginStatusThread()).start();
    }
}