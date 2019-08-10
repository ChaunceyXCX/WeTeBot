package com.chauncey.WeTeBot.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chauncey.WeTeBot.enums.ResultEnum;
import com.chauncey.WeTeBot.enums.RetCodeEnum;
import com.chauncey.WeTeBot.enums.StorageLoginInfoEnum;
import com.chauncey.WeTeBot.enums.URLEnum;
import com.chauncey.WeTeBot.enums.parameters.BaseParaEnum;
import com.chauncey.WeTeBot.enums.parameters.LoginParaEnum;
import com.chauncey.WeTeBot.enums.parameters.StatusNotifyParaEnum;
import com.chauncey.WeTeBot.enums.parameters.UUIDParaEnum;
import com.chauncey.WeTeBot.model.wechat.*;
import com.chauncey.WeTeBot.repository.MemberRepository;
import com.chauncey.WeTeBot.service.ILoginService;
import com.chauncey.WeTeBot.service.IMessageProcessService;
import com.chauncey.WeTeBot.utils.Config;
import com.chauncey.WeTeBot.utils.MyHttpClient;
import com.chauncey.WeTeBot.utils.SleepUtils;
import com.chauncey.WeTeBot.utils.tools.CommonTools;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;

/**
 * @Author https://github.com/ChaunceyCX
 * @Description //登录服务实现类
 * @Date 下午4:04 2019/6/22
 * @Param
 * @return
 **/
@Service
@Log4j2
public class LoginServiceImpl implements ILoginService {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BatchGroup batchGroup;
    @Autowired
    private BatchMember batchMember;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ContactInit contactInit;
    @Autowired
    private IMessageProcessService messageProcessService;
    @Autowired
    private Core core;
    private MyHttpClient httpClient = core.getMyHttpClient();

    private MyHttpClient myHttpClient = core.getMyHttpClient();

    public LoginServiceImpl() {

    }

    public boolean login() {

        boolean isLogin = false;
        // 组装参数和URL
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair(LoginParaEnum.LOGIN_ICON.para(), LoginParaEnum.LOGIN_ICON.value()));
        params.add(new BasicNameValuePair(LoginParaEnum.UUID.para(), core.getUuid()));
        params.add(new BasicNameValuePair(LoginParaEnum.TIP.para(), LoginParaEnum.TIP.value()));

        // long time = 4000;
        while (!isLogin) {
            // SleepUtils.sleep(time += 1000);
            long millis = System.currentTimeMillis();
            params.add(new BasicNameValuePair(LoginParaEnum.R.para(), String.valueOf(millis / 1579L)));
            //TODO
            params.add(new BasicNameValuePair(LoginParaEnum._.para(), String.valueOf(millis)));
            HttpEntity entity = httpClient.doGet(URLEnum.LOGIN_URL.getUrl(), params, true, null);

            try {
                String result = EntityUtils.toString(entity);
                String status = checklogin(result);

                if (ResultEnum.SUCCESS.getCode().equals(status)) {
                    processLoginInfo(result); // 处理结果
                    isLogin = true;
                    core.setAlive(isLogin);
                    break;
                }
                if (ResultEnum.WAIT_CONFIRM.getCode().equals(status)) {
                    log.info("请点击微信确认按钮，进行登陆");
                }

            } catch (Exception e) {
                log.error("微信登陆异常！", e);
            }
        }
        return isLogin;
    }


    public String getUuid() {
        // 组装参数和URL
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        // TODO
        params.add(new BasicNameValuePair(UUIDParaEnum.APP_ID.para(), UUIDParaEnum.APP_ID.value()));
        params.add(new BasicNameValuePair(UUIDParaEnum.FUN.para(), UUIDParaEnum.FUN.value()));
        params.add(new BasicNameValuePair(UUIDParaEnum.LANG.para(), UUIDParaEnum.LANG.value()));
        //TODO
        params.add(new BasicNameValuePair(UUIDParaEnum._.para(), String.valueOf(System.currentTimeMillis())));

        HttpEntity entity = httpClient.doGet(URLEnum.UUID_URL.getUrl(), params, true, null);

        try {
            String result = EntityUtils.toString(entity);
            String regEx = "window.QRLogin.code = (\\d+); window.QRLogin.uuid = \"(\\S+?)\";";
            Matcher matcher = CommonTools.getMatcher(regEx, result);
            if (matcher.find()) {
                if ((ResultEnum.SUCCESS.getCode().equals(matcher.group(1)))) {
                    core.setUuid(matcher.group(2));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return core.getUuid();
    }


    public boolean getQR(String qrPath) {
        qrPath = qrPath + File.separator + "QR.jpg";
        String qrUrl = URLEnum.QRCODE_URL.getUrl() + core.getUuid();
        HttpEntity entity = myHttpClient.doGet(qrUrl, null, true, null);
        try {
            File file = new File(qrPath);
            if (!file.getParentFile().exists()) {
                log.info("目标路径不存在,创建之");
                if (!file.getParentFile().mkdirs()) {
                    log.info("创建目标文件所在目录失败！");
                    return false;
                }
            }
            OutputStream out = new FileOutputStream(qrPath);
            byte[] bytes = EntityUtils.toByteArray(entity);
            out.write(bytes);
            out.flush();
            out.close();
            try {
                CommonTools.printQr(qrPath, bytes); // 打开登陆二维码图片
            } catch (Exception e) {
                log.info(e.getMessage());
            }

        } catch (Exception e) {
            log.info(e.getMessage());
            return false;
        }

        return true;
    }


    public boolean webWxInit() {
        core.setAlive(true);
        core.setLastNormalRetcodeTime(System.currentTimeMillis());
        // 组装请求URL和参数
        String url = String.format(URLEnum.INIT_URL.getUrl(),
                core.getLoginInfo().get(StorageLoginInfoEnum.url.getKey()),
                String.valueOf(System.currentTimeMillis() / 3158L),
                core.getLoginInfo().get(StorageLoginInfoEnum.pass_ticket.getKey()));

        Map<String, Object> paramMap = core.getParamMap();

        // 请求初始化接口
        HttpEntity entity = httpClient.doPost(url, JSON.toJSONString(paramMap));
        try {
            String result = EntityUtils.toString(entity, Consts.UTF_8);
            contactInit = objectMapper.readValue(result, ContactInit.class);

            //```````````
            JSONObject obj = JSON.parseObject(result);

            JSONObject user = obj.getJSONObject(StorageLoginInfoEnum.User.getKey());
            JSONObject syncKey = obj.getJSONObject(StorageLoginInfoEnum.SyncKey.getKey());
            //`````````````

            core.getLoginInfo().put(StorageLoginInfoEnum.InviteStartCount.getKey(), contactInit.getInviteStartCount());
            core.getLoginInfo().put(StorageLoginInfoEnum.SyncKey.getKey(), contactInit.getSyncKey());

            StringBuilder sb = new StringBuilder();
            List<SyncKeyItem> syncArray = contactInit.getSyncKey().getList();
            for (int i = 0; i < syncArray.size(); i++) {
                sb.append(syncArray.get(i).getKey() + "_"
                        + syncArray.get(i).getVal() + "|");
            }
            // 1_661706053|2_661706420|3_661706415|1000_1494151022|
            String synckey = sb.toString();

            // 1_661706053|2_661706420|3_661706415|1000_1494151022
            core.getLoginInfo().put(StorageLoginInfoEnum.synckey.getKey(), synckey.substring(0, synckey.length() - 1));// 1_656161336|2_656161626|3_656161313|11_656159955|13_656120033|201_1492273724|1000_1492265953|1001_1492250432|1004_1491805192
            core.setUserSelf(contactInit.getUser());

            String chatSet = contactInit.getChatSet();
            String[] chatSetArray = chatSet.split(",");
            for (int i = 0; i < chatSetArray.length; i++) {
                if (chatSetArray[i].indexOf("@@") != -1) {
                    // 更新GroupIdList
                    core.getGroupIdList().add(chatSetArray[i]); //
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public void wxStatusNotify() {
        // 组装请求URL和参数
        String url = String.format(URLEnum.STATUS_NOTIFY_URL.getUrl(),
                core.getLoginInfo().get(StorageLoginInfoEnum.pass_ticket.getKey()));

        Map<String, Object> paramMap = core.getParamMap();
        paramMap.put(StatusNotifyParaEnum.CODE.para(), StatusNotifyParaEnum.CODE.value());
        paramMap.put(StatusNotifyParaEnum.FROM_USERNAME.para(), core.getUserSelf().getUserName());
        paramMap.put(StatusNotifyParaEnum.TO_USERNAME.para(), core.getUserSelf().getUserName());
        paramMap.put(StatusNotifyParaEnum.CLIENT_MSG_ID.para(), System.currentTimeMillis());
        String paramStr = JSON.toJSONString(paramMap);

        try {
            HttpEntity entity = httpClient.doPost(url, paramStr);
            EntityUtils.toString(entity, Consts.UTF_8);
        } catch (Exception e) {
            log.error("微信状态通知接口失败！", e);
        }

    }


    public void startReceiving() {
        core.setAlive(true);
        new Thread(new Runnable() {
            int retryCount = 0;


            public void run() {
                while (core.isAlive()) {
                    try {
                        Thread.sleep(1000);
                        Map<String, String> resultMap = syncCheck();
                        log.info(JSONObject.toJSONString(resultMap));
                        String retcode = resultMap.get("retcode");
                        String selector = resultMap.get("selector");
                        if (retcode.equals(RetCodeEnum.UNKOWN.getCode())) {
                            log.info(RetCodeEnum.UNKOWN.getType());
                            continue;
                        } else if (retcode.equals(RetCodeEnum.LOGIN_OUT.getCode())) { // 退出
                            log.info(RetCodeEnum.LOGIN_OUT.getType());
                            break;
                        } else if (retcode.equals(RetCodeEnum.LOGIN_OTHERWHERE.getCode())) { // 其它地方登陆
                            log.info(RetCodeEnum.LOGIN_OTHERWHERE.getType());
                            break;
                        } else if (retcode.equals(RetCodeEnum.MOBILE_LOGIN_OUT.getCode())) { // 移动端退出
                            log.info(RetCodeEnum.MOBILE_LOGIN_OUT.getType());
                            break;
                        } else if (retcode.equals(RetCodeEnum.NORMAL.getCode())) {
                            core.setLastNormalRetcodeTime(System.currentTimeMillis()); // 最后收到正常报文时间
                            JSONObject msgObj = webWxSync();
                            if (selector.equals("2")) {
                                if (msgObj != null) {
                                    try {
                                        JSONArray msgList = new JSONArray();
                                        msgList = msgObj.getJSONArray("AddMsgList");
                                        msgList = messageProcessService.produceMsg(msgList);
                                        for (int j = 0; j < msgList.size(); j++) {
                                            BaseMsg baseMsg = JSON.toJavaObject(msgList.getJSONObject(j),
                                                    BaseMsg.class);
                                            core.getMsgList().add(baseMsg);
                                        }
                                    } catch (Exception e) {
                                        log.info(e.getMessage());
                                    }
                                }
                            } else if (selector.equals("7")) {
                                webWxSync();
                            } else if (selector.equals("4")) {
                                continue;
                            } else if (selector.equals("3")) {
                                continue;
                            } else if (selector.equals("6")) {
                                if (msgObj != null) {
                                    try {
                                        JSONArray msgList = new JSONArray();
                                        msgList = msgObj.getJSONArray("AddMsgList");
                                        JSONArray modContactList = msgObj.getJSONArray("ModContactList"); // 存在删除或者新增的好友信息
                                        msgList = messageProcessService.produceMsg(msgList);
                                        for (int j = 0; j < msgList.size(); j++) {
                                            JSONObject userInfo = modContactList.getJSONObject(j);
                                            // 存在主动加好友之后的同步联系人到本地
//											core.getContactList().add(userInfo);
                                        }
                                    } catch (Exception e) {
                                        log.info(e.getMessage());
                                    }
                                }

                            }
                        } else {
                            JSONObject obj = webWxSync();
                        }
                    } catch (Exception e) {
                        log.info(e.getMessage());
                        retryCount += 1;
                        if (core.getReceivingRetryCount() < retryCount) {
                            core.setAlive(false);
                        } else {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e1) {
                                log.info(e.getMessage());
                            }
                        }
                    }

                }
            }
        }).start();

    }


    public void webWxGetContact() {
        String url = String.format(URLEnum.WEB_WX_GET_CONTACT.getUrl(),
                core.getLoginInfo().get(StorageLoginInfoEnum.url.getKey()));
        Map<String, Object> paramMap = core.getParamMap();
        HttpEntity entity = httpClient.doPost(url, JSON.toJSONString(paramMap));


        try {
            String result = EntityUtils.toString(entity, Consts.UTF_8);
            batchMember = objectMapper.readValue(result, BatchMember.class);

            long currentTime = new Date().getTime();
            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            BatchMember batchMember1 = new BatchMember();
            batchMember1.setSeq(batchMember.getSeq());

            // 循环获取seq直到为0，即获取全部好友列表 ==0：好友获取完毕 >0：好友未获取完毕，此时seq为已获取的字节数
            while (batchMember1.getSeq() > 0) {
                // 设置seq传参
                params.add(new BasicNameValuePair("r", String.valueOf(currentTime)));
                params.add(new BasicNameValuePair("seq", String.valueOf(batchMember.getSeq())));
                entity = httpClient.doGet(url, params, false, null);

                params.remove(new BasicNameValuePair("r", String.valueOf(currentTime)));
                params.remove(new BasicNameValuePair("seq", String.valueOf(batchMember.getSeq())));

                result = EntityUtils.toString(entity, Consts.UTF_8);
                batchMember1 = objectMapper.readValue(result, BatchMember.class);
                batchMember.getMemberList().addAll(batchMember1.getMemberList());
            }

            core.setMemberCount(batchMember.getMemberList().size());
            List<WechatMember> wechatMembers = new ArrayList<>();
            for (WechatMember wechatMember : batchMember.getMemberList()) {
                if (wechatMember.getUserName().indexOf("@@") != -1) { // 群聊
                    if (!core.getGroupIdList().contains(wechatMember.getUserName())) {
                        //群id取出来等会用
                        core.getGroupIdList().add(wechatMember.getUserName());
                    }
                } else {
                    wechatMembers.add(wechatMember);
                    if ((wechatMember.getVerifyFlag() & 8) != 0) { // 公众号/服务号
                        wechatMember.setFlag(2);
                    } else if (Config.API_SPECIAL_USER.contains(wechatMember.getUserName())) { // 特殊账号
                        wechatMember.setFlag(3);
                    } else if (wechatMember.getUserName().equals(core.getUserSelf().getUserName())) { // 自己
                        wechatMember.setFlag(0);
                    } else { // 普通联系人
                        wechatMember.setFlag(1);
                    }
                }
            }
            memberRepository.saveAll(wechatMembers);
            log.info(memberRepository.findAll());
            return;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return;
    }


    public void WebWxBatchGetContact() {
        String url = String.format(URLEnum.WEB_WX_BATCH_GET_CONTACT.getUrl(),
                core.getLoginInfo().get(StorageLoginInfoEnum.url.getKey()), new Date().getTime(),
                core.getLoginInfo().get(StorageLoginInfoEnum.pass_ticket.getKey()));
        Map<String, Object> paramMap = core.getParamMap();
        paramMap.put("Count", core.getGroupIdList().size());
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        for (int i = 0; i < core.getGroupIdList().size(); i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("UserName", core.getGroupIdList().get(i));
            map.put("EncryChatRoomId", "");
            list.add(map);
        }
        paramMap.put("List", list);
        HttpEntity entity = httpClient.doPost(url, JSON.toJSONString(paramMap));
        try {
            String text = EntityUtils.toString(entity, Consts.UTF_8);
            batchGroup = objectMapper.readValue(text, BatchGroup.class);
            core.setGroups(batchGroup.getContactList());
//			groupRepository.saveAll(batchGroup.getContactList());
//			log.info(groupRepository.findAll());
//			log.info(groupUserReposity.findAll());
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    /**
     * @return java.lang.String
     * @Author https://github.com/ChaunceyCX
     * @Description //检查登录状态
     * @Date 下午4:04 2019/6/22
     * @Param [result]
     **/
    public String checklogin(String result) {
        String regEx = "window.code=(\\d+)";
        Matcher matcher = CommonTools.getMatcher(regEx, result);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    /**
     * @return void
     * @Author https://github.com/ChaunceyCX
     * @Description //处理登录信息
     * @Date 下午4:04 2019/6/22
     * @Param [loginContent]
     **/
    private void processLoginInfo(String loginContent) {
        String regEx = "window.redirect_uri=\"(\\S+)\";";
        Matcher matcher = CommonTools.getMatcher(regEx, loginContent);
        if (matcher.find()) {
            String originalUrl = matcher.group(1);
            String url = originalUrl.substring(0, originalUrl.lastIndexOf('/')); // https://wx2.qq.com/cgi-bin/mmwebwx-bin
            core.getLoginInfo().put("url", url);
            Map<String, List<String>> possibleUrlMap = this.getPossibleUrlMap();
            Iterator<Entry<String, List<String>>> iterator = possibleUrlMap.entrySet().iterator();
            Entry<String, List<String>> entry;
            String fileUrl;
            String syncUrl;
            while (iterator.hasNext()) {
                entry = iterator.next();
                String indexUrl = entry.getKey();
                fileUrl = "https://" + entry.getValue().get(0) + "/cgi-bin/mmwebwx-bin";
                syncUrl = "https://" + entry.getValue().get(1) + "/cgi-bin/mmwebwx-bin";
                if (core.getLoginInfo().get("url").toString().contains(indexUrl)) {
                    core.setIndexUrl(indexUrl);
                    core.getLoginInfo().put("fileUrl", fileUrl);
                    core.getLoginInfo().put("syncUrl", syncUrl);
                    break;
                }
            }
            if (core.getLoginInfo().get("fileUrl") == null && core.getLoginInfo().get("syncUrl") == null) {
                core.getLoginInfo().put("fileUrl", url);
                core.getLoginInfo().put("syncUrl", url);
            }
            core.getLoginInfo().put("deviceid", "e" + String.valueOf(new Random().nextLong()).substring(1, 16)); // 生成15位随机数
            core.getLoginInfo().put("BaseRequest", new ArrayList<String>());
            String text = "";

            try {
                HttpEntity entity = myHttpClient.doGet(originalUrl, null, false, null);
                text = EntityUtils.toString(entity);
            } catch (Exception e) {
                log.info(e.getMessage());
                return;
            }
            //add by 默非默 2017-08-01 22:28:09
            //如果登录被禁止时，则登录返回的message内容不为空，下面代码则判断登录内容是否为空，不为空则退出程序
            String msg = getLoginMessage(text);
            if (!"".equals(msg)) {
                log.info(msg);
                System.exit(0);
            }
            Document doc = CommonTools.xmlParser(text);
            if (doc != null) {
                core.getLoginInfo().put(StorageLoginInfoEnum.skey.getKey(),
                        doc.getElementsByTagName(StorageLoginInfoEnum.skey.getKey()).item(0).getFirstChild()
                                .getNodeValue());
                core.getLoginInfo().put(StorageLoginInfoEnum.wxsid.getKey(),
                        doc.getElementsByTagName(StorageLoginInfoEnum.wxsid.getKey()).item(0).getFirstChild()
                                .getNodeValue());
                core.getLoginInfo().put(StorageLoginInfoEnum.wxuin.getKey(),
                        doc.getElementsByTagName(StorageLoginInfoEnum.wxuin.getKey()).item(0).getFirstChild()
                                .getNodeValue());
                core.getLoginInfo().put(StorageLoginInfoEnum.pass_ticket.getKey(),
                        doc.getElementsByTagName(StorageLoginInfoEnum.pass_ticket.getKey()).item(0).getFirstChild()
                                .getNodeValue());
            }

        }
    }

    private Map<String, List<String>> getPossibleUrlMap() {
        Map<String, List<String>> possibleUrlMap = new HashMap<String, List<String>>();
        possibleUrlMap.put("wx.qq.com", new ArrayList<String>() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            {
                add("file.wx.qq.com");
                add("webpush.wx.qq.com");
            }
        });

        possibleUrlMap.put("wx2.qq.com", new ArrayList<String>() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            {
                add("file.wx2.qq.com");
                add("webpush.wx2.qq.com");
            }
        });
        possibleUrlMap.put("wx8.qq.com", new ArrayList<String>() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            {
                add("file.wx8.qq.com");
                add("webpush.wx8.qq.com");
            }
        });

        possibleUrlMap.put("web2.wechat.com", new ArrayList<String>() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            {
                add("file.web2.wechat.com");
                add("webpush.web2.wechat.com");
            }
        });
        possibleUrlMap.put("wechat.com", new ArrayList<String>() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            {
                add("file.web.wechat.com");
                add("webpush.web.wechat.com");
            }
        });
        return possibleUrlMap;
    }

    /**
     * @return com.alibaba.fastjson.JSONObject
     * @Author https://github.com/ChaunceyCX
     * @Description //同步消息 sync the message
     * @Date 下午4:05 2019/6/22
     * @Param []
     **/
    private JSONObject webWxSync() {
        JSONObject result = null;
        String url = String.format(URLEnum.WEB_WX_SYNC_URL.getUrl(),
                core.getLoginInfo().get(StorageLoginInfoEnum.url.getKey()),
                core.getLoginInfo().get(StorageLoginInfoEnum.wxsid.getKey()),
                core.getLoginInfo().get(StorageLoginInfoEnum.skey.getKey()),
                core.getLoginInfo().get(StorageLoginInfoEnum.pass_ticket.getKey()));
        Map<String, Object> paramMap = core.getParamMap();
        paramMap.put(StorageLoginInfoEnum.SyncKey.getKey(),
                core.getLoginInfo().get(StorageLoginInfoEnum.SyncKey.getKey()));
        paramMap.put("rr", -new Date().getTime() / 1000);
        String paramStr = null;
        try {
            paramStr = objectMapper.writeValueAsString(paramMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            HttpEntity entity = myHttpClient.doPost(url, paramStr);
            String text = EntityUtils.toString(entity, Consts.UTF_8);
            JSONObject obj = JSON.parseObject(text);
            if (obj.getJSONObject("BaseResponse").getInteger("Ret") != 0) {
                result = null;
            } else {
                result = obj;
                core.getLoginInfo().put(StorageLoginInfoEnum.SyncKey.getKey(), obj.getJSONObject("SyncCheckKey"));
                JSONArray syncArray = obj.getJSONObject(StorageLoginInfoEnum.SyncKey.getKey()).getJSONArray("List");
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < syncArray.size(); i++) {
                    sb.append(syncArray.getJSONObject(i).getString("Key") + "_"
                            + syncArray.getJSONObject(i).getString("Val") + "|");
                }
                String synckey = sb.toString();
                core.getLoginInfo().put(StorageLoginInfoEnum.synckey.getKey(),
                        synckey.substring(0, synckey.length() - 1));// 1_656161336|2_656161626|3_656161313|11_656159955|13_656120033|201_1492273724|1000_1492265953|1001_1492250432|1004_1491805192
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return result;

    }

    /**
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @Author https://github.com/ChaunceyCX
     * @Description //检查是否有新消息
     * @Date 下午4:05 2019/6/22
     * @Param []
     **/
    private Map<String, String> syncCheck() {
        Map<String, String> resultMap = new HashMap<String, String>();
        // 组装请求URL和参数
        String url = core.getLoginInfo().get(StorageLoginInfoEnum.syncUrl.getKey()) + URLEnum.SYNC_CHECK_URL.getUrl();
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        for (BaseParaEnum baseRequest : BaseParaEnum.values()) {
            params.add(new BasicNameValuePair(baseRequest.para().toLowerCase(),
                    core.getLoginInfo().get(baseRequest.value()).toString()));
        }
        params.add(new BasicNameValuePair("r", String.valueOf(new Date().getTime())));
        params.add(new BasicNameValuePair("synckey", (String) core.getLoginInfo().get("synckey")));
        params.add(new BasicNameValuePair("_", String.valueOf(new Date().getTime())));
        SleepUtils.sleep(7);
        try {
            HttpEntity entity = myHttpClient.doGet(url, params, true, null);
            if (entity == null) {
                resultMap.put("retcode", "9999");
                resultMap.put("selector", "9999");
                return resultMap;
            }
            String text = EntityUtils.toString(entity);
            String regEx = "window.synccheck=\\{retcode:\"(\\d+)\",selector:\"(\\d+)\"\\}";
            Matcher matcher = CommonTools.getMatcher(regEx, text);
            if (!matcher.find() || matcher.group(1).equals("2")) {
                log.info(String.format("Unexpected sync check result: %s", text));
            } else {
                resultMap.put("retcode", matcher.group(1));
                resultMap.put("selector", matcher.group(2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     * @return java.lang.String
     * @Author https://github.com/ChaunceyCX
     * @Description //解析登录返回的消息,如果成功返回空
     * @Date 下午4:06 2019/6/22
     * @Param [result]
     **/
    public String getLoginMessage(String result) {
        String[] strArr = result.split("<message>");
        String[] rs = strArr[1].split("</message>");
        if (rs != null && rs.length > 1) {
            return rs[0];
        }
        return "";
    }
}
