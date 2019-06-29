package com.chauncey.WeTeBot.model;

import com.alibaba.fastjson.JSONArray;
import com.chauncey.WeTeBot.enums.parameters.BaseParaEnum;
import com.chauncey.WeTeBot.utils.MyHttpClient;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author https://github.com/ChaunceyCX
 * @Description //核心存储类，全局只保存一份，单例模式
 * @Date 下午5:07 19-6-17
 * @Param
 * @return
 **/
@Data
public class Core {

    private static Core instance;

    private Core() {

    }

    public static Core getInstance() {
        if (instance == null) {
            synchronized (Core.class) {
                instance = new Core();
            }
        }
        return instance;
    }

    boolean alive = false;
    private int memberCount = 0;

    private String indexUrl;

    private User userSelf; // 登陆账号自身信息
    private List<String> groupIdList = new ArrayList<String>(); // 群ID列表

    private List<BaseMsg> msgList = new ArrayList<BaseMsg>();

    private List<Group> groups;

    //``````````````
    private List<Member> memberList = new ArrayList<Member>(); // 好友+群聊+公众号+特殊账号
    private List<Member> contactList = new ArrayList<Member>();// 好友
    private List<Member> groupList = new ArrayList<Member>();
    ; // 群
    private Map<String, JSONArray> groupMemeberMap = new HashMap<String, JSONArray>(); // 群聊成员字典
    private List<Member> publicUsersList = new ArrayList<Member>();
    ;// 公众号／服务号
    private List<Member> specialUsersList = new ArrayList<Member>();
    ;// 特殊账号
    private List<String> groupNickNameList = new ArrayList<String>(); // 群NickName列表

    private Map<String, Member> userInfoMap = new HashMap<String, Member>();
    //``````````````````


    Map<String, Object> loginInfo = new HashMap<String, Object>();
    // CloseableHttpClient httpClient = HttpClients.createDefault();
    MyHttpClient myHttpClient = MyHttpClient.getInstance();
    String uuid = null;

    boolean useHotReload = false;
    String hotReloadDir = "itchat.pkl";
    int receivingRetryCount = 5;

    private long lastNormalRetcodeTime; // 最后一次收到正常retcode的时间，秒为单位

    /**
     * 请求参数
     */
    public Map<String, Object> getParamMap() {
        return new HashMap<String, Object>(1) {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            {
                Map<String, String> map = new HashMap<String, String>();
                for (BaseParaEnum baseRequest : BaseParaEnum.values()) {
                    map.put(baseRequest.para(), getLoginInfo().get(baseRequest.value()).toString());
                }
                put("BaseRequest", map);
            }
        };
    }


}
