package com.cherry.WeTeBot.component;

/**
 * @ClassName Cookies
 * @Description TODO
 * @Author https://github.com/ChaunceyCX
 * @Date 19-4-24 下午3:31
 * @Version 1.0
 **/
public class Cookies {
    private Long wxuin;
    private String webwx_auth_ticket;
    private String webwxuvid;
    private String webwx_data_ticket;
    private String wxloadtime;
    private String wxsid;

    public Long getWxuin() {
        return wxuin;
    }

    public void setWxuin(Long wxuin) {
        this.wxuin = wxuin;
    }

    public String getWebwx_auth_ticket() {
        return webwx_auth_ticket;
    }

    public void setWebwx_auth_ticket(String webwx_auth_ticket) {
        this.webwx_auth_ticket = webwx_auth_ticket;
    }

    public String getWebwxuvid() {
        return webwxuvid;
    }

    public void setWebwxuvid(String webwxuvid) {
        this.webwxuvid = webwxuvid;
    }

    public String getWebwx_data_ticket() {
        return webwx_data_ticket;
    }

    public void setWebwx_data_ticket(String webwx_data_ticket) {
        this.webwx_data_ticket = webwx_data_ticket;
    }

    public String getWxloadtime() {
        return wxloadtime;
    }

    public void setWxloadtime(String wxloadtime) {
        this.wxloadtime = wxloadtime;
    }

    public String getWxsid() {
        return wxsid;
    }

    public void setWxsid(String wxsid) {
        this.wxsid = wxsid;
    }
}
