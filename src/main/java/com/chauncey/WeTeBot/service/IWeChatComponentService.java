package com.chauncey.WeTeBot.service;

import com.alibaba.fastjson.JSONArray;
import com.chauncey.WeTeBot.model.wechat.BaseMsg;

import java.util.List;

/**
 * @ClassName IWeChatComponentService
 * @Description TODO
 * @Author https://github.com/ChaunceyCX
 * @Date 2019/8/9 下午4:11
 * @Version 1.0
 **/
public interface IWeChatComponentService {


    /**
     * @return void
     * @Author https://github.com/ChaunceyCX
     * @Description //根据用户的微信ID发送文本消息
     * @Date 下午4:59 19-6-17
     * @Param [msg, toUserName]
     **/
    void sendTextMsgByWeId(String msg, String toUserName);


    /**
     * @param filePath
     * @param fileName
     * @return boolean
     * @throws
     * @despretion 根据用户微信ID发送图片文件
     * @author https://github.com/ChaunceyCX
     * @date 2019/8/9 下午4:47
     */
    boolean sendPicMsgByWeId(String filePath, String fileName);

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
    String getUserNameByNickName(String nickName);

    /**
     * @return java.util.List<java.lang.String>
     * @Author https://github.com/ChaunceyCX
     * @Description //返回好友昵称列表
     * @Date 下午5:02 19-6-17
     * @Param []
     **/
    List<String> getContactNickNameList();

    /**
     * @Author https://github.com/ChaunceyCX
     * @Description //返回好友完整信息列表
     * @Date 下午5:02 19-6-17
     * @Param []
     * @return java.util.List<com.alibaba.fastjson.JSONObject>
     **/
	/*public List<JSONObject> getContactList() {
		return core.getContactList();
	}*/

    /**
     * @Author https://github.com/ChaunceyCX
     * @Description //返回群列表
     * @Date 下午5:02 19-6-17
     * @Param []
     * @return java.util.List<com.alibaba.fastjson.JSONObject>
     **/
	/*public List<JSONObject> getGroupList() {
		return core.getGroupList();
	}*/

    /**
     * @return java.util.List<java.lang.String>
     * @Author https://github.com/ChaunceyCX
     * @Description //获取群ID列表
     * @Date 下午5:03 19-6-17
     * @Param []
     **/
    List<String> getGroupIdList();

    /**
     * @return java.util.List<java.lang.String>
     * @Author https://github.com/ChaunceyCX
     * @Description //获取群NickName列表
     * @Date 下午5:03 19-6-17
     * @Param []
     **/
    List<String> getGroupNickNameList();

    /**
     * @return com.alibaba.fastjson.JSONArray
     * @Author https://github.com/ChaunceyCX
     * @Description //根据groupIdList返回群成员列表
     * @Date 下午5:03 19-6-17
     * @Param [groupId]
     **/
    JSONArray getMemberListByGroupId(String groupId);

    /**
     * @return void
     * @Author https://github.com/ChaunceyCX
     * @Description //退出微信
     * @Date 下午5:03 19-6-17
     * @Param []
     **/
    boolean logout();


    /**
     * @return boolean
     * @Author https://github.com/ChaunceyCX
     * @Description //获取微信在线状态
     * @Date 下午5:05 19-6-17
     * @Param []
     **/
    boolean getWechatStatus();

    /**
     * @return void
     * @Author https://github.com/ChaunceyCX
     * @Description //根据用户昵称设置备注名称
     * @Date 下午5:04 19-6-17
     * @Param [nickName, remName]
     **/
    void remarkNameByNickName(String nickName, String remName);


    /**
     * @param baseMsg
     * @param accepted
     * @return boolean
     * @throws
     * @despretion 处理好友添加请求(true : 同意)
     * @author https://github.com/ChaunceyCX
     * @date 2019/8/9 下午5:01
     */
    boolean addFriend(BaseMsg baseMsg, boolean accepted);

    /**
     * @param baseMsg
     * @param type
     * @param savePath
     * @return java.lang.Object
     * @throws
     * @despretion 下载文件消息:图片,语音,其他
     * @author https://github.com/ChaunceyCX
     * @date 2019/8/9 下午5:04
     */
    Object downloadFile(BaseMsg baseMsg, String type, String savePath);

}
