package com.cherry.WeTeBot.domain.response;

import com.cherry.WeTeBot.domain.response.component.WechatHttpResponseBase;
import com.cherry.WeTeBot.component.Contact;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchGetContactResponse extends WechatHttpResponseBase {
    @JsonProperty
    private Set<Contact> ContactList;
    @JsonProperty
    private int Count;

    public Set<Contact> getContactList() {
        return ContactList;
    }

    public void setContactList(Set<Contact> contactList) {
        ContactList = contactList;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }
}
