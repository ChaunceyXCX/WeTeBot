package com.cherry.WeTeBot.utils;

import com.cherry.WeTeBot.component.Cookies;

import java.util.List;

/**
 * @ClassName CookieUtils
 * @Description TODO
 * @Author https://github.com/ChaunceyCX
 * @Date 19-4-24 下午5:18
 * @Version 1.0
 **/
public class CookieUtils {
    public static Cookies getCookiesFromList(List<String> cookiesList) {
        Cookies cookies = new Cookies();
        for (String cookieItemStr: cookiesList) {
            for (String cookie: cookieItemStr.split("; ")) {
                String[] cookieKeyValues = cookie.split("=");
                if (cookieKeyValues.length>0){
                    if (cookieKeyValues[0].equals("wxuin")){
                        cookies.setWxuin(Long.valueOf(cookieKeyValues[1]));
                    }else if (cookieKeyValues[0].equals("wxsid")) {
                        cookies.setWxsid(cookieKeyValues[1]);
                    }else if (cookieKeyValues[0].equals("wxloadtime")) {
                        cookies.setWxloadtime(cookieKeyValues[1]);
                    }else if (cookieKeyValues[0].equals("webwx_data_ticket")) {
                        cookies.setWebwx_data_ticket(cookieKeyValues[1]);
                    }else if (cookieKeyValues[0].equals("webwxuvid")) {
                        cookies.setWebwxuvid(cookieKeyValues[1]);
                    }else if (cookieKeyValues[0].equals("webwx_auth_ticket")) {
                        cookies.setWebwx_auth_ticket(cookieKeyValues[1]);
                    }
                }
            }
        }
        return cookies;
    }

    public static Long getMaxAge(List<String> strictTransportSecurity) {
        String strictTransportSecurityItem = null;
        if (strictTransportSecurity.size()>0){
            strictTransportSecurityItem = strictTransportSecurity.get(0);
            String[] keyValues = strictTransportSecurityItem.split("=");
            if (keyValues.length>0 && keyValues[0].equals("max-age")){
                return Long.valueOf(keyValues[1]);
            }
        }
        return Long.valueOf(0);
    }
}
