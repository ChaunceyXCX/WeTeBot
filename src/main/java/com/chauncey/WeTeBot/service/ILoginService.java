package com.chauncey.WeTeBot.service;

/**
 * @author https://github.com/ChaunceyCX
 * @description //登录服务接口
 * @date 下午4:06 2019/6/22
 **/
public interface ILoginService {

    /**
     * @param
     * @return boolean
     * @author https://github.com/ChaunceyCX
     * @description 登录
     * @date 下午2:40 2019/6/29
     **/
    boolean login();

    /**
     * @param
     * @return java.lang.String
     * @author https://github.com/ChaunceyCX
     * @description 获取UUID
     * @date 下午2:40 2019/6/29
     **/
    String getUuid();

    /**
     * @param qrPath
     * @return boolean
     * @author https://github.com/Ch
     * @description 获取二维码图片
     * @date 下午2:40 2019/6/29
     **/
    boolean getQR(String qrPath);

    /**
     * @param
     * @return boolean
     * @author https://github.com/ChaunceyCX
     * @description web初始化
     * @date 下午2:43 2019/6/29
     **/
    boolean webWxInit();

    /**
     * @param
     * @return void
     * @author https://github.com/ChaunceyCX
     * @description 微信状态通知
     * @date 下午2:43 2019/6/29
     **/
    void wxStatusNotify();

    /**
     * @param
     * @return void
     * @author https://github.com/ChaunceyCX
     * @description 接收消息
     * @date 下午2:44 2019/6/29
     **/
    void startReceiving();

    /**
     * @param
     * @return void
     * @author https://github.com/ChaunceyCX
     * @description 获取微信联系人
     * @date 下午2:44 2019/6/29
     **/
    void webWxGetContact();

    /**
     * @param
     * @return void
     * @author https://github.com/ChaunceyCX
     * @description 批量获取联系人信息
     * @date 下午2:44 2019/6/29
     **/
    void WebWxBatchGetContact();

}
