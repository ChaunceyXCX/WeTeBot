package com.cherry.WeTeBot.service;

import com.cherry.WeTeBot.component.*;
import com.cherry.WeTeBot.service.impl.MessageHandlerImpl;
import com.cherry.WeTeBot.enums.MessageType;
import com.cherry.WeTeBot.enums.Selector;
import com.cherry.WeTeBot.exception.WechatException;
import com.cherry.WeTeBot.utils.WechatUtils;
import com.cherry.WeTeBot.domain.response.SyncCheckResponse;
import com.cherry.WeTeBot.domain.response.SyncResponse;
import com.cherry.WeTeBot.domain.response.VerifyUserResponse;
import com.cherry.WeTeBot.enums.RetCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SyncServie {
    private static final Logger logger = LoggerFactory.getLogger(SyncServie.class);
    @Autowired
    private WeChat weChat;
    @Autowired
    private WechatHttpServiceInternal wechatHttpService;
    @Autowired(required = false)
    private MessageHandler messageHandler;

    @Value("${wechat.url.get_msg_img}")
    private String WECHAT_URL_GET_MSG_IMG;

    @Value("${wechat.url.upload_media}")
    private String WECHAT_URL_UPLOAD_MEDIA;

    private String myID = null;

    private final static String RED_PACKET_CONTENT = "收到红包，请在手机上查看";

    @PostConstruct
    public void setMessageHandler() {
        if (messageHandler == null) {
            this.messageHandler = new MessageHandlerImpl();
        }
    }

    public void listen() throws IOException, URISyntaxException {
        SyncCheckResponse syncCheckResponse = wechatHttpService.syncCheck(
                weChat.getSyncUrl(),
                weChat.getBaseRequest().getUin(),
                weChat.getBaseRequest().getSid(),
                weChat.getBaseRequest().getSkey(),
                weChat.getSyncKey());
        int retCode = syncCheckResponse.getRetcode();
        int selector = syncCheckResponse.getSelector();
        logger.info(String.format("[SYNCCHECK] retcode = %s, selector = %s", retCode, selector));
        if (retCode == RetCode.NORMAL.getCode()) {
            //有新消息
            if (selector == Selector.NEW_MESSAGE.getCode()) {
                onNewMessage();
            } else if (selector == Selector.ENTER_LEAVE_CHAT.getCode()) {
                sync();
            } else if (selector == Selector.CONTACT_UPDATED.getCode()) {
                sync();
            } else if (selector == Selector.UNKNOWN1.getCode()) {
                sync();
            } else if (selector == Selector.UNKNOWN6.getCode()) {
                sync();
            } /*else if (selector != Selector.NORMAL.getCode()) {
                throw new WechatException("syncCheckResponse ret = " + retCode + "; selector = " + selector);
            }*/
        } else {
            throw new WechatException("syncCheckResponse selector = " + selector);
        }
    }

    private SyncResponse sync() throws IOException {
        SyncResponse syncResponse = wechatHttpService.sync(weChat.getHostUrl(), weChat.getSyncKey(), weChat.getBaseRequest());
        WechatUtils.checkBaseResponse(syncResponse);
        weChat.setSyncKey(syncResponse.getSyncKey());
        weChat.setSyncCheckKey(syncResponse.getSyncCheckKey());
        //mod包含新增和修改
        if (syncResponse.getModContactCount() > 0) {
            onContactsModified(syncResponse.getModContactList());
        }
        //del->联系人移除
        if (syncResponse.getDelContactCount() > 0) {
            onContactsDeleted(syncResponse.getDelContactList());
        }
        return syncResponse;
    }

    /**
      * @Author https://github.com/ChaunceyCX
      * @Description //接收好友邀请
      * @Date 19-4-15 下午2:58
      * @MethodName acceptFriendInvitation
      * @Param [info]
      * @return void
      **/
    private void acceptFriendInvitation(RecommendInfo info) throws IOException, URISyntaxException {
        VerifyUser user = new VerifyUser();
        user.setValue(info.getUserName());
        user.setVerifyUserTicket(info.getTicket());
        VerifyUserResponse verifyUserResponse = wechatHttpService.acceptFriend(
                weChat.getHostUrl(),
                weChat.getBaseRequest(),
                weChat.getPassTicket(),
                new VerifyUser[]{user}
        );
        WechatUtils.checkBaseResponse(verifyUserResponse);
    }

    /**
     * @Author https://github.com/ChaunceyCX
     * @Description //收到新消息
     * @Date 19-4-15 下午2:54
     * @MethodName onNewMessage
     * @Param []
     * @return void
     **/
    private void onNewMessage() throws IOException, URISyntaxException {
        SyncResponse syncResponse = sync();
        if (messageHandler == null) {
            return;
        }
        for (Message message : syncResponse.getAddMsgList()) {
            //文本消息
            if (message.getMsgType() == MessageType.TEXT.getCode()) {
                //个人 resive
                if (isMessageFromIndividual(message)) {
                    // logger.info("info: the message from friend to me");
                    weChat.getContactNamesWithUnreadMessage().add(message.getFromUserName());
                    messageHandler.onReceivingPrivateTextMessage(message);
                }
                //个人 sync
                else if (isMessageFromIndividualSync(message)){
                    // logger.info("info: the message from myself to sync");
                    weChat.getContactNamesWithUnreadMessage().add(message.getFromUserName());
                    messageHandler.onReceivingPrivateTextMessage(message);
                }
                //群
                else if (isMessageFromChatRoom(message)) {
                    // logger.info("info: the message from myself to sync");
                    messageHandler.onReceivingChatRoomTextMessage(message);
                }
                //群 sync
                else if (isMessageFromChatRoomSync(message)){
                    // logger.info("info: the message from myself to sync");
                    messageHandler.onReceivingChatRoomTextMessageSync(message);
                }
                //图片
            } else if (message.getMsgType() == MessageType.IMAGE.getCode()) {
                String fullImageUrl = String.format(WECHAT_URL_GET_MSG_IMG, weChat.getHostUrl(), message.getMsgId(), weChat.getsKey().replace("@","%40"));
                String thumbImageUrl = fullImageUrl + "&type=slave";
                //logger.info(cacheService.getsKey());
                //个人
                if (isMessageFromIndividual(message)) {
                    weChat.getContactNamesWithUnreadMessage().add(message.getFromUserName());
                    messageHandler.onReceivingPrivateImageMessage(message, thumbImageUrl, fullImageUrl, myID);
                }
                //个人sync
                else if(isMessageFromIndividualSync(message)){
                    messageHandler.onReceivingPrivateImageMessage(message, thumbImageUrl, fullImageUrl, myID);
                }
                //群
                else if (isMessageFromChatRoom(message)) {
                    weChat.getContactNamesWithUnreadMessage().add(message.getFromUserName());
                    messageHandler.onReceivingChatRoomImageMessage(message, thumbImageUrl, fullImageUrl, myID);
                }
                //个人在群sync
                else if (isMessageFromChatRoomSync(message)){
                    messageHandler.onReceivingChatRoomImageMessage(message,thumbImageUrl,fullImageUrl,myID);
                }
            }
            //语音
            else if (message.getMsgType() == MessageType.VOICE.getCode()) {
                //个人
                if(isMessageFromIndividual(message)) {
                    weChat.getContactNamesWithUnreadMessage().add(message.getFromUserName());

                }
                //个人sync
                else if(isMessageFromIndividualSync(message)) {

                }
                //群
                else if(isMessageFromChatRoom(message)) {
                    weChat.getContactNamesWithUnreadMessage().add(message.getFromUserName());

                }
                //个人在群sync
                else if (isMessageFromChatRoomSync(message)) {

                }
            }
            //系统消息
            else if (message.getMsgType() == MessageType.SYS.getCode()) {
                //红包
                if (RED_PACKET_CONTENT.equals(message.getContent())) {
                    logger.info("[*] you've received a red packet");
                    String from = message.getFromUserName();
                    Set<Contact> contacts = null;
                    //个人
                    if (isMessageFromIndividual(message)) {
                        contacts = weChat.getIndividuals();
                    }
                    //群
                    else if (isMessageFromChatRoom(message)) {
                        contacts = weChat.getChatRooms();
                    }
                    if (contacts != null) {
                        Contact contact = contacts.stream().filter(x -> Objects.equals(x.getUserName(), from)).findAny().orElse(null);
                        messageHandler.onRedPacketReceived(contact);
                    }
                }
            }
            //好友邀请
            else if (message.getMsgType() == MessageType.VERIFYMSG.getCode() && weChat.getOwner().getUserName().equals(message.getToUserName())) {
                if (messageHandler.onReceivingFriendInvitation(message.getRecommendInfo())) {
                    acceptFriendInvitation(message.getRecommendInfo());
                    logger.info("[*] you've accepted the invitation");
                    messageHandler.postAcceptFriendInvitation(message);
                } else {
                    logger.info("[*] you've declined the invitation");
                    //TODO decline invitation
                }
            }else if(message.getMsgType() == MessageType.STATUSNOTIFY.getCode()){
                logger.info("login init");
                logger.info("current userID: "+message.getFromUserName());
                this.myID = message.getFromUserName();
            }

        }
    }

    /**
      * @Author https://github.com/ChaunceyCX
      * @Description //判断消息是否来自群同步
      * @Date 19-4-15 下午6:53
      * @MethodName isMessageFromChatRoomSync
      * @Param [message]
      * @return boolean
      **/
    private boolean isMessageFromChatRoomSync(Message message) {
        return message.getFromUserName() != null
                &&message.getFromUserName().startsWith("@")
                && !message.getFromUserName().startsWith("@@")
                && message.getToUserName().startsWith("@@");
    }

    /**
     * @Author https://github.com/ChaunceyCX
     * @Description 判断消息是否来自好友
     * @Date 19-4-15 下午2:51
     * @MethodName isMessageFromIndividual
     * @Param [message]
     * @return boolean
     **/
    private boolean isMessageFromIndividual(Message message) {
        return message.getFromUserName() != null
                && myID !=null
                && !message.getFromUserName().equals(myID)
                && message.getFromUserName().startsWith("@")
                && !message.getFromUserName().startsWith("@@")
                && message.getToUserName().startsWith("@")
                && !message.getToUserName().startsWith("@@")
                && !message.getToUserName().equals(message.getFromUserName())
                && !message.getToUserName().startsWith("@@");
    }

    /**
      * @Author https://github.com/ChaunceyCX
      * @Description //判断消息是否来自群
      * @Date 19-4-15 下午2:52
      * @MethodName isMessageFromChatRoom
      * @Param [message]
      * @return boolean
      **/
    private boolean isMessageFromChatRoom(Message message) {
        return message.getFromUserName() != null && message.getFromUserName().startsWith("@@");
    }

    /**
      * @Author https://github.com/ChaunceyCX
      * @Description //判断是否自己发送消息同步
      * @Date 19-4-15 下午3:08
      * @MethodName isMessageFromIndividualSync
      * @Param [message]
      * @return boolean
      **/
    private boolean isMessageFromIndividualSync(Message message) {
        return message.getFromUserName()!=null
                && myID !=null
                && message.getFromUserName().equals(myID)
                && message.getToUserName().startsWith("@")
                && !message.getToUserName().startsWith("@@");
    }

    /**
      * @Author https://github.com/ChaunceyCX
      * @Description //会话列表更新
      * @Date 19-4-15 下午2:56
      * @MethodName onContactsModified
      * @Param [contacts]
      * @return void
      **/
    private void onContactsModified(Set<Contact> contacts) {
        Set<Contact> individuals = new HashSet<>();
        Set<Contact> chatRooms = new HashSet<>();
        Set<Contact> mediaPlatforms = new HashSet<>();

        for (Contact contact : contacts) {
            if (WechatUtils.isIndividual(contact)) {
                individuals.add(contact);
            } else if (WechatUtils.isMediaPlatform(contact)) {
                mediaPlatforms.add(contact);
            } else if (WechatUtils.isChatRoom(contact)) {
                chatRooms.add(contact);
            }
        }

        //individual
        if (individuals.size() > 0) {
            Set<Contact> existingIndividuals = weChat.getIndividuals();
            Set<Contact> newIndividuals = individuals.stream().filter(x -> !existingIndividuals.contains(x)).collect(Collectors.toSet());
            individuals.forEach(x -> {
                existingIndividuals.remove(x);
                existingIndividuals.add(x);
            });
            if (messageHandler != null && newIndividuals.size() > 0) {
                messageHandler.onNewFriendsFound(newIndividuals);
            }
        }
        //chatroom
        if (chatRooms.size() > 0) {
            Set<Contact> existingChatRooms = weChat.getChatRooms();
            Set<Contact> newChatRooms = new HashSet<>();
            Set<Contact> modifiedChatRooms = new HashSet<>();
            for (Contact chatRoom : chatRooms) {
                if (existingChatRooms.contains(chatRoom)) {
                    modifiedChatRooms.add(chatRoom);
                } else {
                    newChatRooms.add(chatRoom);
                }
            }
            existingChatRooms.addAll(newChatRooms);
            if (messageHandler != null && newChatRooms.size() > 0) {
                messageHandler.onNewChatRoomsFound(newChatRooms);
            }
            for (Contact chatRoom : modifiedChatRooms) {
                Contact existingChatRoom = existingChatRooms.stream().filter(x -> x.getUserName().equals(chatRoom.getUserName())).findFirst().orElse(null);
                if (existingChatRoom == null) {
                    continue;
                }
                existingChatRooms.remove(existingChatRoom);
                existingChatRooms.add(chatRoom);
                if (messageHandler != null) {
                    Set<ChatRoomMember> oldMembers = existingChatRoom.getMemberList();
                    Set<ChatRoomMember> newMembers = chatRoom.getMemberList();
                    Set<ChatRoomMember> joined = newMembers.stream().filter(x -> !oldMembers.contains(x)).collect(Collectors.toSet());
                    Set<ChatRoomMember> left = oldMembers.stream().filter(x -> !newMembers.contains(x)).collect(Collectors.toSet());
                    if (joined.size() > 0 || left.size() > 0) {
                        messageHandler.onChatRoomMembersChanged(chatRoom, joined, left);
                    }
                }
            }
        }
        if (mediaPlatforms.size() > 0) {
            //media platform
            Set<Contact> existingPlatforms = weChat.getMediaPlatforms();
            Set<Contact> newMediaPlatforms = existingPlatforms.stream().filter(x -> !existingPlatforms.contains(x)).collect(Collectors.toSet());
            mediaPlatforms.forEach(x -> {
                existingPlatforms.remove(x);
                existingPlatforms.add(x);
            });
            if (messageHandler != null && newMediaPlatforms.size() > 0) {
                messageHandler.onNewMediaPlatformsFound(newMediaPlatforms);
            }
        }
    }

    /**
      * @Author https://github.com/ChaunceyCX
      * @Description //删除会话列表
      * @Date 19-4-15 下午2:57
      * @MethodName onContactsDeleted
      * @Param [contacts]
      * @return void
      **/
    private void onContactsDeleted(Set<Contact> contacts) {
        Set<Contact> individuals = new HashSet<>();
        Set<Contact> chatRooms = new HashSet<>();
        Set<Contact> mediaPlatforms = new HashSet<>();
        for (Contact contact : contacts) {
            if (WechatUtils.isIndividual(contact)) {
                individuals.add(contact);
                weChat.getIndividuals().remove(contact);
            } else if (WechatUtils.isChatRoom(contact)) {
                chatRooms.add(contact);
                weChat.getChatRooms().remove(contact);
            } else if (WechatUtils.isMediaPlatform(contact)) {
                mediaPlatforms.add(contact);
                weChat.getMediaPlatforms().remove(contact);
            }
        }
        if (messageHandler != null) {
            if (individuals.size() > 0) {
                messageHandler.onFriendsDeleted(individuals);
            }
            if (chatRooms.size() > 0) {
                messageHandler.onChatRoomsDeleted(chatRooms);
            }
            if (mediaPlatforms.size() > 0) {
                messageHandler.onMediaPlatformsDeleted(mediaPlatforms);
            }
        }
    }
}
