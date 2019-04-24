package com.cherry.WeTeBot.component;

import com.cherry.WeTeBot.domain.request.component.BaseRequest;

/**
 * @ClassName UploadMediaRequest
 * @Description TODO
 * @Author https://github.com/ChaunceyCX
 * @Date 19-4-23 下午9:44
 * @Version 1.0
 **/
public class UploadMediaRequest {
    private Integer uploadType;
    private BaseRequest baseRequest;
    private String clientMediaId;
    private Long totalLen;
    private Integer startPos;
    private Long dataLen;
    private Integer mediaType;
    private String fromUserName;
    private String toUserName;
    private String fileMd5;
}
