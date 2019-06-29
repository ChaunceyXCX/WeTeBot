package com.chauncey.WeTeBot.utils.tools;

import com.chauncey.WeTeBot.enums.MsgTypeEnum;
import com.chauncey.WeTeBot.enums.URLEnum;
import com.chauncey.WeTeBot.model.BaseMsg;
import com.chauncey.WeTeBot.model.Core;
import com.chauncey.WeTeBot.utils.MyHttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 下载工具类
 *
 * @author https://github.com/yaphone
 * @version 1.0
 * @date 创建时间：2017年4月21日 下午11:18:46
 */
public class DownloadTools {
    private static Logger logger = Logger.getLogger("DownloadTools");
    private static Core core = Core.getInstance();
    private static MyHttpClient myHttpClient = core.getMyHttpClient();

    /**
     * @return java.lang.Object
     * @Author https://github.com/ChaunceyCX
     * @Description //处理下载任务
     * @Date 下午12:02 2019/6/29
     * @Param [msg, type, path]
     **/
    public static Object getDownloadFn(BaseMsg msg, String type, String path) {
        Map<String, String> headerMap = new HashMap<String, String>();
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        String url = "";
        if (type.equals(MsgTypeEnum.PIC.getType())) {
            url = String.format(URLEnum.WEB_WX_GET_MSG_IMG.getUrl(), (String) core.getLoginInfo().get("url"));
        } else if (type.equals(MsgTypeEnum.VOICE.getType())) {
            url = String.format(URLEnum.WEB_WX_GET_VOICE.getUrl(), (String) core.getLoginInfo().get("url"));
        } else if (type.equals(MsgTypeEnum.VIEDO.getType())) {
            headerMap.put("Range", "bytes=0-");
            url = String.format(URLEnum.WEB_WX_GET_VIEDO.getUrl(), (String) core.getLoginInfo().get("url"));
        } else if (type.equals(MsgTypeEnum.MEDIA.getType())) {
            headerMap.put("Range", "bytes=0-");
            url = String.format(URLEnum.WEB_WX_GET_MEDIA.getUrl(), (String) core.getLoginInfo().get("fileUrl"));
            params.add(new BasicNameValuePair("sender", msg.getFromUserName()));
            params.add(new BasicNameValuePair("mediaid", msg.getMediaId()));
            params.add(new BasicNameValuePair("filename", msg.getFileName()));
        }
        params.add(new BasicNameValuePair("msgid", msg.getNewMsgId()));
        params.add(new BasicNameValuePair("skey", (String) core.getLoginInfo().get("skey")));
        HttpEntity entity = myHttpClient.doGet(url, params, true, headerMap);
        try {
            OutputStream out = new FileOutputStream(path);
            byte[] bytes = EntityUtils.toByteArray(entity);
            out.write(bytes);
            out.flush();
            out.close();
            // Tools.printQr(path);

        } catch (Exception e) {
            logger.info(e.getMessage());
            return false;
        }
        return null;
    }

    ;

}
