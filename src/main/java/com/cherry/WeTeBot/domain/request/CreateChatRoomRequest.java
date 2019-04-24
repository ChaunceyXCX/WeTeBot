package com.cherry.WeTeBot.domain.request;


import com.cherry.WeTeBot.domain.request.component.BaseRequest;
import com.cherry.WeTeBot.component.ChatRoomMember;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateChatRoomRequest {
    @JsonProperty
    private com.cherry.WeTeBot.domain.request.component.BaseRequest BaseRequest;
    @JsonProperty
    private int MemberCount;
    @JsonProperty
    private ChatRoomMember[] MemberList;
    @JsonProperty
    private String Topic;

    public BaseRequest getBaseRequest() {
        return BaseRequest;
    }

    public void setBaseRequest(BaseRequest baseRequest) {
        BaseRequest = baseRequest;
    }

    public int getMemberCount() {
        return MemberCount;
    }

    public void setMemberCount(int memberCount) {
        MemberCount = memberCount;
    }

    public ChatRoomMember[] getMemberList() {
        return MemberList;
    }

    public void setMemberList(ChatRoomMember[] memberList) {
        MemberList = memberList;
    }

    public String getTopic() {
        return Topic;
    }

    public void setTopic(String topic) {
        Topic = topic;
    }
}
