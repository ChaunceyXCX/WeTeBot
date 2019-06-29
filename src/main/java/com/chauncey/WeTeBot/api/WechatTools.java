package com.chauncey.WeTeBot.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.chauncey.WeTeBot.enums.StorageLoginInfoEnum;
import com.chauncey.WeTeBot.enums.URLEnum;
import com.chauncey.WeTeBot.model.Core;
import com.chauncey.WeTeBot.model.Member;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class WechatTools {
    private static Logger LOG = LoggerFactory.getLogger(WechatTools.class);

    private static Core core = Core.getInstance();

    /**
     * @return void
     * @Author https://github.com/ChaunceyCX
     * @Description //根据用户名发送文本消息
     * @Date 下午4:59 19-6-17
     * @Param [msg, toUserName]
     **/
    public static void sendMsgByUserName(String msg, String toUserName) {
        MessageTools.sendMsgById(msg, toUserName);
    }

    /**
     * @return java.lang.String
     * @Author https://github.com/ChaunceyCX
     * @Description [
     * <p>
     * 通过RealName获取本次UserName
     * </p>
     * <p>
     * 如NickName为"yaphone"，则获取UserName=
     * "@1212d3356aea8285e5bbe7b91229936bc183780a8ffa469f2d638bf0d2e4fc63"，
     * 可通过UserName发送消息
     * </p>
     * ]
     * @Date 下午5:00 19-6-17
     * @Param [nickName]
     **/
    public static String getUserNameByNickName(String nickName) {
		/*for (JSONObject o : core.getContactList()) {
			if (o.getString("NickName").equals(nickName)) {
				return o.getString("UserName");
			}
		}*/
        return null;
    }

    /**
     * @return java.util.List<java.lang.String>
     * @Author https://github.com/ChaunceyCX
     * @Description //返回好友昵称列表
     * @Date 下午5:02 19-6-17
     * @Param []
     **/
    public static List<String> getContactNickNameList() {
        List<String> contactNickNameList = new ArrayList<String>();
		/*for (JSONObject o : core.getContactList()) {
			contactNickNameList.add(o.getString("NickName"));
		}*/
        return contactNickNameList;
    }

    /**
     * @Author https://github.com/ChaunceyCX
     * @Description //返回好友完整信息列表
     * @Date 下午5:02 19-6-17
     * @Param []
     * @return java.util.List<com.alibaba.fastjson.JSONObject>
     **/
	/*public static List<JSONObject> getContactList() {
		return core.getContactList();
	}*/

    /**
     * @Author https://github.com/ChaunceyCX
     * @Description //返回群列表
     * @Date 下午5:02 19-6-17
     * @Param []
     * @return java.util.List<com.alibaba.fastjson.JSONObject>
     **/
	/*public static List<JSONObject> getGroupList() {
		return core.getGroupList();
	}*/

    /**
     * @return java.util.List<java.lang.String>
     * @Author https://github.com/ChaunceyCX
     * @Description //获取群ID列表
     * @Date 下午5:03 19-6-17
     * @Param []
     **/
    public static List<String> getGroupIdList() {
        return core.getGroupIdList();
    }

    /**
     * @return java.util.List<java.lang.String>
     * @Author https://github.com/ChaunceyCX
     * @Description //获取群NickName列表
     * @Date 下午5:03 19-6-17
     * @Param []
     **/
    public static List<String> getGroupNickNameList() {
        return core.getGroupNickNameList();
    }

    /**
     * @return com.alibaba.fastjson.JSONArray
     * @Author https://github.com/ChaunceyCX
     * @Description //根据groupIdList返回群成员列表
     * @Date 下午5:03 19-6-17
     * @Param [groupId]
     **/
    public static JSONArray getMemberListByGroupId(String groupId) {
        return core.getGroupMemeberMap().get(groupId);
    }

    /**
     * @return void
     * @Author https://github.com/ChaunceyCX
     * @Description //退出微信
     * @Date 下午5:03 19-6-17
     * @Param []
     **/
    public static void logout() {
        webWxLogout();
    }

    private static boolean webWxLogout() {
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
            LOG.debug(e.getMessage());
        }
        return false;
    }

    public static void setUserInfo() {
        for (Member member : core.getContactList()) {
            core.getUserInfoMap().put(member.getNickName(), member);
            core.getUserInfoMap().put(member.getUserName(), member);
        }
    }

    /**
     * @return void
     * @Author https://github.com/ChaunceyCX
     * @Description //根据用户昵称设置备注名称
     * @Date 下午5:04 19-6-17
     * @Param [nickName, remName]
     **/
    public static void remarkNameByNickName(String nickName, String remName) {
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
            LOG.info("修改备注" + remName);
        } catch (Exception e) {
            LOG.error("remarkNameByUserName", e);
        }
    }

    /**
     * @return boolean
     * @Author https://github.com/ChaunceyCX
     * @Description //获取微信在线状态
     * @Date 下午5:05 19-6-17
     * @Param []
     **/
    public static boolean getWechatStatus() {
        return core.isAlive();
    }

}
