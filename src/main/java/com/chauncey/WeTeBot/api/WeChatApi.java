package com.chauncey.WeTeBot.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chauncey.WeTeBot.enums.MsgTypeEnum;
import com.chauncey.WeTeBot.enums.StorageLoginInfoEnum;
import com.chauncey.WeTeBot.enums.URLEnum;
import com.chauncey.WeTeBot.enums.VerifyFriendEnum;
import com.chauncey.WeTeBot.model.wechat.BaseMsg;
import com.chauncey.WeTeBot.model.wechat.Core;
import com.chauncey.WeTeBot.model.wechat.RecommendInfo;
import com.chauncey.WeTeBot.utils.MyHttpClient;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author https://github.com/ChaunceyCX
 * @Description //消息处理类
 * @Date 下午4:54 19-6-17
 * @Param
 * @return
 **/
@Component
@Log4j2
public class WeChatApi {

    @Autowired
    private Core core;
    @Autowired
    private MyHttpClient myHttpClient;

    /**
     * @return void
     * @Author https://github.com/ChaunceyCX
     * @Description //根据UserName(微信用户id:@xxx)发送文本消息
     * @Date 下午4:54 19-6-17
     * @Param [text, toUserName]
     **/
    public void sendMsg(String text, String toUserName) {
        if (text == null) {
            return;
        }
        log.info(String.format("发送消息 %s: %s", toUserName, text));
        webWxSendMsg(1, text, toUserName);
    }


    /**
     * @return void
     * @Author https://github.com/ChaunceyCX
     * @Description //多格式消息发送
     * @Date 下午4:55 19-6-17
     * @Param [msgType, content, toUserName]
     **/
    public void webWxSendMsg(int msgType, String content, String toUserName) {
        String url = String.format(URLEnum.WEB_WX_SEND_MSG.getUrl(), core.getLoginInfo().get("url"));
        Map<String, Object> msgMap = new HashMap<String, Object>();
        msgMap.put("Type", msgType);
        msgMap.put("Content", content);
        msgMap.put("FromUserName", core.getUserSelf().getUserName());
        msgMap.put("ToUserName", toUserName == null ? core.getUserSelf().getUserName() : toUserName);
        msgMap.put("LocalID", new Date().getTime() * 10);
        msgMap.put("ClientMsgId", new Date().getTime() * 10);
        Map<String, Object> paramMap = core.getParamMap();
        paramMap.put("Msg", msgMap);
        paramMap.put("Scene", 0);
        try {
            String paramStr = JSON.toJSONString(paramMap);
            HttpEntity entity = myHttpClient.doPost(url, paramStr);
            EntityUtils.toString(entity, Consts.UTF_8);
        } catch (Exception e) {
            log.error("webWxSendMsg", e);
        }
    }

    /**
     * @return com.alibaba.fastjson.JSONObject
     * @Author https://github.com/ChaunceyCX
     * @Description //上传多媒体文件到 微信服务器，目前应该支持3种类型: 1. pic 直接显示，包含图片，表情 2.video 3.doc 显示为文件，包含PDF等
     * @Date 下午4:56 19-6-17
     * @Param [filePath]
     **/
    public JSONObject webWxUploadMedia(String filePath) {
        File f = new File(filePath);
        if (!f.exists() && f.isFile()) {
            log.info("file is not exist");
            return null;
        }
        String url = String.format(URLEnum.WEB_WX_UPLOAD_MEDIA.getUrl(), core.getLoginInfo().get("fileUrl"));
        String mimeType = new MimetypesFileTypeMap().getContentType(f);
        String mediaType = "";
        if (mimeType == null) {
            mimeType = "text/plain";
        } else {
            mediaType = mimeType.split("/")[0].equals("image") ? "pic" : "doc";
        }
        String lastModifieDate = new SimpleDateFormat("yyyy MM dd HH:mm:ss").format(new Date());
        long fileSize = f.length();
        String passTicket = (String) core.getLoginInfo().get("pass_ticket");
        String clientMediaId = new Date().getTime()
                + String.valueOf(new Random().nextLong()).substring(0, 4);
        String webwxDataTicket = MyHttpClient.getCookie("webwx_data_ticket");
        if (webwxDataTicket == null) {
            log.error("get cookie webwx_data_ticket error");
            return null;
        }

        Map<String, Object> paramMap = core.getParamMap();

        paramMap.put("ClientMediaId", clientMediaId);
        paramMap.put("TotalLen", fileSize);
        paramMap.put("StartPos", 0);
        paramMap.put("DataLen", fileSize);
        paramMap.put("MediaType", 4);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        builder.addTextBody("id", "WU_FILE_0", ContentType.TEXT_PLAIN);
        builder.addTextBody("name", filePath, ContentType.TEXT_PLAIN);
        builder.addTextBody("type", mimeType, ContentType.TEXT_PLAIN);
        builder.addTextBody("lastModifieDate", lastModifieDate, ContentType.TEXT_PLAIN);
        builder.addTextBody("size", String.valueOf(fileSize), ContentType.TEXT_PLAIN);
        builder.addTextBody("mediatype", mediaType, ContentType.TEXT_PLAIN);
        builder.addTextBody("uploadmediarequest", JSON.toJSONString(paramMap), ContentType.TEXT_PLAIN);
        builder.addTextBody("webwx_data_ticket", webwxDataTicket, ContentType.TEXT_PLAIN);
        builder.addTextBody("pass_ticket", passTicket, ContentType.TEXT_PLAIN);
        builder.addBinaryBody("filename", f, ContentType.create(mimeType), filePath);
        HttpEntity reqEntity = builder.build();
        HttpEntity entity = myHttpClient.doPostFile(url, reqEntity);
        if (entity != null) {
            try {
                String result = EntityUtils.toString(entity, Consts.UTF_8);
                return JSON.parseObject(result);
            } catch (Exception e) {
                log.error("webWxUploadMedia 错误： ", e);
            }

        }
        //TODO
        return null;
    }


    /**
     * @return boolean
     * @Author https://github.com/ChaunceyCX
     * @Description //发送图片消息，内部调用
     * @Date 下午4:57 19-6-17
     * @Param [userId, mediaId]
     **/
    private boolean webWxSendMsgImg(String userId, String mediaId) {
        String url = String.format("%s/webwxsendmsgimg?fun=async&f=json&pass_ticket=%s", core.getLoginInfo().get("url"),
                core.getLoginInfo().get("pass_ticket"));
        Map<String, Object> msgMap = new HashMap<String, Object>();
        msgMap.put("Type", 3);
        msgMap.put("MediaId", mediaId);
        msgMap.put("FromUserName", core.getUserSelf().getUserName());
        msgMap.put("ToUserName", userId);
        String clientMsgId = new Date().getTime()
                + String.valueOf(new Random().nextLong()).substring(1, 5);
        msgMap.put("LocalID", clientMsgId);
        msgMap.put("ClientMsgId", clientMsgId);
        Map<String, Object> paramMap = core.getParamMap();
        paramMap.put("BaseRequest", core.getParamMap().get("BaseRequest"));
        paramMap.put("Msg", msgMap);
        String paramStr = JSON.toJSONString(paramMap);
        HttpEntity entity = myHttpClient.doPost(url, paramStr);
        if (entity != null) {
            try {
                String result = EntityUtils.toString(entity, Consts.UTF_8);
                return JSON.parseObject(result).getJSONObject("BaseResponse").getInteger("Ret") == 0;
            } catch (Exception e) {
                log.error("webWxSendMsgImg 错误： ", e);
            }
        }
        return false;

    }



    /**
     * @return boolean
     * @Author https://github.com/ChaunceyCX
     * @Description //内部调用
     * @Date 下午4:58 19-6-17
     * @Param [userId, data]
     **/
    private boolean webWxSendAppMsg(String userId, Map<String, String> data) {
        String url = String.format("%s/webwxsendappmsg?fun=async&f=json&pass_ticket=%s", core.getLoginInfo().get("url"),
                core.getLoginInfo().get("pass_ticket"));
        String clientMsgId = new Date().getTime()
                + String.valueOf(new Random().nextLong()).substring(1, 5);
        String content = "<appmsg appid='wxeb7ec651dd0aefa9' sdkver=''><title>" + data.get("title")
                + "</title><des></des><action></action><type>6</type><content></content><url></url><lowurl></lowurl>"
                + "<appattach><totallen>" + data.get("totallen") + "</totallen><attachid>" + data.get("attachid")
                + "</attachid><fileext>" + data.get("fileext") + "</fileext></appattach><extinfo></extinfo></appmsg>";
        Map<String, Object> msgMap = new HashMap<String, Object>();
        msgMap.put("Type", data.get("type"));
        msgMap.put("Content", content);
        msgMap.put("FromUserName", core.getUserSelf().getUserName());
        msgMap.put("ToUserName", userId);
        msgMap.put("LocalID", clientMsgId);
        msgMap.put("ClientMsgId", clientMsgId);
        /*
         * Map<String, Object> paramMap = new HashMap<String, Object>();
         *
         * @SuppressWarnings("unchecked") Map<String, Map<String, String>>
         * baseRequestMap = (Map<String, Map<String, String>>)
         * core.getLoginInfo() .get("baseRequest"); paramMap.put("BaseRequest",
         * baseRequestMap.get("BaseRequest"));
         */

        Map<String, Object> paramMap = core.getParamMap();
        paramMap.put("Msg", msgMap);
        paramMap.put("Scene", 0);
        String paramStr = JSON.toJSONString(paramMap);
        HttpEntity entity = myHttpClient.doPost(url, paramStr);
        if (entity != null) {
            try {
                String result = EntityUtils.toString(entity, Consts.UTF_8);
                return JSON.parseObject(result).getJSONObject("BaseResponse").getInteger("Ret") == 0;
            } catch (Exception e) {
                log.error("错误: ", e);
            }
        }
        return false;
    }

    /**
     * @return void
     * @Author https://github.com/ChaunceyCX
     * @Description //被动添加好友
     * @Date 下午4:58 19-6-17
     * @Param [msg, accept:true 接受 false 拒绝]
     **/
    public void addFriend(BaseMsg msg, boolean accept) {
        if (!accept) { // 不添加
            return;
        }
        int status = VerifyFriendEnum.ACCEPT.getCode(); // 接受好友请求
        RecommendInfo recommendInfo = msg.getRecommendInfo();
        String userName = recommendInfo.getUserName();
        String ticket = recommendInfo.getTicket();
        // 更新好友列表
        // TODO 此处需要更新好友列表
        // core.getContactList().add(msg.getJSONObject("RecommendInfo"));

        String url = String.format(URLEnum.WEB_WX_VERIFYUSER.getUrl(), core.getLoginInfo().get("url"),
                String.valueOf(System.currentTimeMillis() / 3158L), core.getLoginInfo().get("pass_ticket"));

        List<Map<String, Object>> verifyUserList = new ArrayList<Map<String, Object>>();
        Map<String, Object> verifyUser = new HashMap<String, Object>();
        verifyUser.put("Value", userName);
        verifyUser.put("VerifyUserTicket", ticket);
        verifyUserList.add(verifyUser);

        List<Integer> sceneList = new ArrayList<Integer>();
        sceneList.add(33);

        JSONObject body = new JSONObject();
        body.put("BaseRequest", core.getParamMap().get("BaseRequest"));
        body.put("Opcode", status);
        body.put("VerifyUserListSize", 1);
        body.put("VerifyUserList", verifyUserList);
        body.put("VerifyContent", "");
        body.put("SceneListCount", 1);
        body.put("SceneList", sceneList);
        body.put("skey", core.getLoginInfo().get(StorageLoginInfoEnum.skey.getKey()));

        String result = null;
        try {
            String paramStr = JSON.toJSONString(body);
            HttpEntity entity = myHttpClient.doPost(url, paramStr);
            result = EntityUtils.toString(entity, Consts.UTF_8);
        } catch (Exception e) {
            log.error("webWxSendMsg", e);
        }

        if (StringUtils.isBlank(result)) {
            log.error("被动添加好友失败");
        }

        log.debug(result);

    }

    /**
     * @return java.lang.Object
     * @Author https://github.com/ChaunceyCX
     * @Description //处理下载任务
     * @Date 下午12:02 2019/6/29
     * @Param [msg, type, path]
     **/
    public Object downloadFile(BaseMsg msg, String type, String path) {
        Map<String, String> headerMap = new HashMap<String, String>();
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        String url = "";
        if (type.equals(MsgTypeEnum.PIC.getType())) {
            url = String.format(URLEnum.WEB_WX_GET_MSG_IMG.getUrl(), core.getLoginInfo().get("url"));
        } else if (type.equals(MsgTypeEnum.VOICE.getType())) {
            url = String.format(URLEnum.WEB_WX_GET_VOICE.getUrl(), core.getLoginInfo().get("url"));
        } else if (type.equals(MsgTypeEnum.VIEDO.getType())) {
            headerMap.put("Range", "bytes=0-");
            url = String.format(URLEnum.WEB_WX_GET_VIEDO.getUrl(), core.getLoginInfo().get("url"));
        } else if (type.equals(MsgTypeEnum.MEDIA.getType())) {
            headerMap.put("Range", "bytes=0-");
            url = String.format(URLEnum.WEB_WX_GET_MEDIA.getUrl(), core.getLoginInfo().get("fileUrl"));
            params.add(new BasicNameValuePair("sender", msg.getFromUserName()));
            params.add(new BasicNameValuePair("mediaid", msg.getMediaId()));
            params.add(new BasicNameValuePair("filename", msg.getFileName()));
        }
        params.add(new BasicNameValuePair("msgid", msg.getNewMsgId()));
        params.add(new BasicNameValuePair("skey", (String) core.getLoginInfo().get("skey")));
        HttpEntity entity = myHttpClient.doGet(url, params, true, headerMap);
        try {
            OutputStream out = new FileOutputStream(path);
            byte[] bytes = EntityUtils.toByteArray(entity);
            out.write(bytes);
            out.flush();
            out.close();
            // Tools.printQr(path);

        } catch (Exception e) {
            log.info(e.getMessage());
            return false;
        }
        return null;
    }

}
