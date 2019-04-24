package com.cherry.WeTeBot.service;

import com.cherry.WeTeBot.component.Token;
import com.cherry.WeTeBot.component.WeChat;
import com.cherry.WeTeBot.domain.request.component.BaseRequest;
import com.cherry.WeTeBot.domain.response.*;
import com.cherry.WeTeBot.component.ChatRoomDescription;
import com.cherry.WeTeBot.component.Contact;
import com.cherry.WeTeBot.enums.LoginCode;
import com.cherry.WeTeBot.enums.StatusNotifyCode;
import com.cherry.WeTeBot.exception.WechatException;
import com.cherry.WeTeBot.exception.WechatQRExpiredException;
import com.cherry.WeTeBot.utils.CookieUtils;
import com.cherry.WeTeBot.utils.FileUtils;
import com.cherry.WeTeBot.utils.QRCodeUtils;
import com.cherry.WeTeBot.utils.WechatUtils;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @ClassName WechatHttpService
 * @Description 微信相关接口的个性化附加
 * @Author chauncey
 * @Date 19-4-10 下午6:24
 * @Version 1.0
 **/

@Component
public class WechatHttpService {

    @Autowired
    private WechatHttpServiceInternal wechatHttpServiceInternal;
    @Autowired
    private WeChat weChat;
    private static final Logger logger = LoggerFactory.getLogger(WechatHttpService.class);

    /**
      * @Author https://github.com/ChaunceyCX
      * @Description //打开entry page
      * @Date 下午4:01 19-4-24
      * @Param [retryTimes] 二维码扫描重试时间
      * @return void
      **/

    public void open(int retryTimes){
        wechatHttpServiceInternal.open(retryTimes);
    }

    /**
      * @Author https://github.com/ChaunceyCX
      * @Description //获取UUID
      * @Date 下午4:05 19-4-24
      * @Param []
      * @return java.lang.String
      **/

    public String getUUID() {
        return wechatHttpServiceInternal.getUUID();
    }

    /**
      * @Author https://github.com/ChaunceyCX
      * @Description //获取二维码
      * @Date 下午4:08 19-4-24
      * @Param [uuid]
      * @return java.lang.String
      **/

    public String getQRStr(String uuid) throws WriterException, IOException, NotFoundException {
        byte[] qrData = wechatHttpServiceInternal.getQR(weChat.getUuid());
        ByteArrayInputStream stream = new ByteArrayInputStream(qrData);
        //TODO 保存二维码
        String qrUrl = QRCodeUtils.decode(stream);
        stream.close();
        return QRCodeUtils.generateQR(qrUrl, 40, 40);
    }

    /**
      * @Author https://github.com/ChaunceyCX
      * @Description //扫描状态监控
      * @Date 下午4:13 19-4-24
      * @Param []
      * @return void
      **/

    public void statReport() {
        wechatHttpServiceInternal.statReport();
    }

    /**
      * @Author https://github.com/ChaunceyCX
      * @Description //登陆
      * @Date 下午4:16 19-4-24
      * @Param [weChat]
      * @return com.cherry.WeTeBot.component.WeChat
      **/

    public WeChat login(WeChat weChat) {
        LoginResult loginResponse;
        while (true) {
            loginResponse = wechatHttpServiceInternal.login(weChat.getUuid());
            if (LoginCode.SUCCESS.getCode().equals(loginResponse.getCode())) {
                if (loginResponse.getHostUrl() == null) {
                    throw new WechatException("hostUrl can't be found");
                }
                if (loginResponse.getRedirectUrl() == null) {
                    throw new WechatException("redirectUrl can't be found");
                }
                weChat.setRedirectUrl(loginResponse.getRedirectUrl());
                weChat.setHostUrl(loginResponse.getHostUrl());
                if (loginResponse.getHostUrl().equals("https://wechat.com")) {
                    weChat.setSyncUrl("https://webpush.web.wechat.com");
                    weChat.setFileUrl("https://file.web.wechat.com");
                } else {
                    weChat.setSyncUrl(loginResponse.getHostUrl().replaceFirst("^https://", "https://webpush."));
                    weChat.setFileUrl(loginResponse.getHostUrl().replaceFirst("^https://", "https://file."));
                }
                break;
            } else if (LoginCode.AWAIT_CONFIRMATION.getCode().equals(loginResponse.getCode())) {
                logger.info("[*] login status = AWAIT_CONFIRMATION");
            } else if (LoginCode.AWAIT_SCANNING.getCode().equals(loginResponse.getCode())) {
                logger.info("[*] login status = AWAIT_SCANNING");
            } else if (LoginCode.EXPIRED.getCode().equals(loginResponse.getCode())) {
                logger.info("[*] login status = EXPIRED");
                throw new WechatQRExpiredException();
            } else {
                logger.info("[*] login status = " + loginResponse.getCode());
            }
        }
        return weChat;
    }

    /**
      * @Author https://github.com/ChaunceyCX
      * @Description //重定向登录页设置cookies等
      * @Date 下午4:23 19-4-24
      * @Param [weChat]
      * @return com.cherry.WeTeBot.component.WeChat
      **/

    public WeChat openNewloginpage(WeChat weChat) throws IOException {
        Token token = wechatHttpServiceInternal.openNewloginpage(weChat.getRedirectUrl());
        if (token.getRet() == 0) {
            weChat.setPassTicket(token.getPass_ticket());
            weChat.setsKey(token.getSkey());
            weChat.setSid(token.getWxsid());
            weChat.setUin(token.getWxuin());
            BaseRequest baseRequest = new BaseRequest();
            baseRequest.setUin(weChat.getUin());
            baseRequest.setSid(weChat.getSid());
            baseRequest.setSkey(weChat.getsKey());
            weChat.setBaseRequest(baseRequest);
            weChat.setCookies(CookieUtils.getCookiesFromList(token.getCookies()));
            weChat.setMaxAge(CookieUtils.getMaxAge(token.getStrictTransportSecurity()));
        } else {
            throw new WechatException("token ret = " + token.getRet());
        }
        return weChat;
    }

    /**
      * @Author https://github.com/ChaunceyCX
      * @Description //重定向到消息页
      * @Date 下午4:26 19-4-24
      * @Param [hostUrl]
      * @return void
      **/

    public void redirect(String hostUrl) {
        wechatHttpServiceInternal.redirect(hostUrl);
    }

    /**
      * @Author https://github.com/ChaunceyCX
      * @Description //初始化用户信息
      * @Date 下午4:28 19-4-24
      * @Param [weChat]
      * @return com.cherry.WeTeBot.component.WeChat
      **/

    public WeChat init(WeChat weChat) throws IOException {
        InitResponse initResponse = wechatHttpServiceInternal.init(weChat.getHostUrl(), weChat.getBaseRequest());
        weChat.setChatRoomDescriptions(
                initResponse.getContactList().stream()
                        .filter(x -> x != null && WechatUtils.isChatRoom(x))
                        .map(x -> {
                            ChatRoomDescription description = new ChatRoomDescription();
                            description.setUserName(x.getUserName());
                            return description;
                        })
                        .toArray(ChatRoomDescription[]::new)
        );
        WechatUtils.checkBaseResponse(initResponse);
        weChat.setSyncKey(initResponse.getSyncKey());
        weChat.setOwner(initResponse.getUser());
        return weChat;
    }

    /**
      * @Author https://github.com/ChaunceyCX
      * @Description //status notify
      * @Date 下午4:32 19-4-24
      * @Param [weChat]
      * @return void
      **/

    public void statusNotify(WeChat weChat) throws IOException {
        StatusNotifyResponse statusNotifyResponse =
                wechatHttpServiceInternal.statusNotify(weChat.getHostUrl(),
                        weChat.getBaseRequest(),
                        weChat.getOwner().getUserName(), StatusNotifyCode.INITED.getCode());
        WechatUtils.checkBaseResponse(statusNotifyResponse);
    }

    /**
      * @Author https://github.com/ChaunceyCX
      * @Description //获取打开的对话列表
      * @Date 下午4:39 19-4-24
      * @Param [weChat]
      * @return com.cherry.WeTeBot.component.WeChat
      **/

    public WeChat getContact(WeChat weChat) throws IOException {
        long seq = 0;
        do {
            GetContactResponse getContactResponse = wechatHttpServiceInternal.getContact(weChat.getHostUrl(), weChat.getBaseRequest().getSkey(), seq);
            WechatUtils.checkBaseResponse(getContactResponse);
            logger.info("[*] getContactResponse seq = " + getContactResponse.getSeq());
            logger.info("[*] getContactResponse memberCount = " + getContactResponse.getMemberCount());
            seq = getContactResponse.getSeq();
            weChat.getIndividuals().addAll(getContactResponse.getMemberList().stream().filter(WechatUtils::isIndividual).collect(Collectors.toSet()));
            weChat.getMediaPlatforms().addAll(getContactResponse.getMemberList().stream().filter(WechatUtils::isMediaPlatform).collect(Collectors.toSet()));
        } while (seq > 0);
        return weChat;
    }


    /**
     * Log out
     *
     * @throws IOException if logout fails
     */
    public void logout() throws IOException {
        wechatHttpServiceInternal.logout(weChat.getHostUrl(), weChat.getsKey());
    }

    /**
     * Get all the contacts
     *
     * @return contacts
     * @throws IOException if getContact fails
     */
    public Set<Contact> getContact() throws IOException {
        Set<Contact> contacts = new HashSet<>();
        long seq = 0;
        do {
            GetContactResponse response = wechatHttpServiceInternal.getContact(weChat.getHostUrl(), weChat.getsKey(), seq);
            WechatUtils.checkBaseResponse(response);
            seq = response.getSeq();
            contacts.addAll(response.getMemberList());
        }
        while (seq > 0);
        return contacts;
    }

    public WeChat batchGetContact(WeChat weChat) throws IOException {
        if (weChat.getChatRoomDescriptions().length > 0) {
            BatchGetContactResponse batchGetContactResponse = wechatHttpServiceInternal.batchGetContact(
                    weChat.getHostUrl(),
                    weChat.getBaseRequest(),
                    weChat.getChatRoomDescriptions());
            WechatUtils.checkBaseResponse(batchGetContactResponse);
            logger.info("[*] batchGetContactResponse count = " + batchGetContactResponse.getCount());
            weChat.getChatRooms().addAll(batchGetContactResponse.getContactList());
        }
        return weChat;
    }

    /**
     * Send plain text to a contact (not chatroom)
     *
     * @param userName the username of the contact
     * @param content  the content of text
     * @throws IOException if sendText fails
     */
    public void sendText(String userName, String content) throws IOException {
        notifyNecessary(userName);
        SendMsgResponse response = wechatHttpServiceInternal.sendText(weChat.getHostUrl(), weChat.getBaseRequest(), content, weChat.getOwner().getUserName(), userName);
        WechatUtils.checkBaseResponse(response);
    }

    /**
     * Set the alias of a contact
     *
     * @param userName the username of the contact
     * @param newAlias alias
     * @throws IOException if setAlias fails
     */
    public void setAlias(String userName, String newAlias) throws IOException {
        OpLogResponse response = wechatHttpServiceInternal.setAlias(weChat.getHostUrl(), weChat.getBaseRequest(), newAlias, userName);
        WechatUtils.checkBaseResponse(response);
    }

    /**
     * Get contacts in chatrooms
     *
     * @param list chatroom usernames
     * @return chatroom list
     * @throws IOException if batchGetContact fails
     */
    public Set<Contact> batchGetContact(Set<String> list) throws IOException {
        ChatRoomDescription[] descriptions =
                list.stream().map(x -> {
                    ChatRoomDescription description = new ChatRoomDescription();
                    description.setUserName(x);
                    return description;
                }).toArray(ChatRoomDescription[]::new);
        BatchGetContactResponse response = wechatHttpServiceInternal.batchGetContact(weChat.getHostUrl(), weChat.getBaseRequest(), descriptions);
        WechatUtils.checkBaseResponse(response);
        return response.getContactList();
    }

    /**
     * Create a chatroom with a topic.
     * In fact, a topic is usually not provided when creating the chatroom.
     *
     * @param userNames the usernames of the contacts who are invited to the chatroom.
     * @param topic     the topic(or nickname)
     * @throws IOException
     */
    public void createChatRoom(String[] userNames, String topic) throws IOException {
        CreateChatRoomResponse response = wechatHttpServiceInternal.createChatRoom(weChat.getHostUrl(), weChat.getBaseRequest(), userNames, topic);
        WechatUtils.checkBaseResponse(response);
        //invoke BatchGetContact after CreateChatRoom
        ChatRoomDescription description = new ChatRoomDescription();
        description.setUserName(response.getChatRoomName());
        ChatRoomDescription[] descriptions = new ChatRoomDescription[]{description};
        BatchGetContactResponse batchGetContactResponse = wechatHttpServiceInternal.batchGetContact(weChat.getHostUrl(), weChat.getBaseRequest(), descriptions);
        WechatUtils.checkBaseResponse(batchGetContactResponse);
        weChat.getChatRooms().addAll(batchGetContactResponse.getContactList());
    }

    /**
     * Delete a contact from a certain chatroom (if you're the owner!)
     *
     * @param chatRoomUserName chatroom username
     * @param userName         contact username
     * @throws IOException if remove chatroom member fails
     */
    public void deleteChatRoomMember(String chatRoomUserName, String userName) throws IOException {
        DeleteChatRoomMemberResponse response = wechatHttpServiceInternal.deleteChatRoomMember(weChat.getHostUrl(), weChat.getBaseRequest(), chatRoomUserName, userName);
        WechatUtils.checkBaseResponse(response);
    }

    /**
     * Invite a contact to a certain chatroom
     *
     * @param chatRoomUserName chatroom username
     * @param userName         contact username
     * @throws IOException if add chatroom member fails
     */
    public void addChatRoomMember(String chatRoomUserName, String userName) throws IOException {
        AddChatRoomMemberResponse response = wechatHttpServiceInternal.addChatRoomMember(weChat.getHostUrl(), weChat.getBaseRequest(), chatRoomUserName, userName);
        WechatUtils.checkBaseResponse(response);
        Contact chatRoom = weChat.getChatRooms().stream().filter(x -> chatRoomUserName.equals(x.getUserName())).findFirst().orElse(null);
        if (chatRoom == null) {
            throw new WechatException("can't find " + chatRoomUserName);
        }
        chatRoom.getMemberList().addAll(response.getMemberList());
    }

    /**
     * download images in the conversation. Note that it's better not to download image directly. This method has included cookies in the request.
     *
     * @param url image url
     * @return image data
     */
    public byte[] downloadImage(String url) {
        return wechatHttpServiceInternal.downloadImage(url);
    }

    /**
     * notify the server that all the messages in the conversation between {@code userName} and me have been read.
     *
     * @param userName the contact with whom I need to set the messages read.
     * @throws IOException if statusNotify fails.
     */
    private void notifyNecessary(String userName) throws IOException {
        if (userName == null) {
            throw new IllegalArgumentException("userName");
        }
        Set<String> unreadContacts = weChat.getContactNamesWithUnreadMessage();
        if (unreadContacts.contains(userName)) {
            wechatHttpServiceInternal.statusNotify(weChat.getHostUrl(), weChat.getBaseRequest(), userName, StatusNotifyCode.READED.getCode());
            unreadContacts.remove(userName);
        }
    }

    /**
      * @Author https://github.com/ChaunceyCX
      * @Description //发送图片
      * @Date 下午5:12 19-4-22
      * @Param
      * @return
      **/
    private void sendImg(){

    }
}
