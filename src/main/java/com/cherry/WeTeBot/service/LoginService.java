package com.cherry.WeTeBot.service;

import com.cherry.WeTeBot.component.WeChat;
import com.cherry.WeTeBot.exception.WechatException;
import com.cherry.WeTeBot.exception.WechatQRExpiredException;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @ClassName LoginService
 * @Description 登录微信时使用
 * @Author chauncey
 * @Date 19-4-10 下午6:24
 * @Version 1.0
 **/

@Component
public class LoginService {
    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    private WeChat weChat;
    @Autowired
    private SyncServie syncServie;
    @Autowired
    WechatHttpService wechatHttpService;

    @Value("${jeeves.auto-relogin-when-qrcode-expired}")
    private boolean AUTO_RELOGIN_WHEN_QRCODE_EXPIRED;

    @Value("${jeeves.max-qr-refresh-times}")
    private int MAX_QR_REFRESH_TIMES;

    private int qrRefreshTimes = 0;

    public void login() {
        try {

            //0 entry
            wechatHttpService.open(qrRefreshTimes);
            logger.info("[0] entry completed");

            //1 uuid
            weChat.setUuid(wechatHttpService.getUUID());
            logger.info("[1] uuid completed");

            //2 qr
            logger.info("\r\n" + wechatHttpService.getQRStr(weChat.getUuid()));
            logger.info("[2] qrcode completed");

            //3 statreport
            wechatHttpService.statReport();
            logger.info("[3] statReport completed");

            //4 login
            weChat = wechatHttpService.login(weChat);
            logger.info("[4] login completed");

            //5 redirect login
            weChat = wechatHttpService.openNewloginpage(weChat);
            logger.info("[5] redirect login completed");

            //6 redirect
            wechatHttpService.redirect(weChat.getHostUrl());
            logger.info("[6] redirect completed");

            //7 init
            weChat = wechatHttpService.init(weChat);
            logger.info("[7] init completed");

            //8 status notify
            wechatHttpService.statusNotify(weChat);
            logger.info("[8] status notify completed");

            //9 get contact
            weChat = wechatHttpService.getContact(weChat);
            logger.info("[9] get contact completed");

            //10 batch get contact
            weChat = wechatHttpService.batchGetContact(weChat);
            logger.info("[10] batch get contact completed");

            weChat.setAlive(true);
            logger.info("[*] login process completed");
            logger.info("[*] start listening");
            while (true) {
                syncServie.listen();
            }
        } catch (IOException | NotFoundException | WriterException | URISyntaxException ex) {
            throw new WechatException(ex);
        } catch (WechatQRExpiredException ex) {
            if (AUTO_RELOGIN_WHEN_QRCODE_EXPIRED && qrRefreshTimes <= MAX_QR_REFRESH_TIMES) {
                login();
            } else {
                throw new WechatException(ex);
            }
        }
    }
}

