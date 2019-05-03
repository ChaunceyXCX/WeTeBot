package com.cherry.WeTeBot.service.impl;

import com.cherry.WeTeBot.component.*;
import com.cherry.WeTeBot.service.MessageHandler;
import com.cherry.WeTeBot.service.WechatHttpService;
import com.cherry.WeTeBot.utils.FileUtils;
import com.cherry.WeTeBot.utils.MessageUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MessageHandlerImpl implements MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(MessageHandlerImpl.class);
    @Autowired
    private WechatHttpService wechatHttpService;
    @Value("${img.root-path}")
    private String imgRootPath;

    @Override
    public void onReceivingChatRoomTextMessage(Message message) throws IOException {
        logger.info("onReceivingChatRoomTextMessage");
        logger.info("from chatroom: " + message.getToUserName() + "\n\t" + message.getFromUserName());
        String content = MessageUtils.getChatRoomTextMessageContent(message.getContent());
        message.setContent(content);
        logger.info("from person: " + MessageUtils.getSenderOfChatRoomTextMessage(message.getContent()));
        logger.info("content:" + content);
        replyMessage(message);
    }

    @Override
    public void onReceivingChatRoomImageMessage(Message message, String thumbImageUrl, String fullImageUrl, String myID) {
        logger.info("onReceivingChatRoomImageMessage");
        logger.info("thumbImageUrl:" + thumbImageUrl);
        logger.info("fullImageUrl:" + fullImageUrl);
        byte[] datas = wechatHttpService.downloadImage(thumbImageUrl);
        try {
            logger.info("imgSavePath: "+ FileUtils.saveImg(message,myID,datas,imgRootPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceivingPrivateTextMessage(Message message) throws IOException {
        logger.info("onReceivingPrivateTextMessage");
        logger.info("from: " + message.getFromUserName());
        logger.info("to: " + message.getToUserName());
        logger.info("content:" + message.getContent());
//        将原文回复给对方
        replyMessage(message);
    }

    @Override
    public void onReceivingPrivateImageMessage(WeChat weChat, Message message, String thumbImageUrl, String fullImageUrl, String myID) {
        logger.info("onReceivingPrivateImageMessage");
        logger.info("thumbImageUrl:" + thumbImageUrl);
        logger.info("fullImageUrl:" + fullImageUrl);
//        将图片保存在本地
        byte[] datas = wechatHttpService.downloadImage(thumbImageUrl);
        try {
            String imgSavePath = FileUtils.saveImg(message,myID,datas,imgRootPath);
            replayImg(weChat, imgSavePath, true);
            logger.info("imgSavePath: "+ imgSavePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onReceivingFriendInvitation(RecommendInfo info) {
        logger.info("onReceivingFriendInvitation");
        logger.info("recommendinfo content:" + info.getContent());
//        默认接收所有的邀请
        return true;
    }

    @Override
    public void postAcceptFriendInvitation(Message message) throws IOException {
        logger.info("postAcceptFriendInvitation");
//        将该用户的微信号设置成他的昵称
        String content = StringEscapeUtils.unescapeXml(message.getContent());
        ObjectMapper xmlMapper = new XmlMapper();
        FriendInvitationContent friendInvitationContent = xmlMapper.readValue(content, FriendInvitationContent.class);
        wechatHttpService.setAlias(message.getRecommendInfo().getUserName(), friendInvitationContent.getFromusername());
    }

    @Override
    public void onChatRoomMembersChanged(Contact chatRoom, Set<ChatRoomMember> membersJoined, Set<ChatRoomMember> membersLeft) {
        logger.info("onChatRoomMembersChanged");
        logger.info("chatRoom:" + chatRoom.getUserName());
        if (membersJoined != null && membersJoined.size() > 0) {
            logger.info("membersJoined:" + String.join(",", membersJoined.stream().map(ChatRoomMember::getNickName).collect(Collectors.toList())));
        }
        if (membersLeft != null && membersLeft.size() > 0) {
            logger.info("membersLeft:" + String.join(",", membersLeft.stream().map(ChatRoomMember::getNickName).collect(Collectors.toList())));
        }
    }

    @Override
    public void onNewChatRoomsFound(Set<Contact> chatRooms) {
        logger.info("onNewChatRoomsFound");
        chatRooms.forEach(x -> logger.info(x.getUserName()));
    }

    @Override
    public void onChatRoomsDeleted(Set<Contact> chatRooms) {
        logger.info("onChatRoomsDeleted");
        chatRooms.forEach(x -> logger.info(x.getUserName()));
    }

    @Override
    public void onNewFriendsFound(Set<Contact> contacts) {
        logger.info("onNewFriendsFound");
        contacts.forEach(x -> {
            logger.info(x.getUserName());
            logger.info(x.getNickName());
        });
    }

    @Override
    public void onFriendsDeleted(Set<Contact> contacts) {
        logger.info("onFriendsDeleted");
        contacts.forEach(x -> {
            logger.info(x.getUserName());
            logger.info(x.getNickName());
        });
    }

    @Override
    public void onNewMediaPlatformsFound(Set<Contact> mps) {
        logger.info("onNewMediaPlatformsFound");
    }

    @Override
    public void onMediaPlatformsDeleted(Set<Contact> mps) {
        logger.info("onMediaPlatformsDeleted");
    }

    @Override
    public void onRedPacketReceived(Contact contact) {
        logger.info("onRedPacketReceived");
        if (contact != null) {
            logger.info("the red packet is from " + contact.getNickName());
        }
    }

    @Override
    public void onReceivingChatRoomTextMessageSync(Message message) {
        logger.info("onReceivingChatRoomTextMessageSync");
        logger.info("from chatroom: " + message.getToUserName() + "\n\t" + message.getFromUserName());
        logger.info("from person: " + MessageUtils.getSenderOfChatRoomTextMessage(message.getContent()));
        logger.info("content:" + message.getContent());
        try {
            replyMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void replyMessage(Message message) throws IOException {
        wechatHttpService.sendText(message.getFromUserName(), message.getContent());
    }

    private void replayImg(WeChat weChat, String filePath, Boolean isImg) {
        wechatHttpService.fileUpload(weChat,filePath,isImg);
    }
}
