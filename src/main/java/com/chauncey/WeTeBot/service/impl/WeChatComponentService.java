package com.chauncey.WeTeBot.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.chauncey.WeTeBot.api.WeChatApi;
import com.chauncey.WeTeBot.enums.StorageLoginInfoEnum;
import com.chauncey.WeTeBot.enums.URLEnum;
import com.chauncey.WeTeBot.model.wechat.BaseMsg;
import com.chauncey.WeTeBot.model.wechat.Core;
import com.chauncey.WeTeBot.service.IWeChatComponentService;
import lombok.extern.log4j.Log4j2;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author https://github.com/ChaunceyCX
 * @Description //微信小工具，如获好友列表等
 * @Date 下午4:59 19-6-17
 * @Param
 * @return
 **/
@Service
@Log4j2
public class WeChatComponentService implements IWeChatComponentService {

    @Autowired
    private Core core;
    @Autowired
    private WeChatApi weChatApi;

    @Override
    public void sendTextMsgByWeId(String msg, String toUserName) {
        weChatApi.sendMsg(msg, toUserName);
    }

    @Override
    public boolean sendPicMsgByWeId(String filePath, String toUserName) {
        weChatApi.webWxUploadMedia(filePath);
        //TODO
        return false;
    }

    @Override
    public String getUserNameByNickName(String nickName) {
		/*for (JSONObject o : core.getContactList()) {
			if (o.getString("NickName").equals(nickName)) {
				return o.getString("UserName");
			}
		}*/
        return getUserNameByNickName(nickName);
    }

    @Override
    public List<String> getContactNickNameList() {
        List<String> contactNickNameList = new ArrayList<String>();
		/*for (JSONObject o : core.getContactList()) {
			contactNickNameList.add(o.getString("NickName"));
		}*/
        return contactNickNameList;
    }

    //@Override
/*public List<JSONObject> getContactList() {
		return core.getContactList();
	}*/

    //@Override
/*public List<JSONObject> getGroupList() {
		return core.getGroupList();
	}*/

    @Override
    public List<String> getGroupIdList() {
        return core.getGroupIdList();
    }

    @Override
    public List<String> getGroupNickNameList() {
        return core.getGroupNickNameList();
    }

    @Override
    public JSONArray getMemberListByGroupId(String groupId) {
        return core.getGroupMemeberMap().get(groupId);
    }


    @Override
    public boolean logout() {
        String url = String.format(URLEnum.WEB_WX_LOGOUT.getUrl(),
                core.getLoginInfo().get(StorageLoginInfoEnum.url.getKey()));
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("redirect", "1"));
        params.add(new BasicNameValuePair("type", "1"));
        params.add(
                new BasicNameValuePair("skey", (String) core.getLoginInfo().get(StorageLoginInfoEnum.skey.getKey())));
        try {
            HttpEntity entity = core.getMyHttpClient().doGet(url, params, false, null);
            String text = EntityUtils.toString(entity, Consts.UTF_8); // 无消息
            return true;
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
        return false;
    }


    @Override
    public void remarkNameByNickName(String nickName, String remName) {
        String url = String.format(URLEnum.WEB_WX_REMARKNAME.getUrl(), core.getLoginInfo().get("url"),
                core.getLoginInfo().get(StorageLoginInfoEnum.pass_ticket.getKey()));
        Map<String, Object> msgMap = new HashMap<String, Object>();
        Map<String, Object> msgMap_BaseRequest = new HashMap<String, Object>();
        msgMap.put("CmdId", 2);
        msgMap.put("RemarkName", remName);
        msgMap.put("UserName", core.getUserInfoMap().get(nickName).getUserName());
        msgMap_BaseRequest.put("Uin", core.getLoginInfo().get(StorageLoginInfoEnum.wxuin.getKey()));
        msgMap_BaseRequest.put("Sid", core.getLoginInfo().get(StorageLoginInfoEnum.wxsid.getKey()));
        msgMap_BaseRequest.put("Skey", core.getLoginInfo().get(StorageLoginInfoEnum.skey.getKey()));
        msgMap_BaseRequest.put("DeviceID", core.getLoginInfo().get(StorageLoginInfoEnum.deviceid.getKey()));
        msgMap.put("BaseRequest", msgMap_BaseRequest);
        try {
            String paramStr = JSON.toJSONString(msgMap);
            HttpEntity entity = core.getMyHttpClient().doPost(url, paramStr);
            // String result = EntityUtils.toString(entity, Consts.UTF_8);
            log.info("修改备注" + remName);
        } catch (Exception e) {
            log.error("remarkNameByUserName", e);
        }
    }

    @Override
    public boolean addFriend(BaseMsg baseMsg, boolean accepted) {
        weChatApi.addFriend(baseMsg, accepted);
        return true;
    }

    @Override
    public Object downloadFile(BaseMsg baseMsg, String type, String savePath) {
        return weChatApi.downloadFile(baseMsg, type, savePath);
    }


    @Override
    public boolean getWechatStatus() {
        return core.isAlive();
    }

}
