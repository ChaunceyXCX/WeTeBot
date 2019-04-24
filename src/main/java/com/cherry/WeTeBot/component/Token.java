package com.cherry.WeTeBot.component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "error")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Token {
    @JacksonXmlProperty(localName = "ret")
    private int ret;
    @JacksonXmlProperty(localName = "message")
    private String message;
    @JacksonXmlProperty(localName = "skey")
    private String skey;
    @JacksonXmlProperty(localName = "wxsid")
    private String wxsid;
    @JacksonXmlProperty(localName = "wxuin")
    private String wxuin;
    @JacksonXmlProperty(localName = "pass_ticket")
    private String pass_ticket;
    @JacksonXmlProperty(localName = "isgrayscale")
    private int isgrayscale;

    private List<String> cookies;

    private List<String> strictTransportSecurity;

    public List<String> getCookies() {
        return cookies;
    }

    public void setCookies(List<String> cookies) {
        this.cookies = cookies;
    }

    public List<String> getStrictTransportSecurity() {
        return strictTransportSecurity;
    }

    public void setStrictTransportSecurity(List<String> strictTransportSecurity) {
        this.strictTransportSecurity = strictTransportSecurity;
    }

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSkey() {
        return skey;
    }

    public void setSkey(String skey) {
        this.skey = skey;
    }

    public String getWxsid() {
        return wxsid;
    }

    public void setWxsid(String wxsid) {
        this.wxsid = wxsid;
    }

    public String getWxuin() {
        return wxuin;
    }

    public void setWxuin(String wxuin) {
        this.wxuin = wxuin;
    }

    public String getPass_ticket() {
        return pass_ticket;
    }

    public void setPass_ticket(String pass_ticket) {
        this.pass_ticket = pass_ticket;
    }

    public int getIsgrayscale() {
        return isgrayscale;
    }

    public void setIsgrayscale(int isgrayscale) {
        this.isgrayscale = isgrayscale;
    }
}