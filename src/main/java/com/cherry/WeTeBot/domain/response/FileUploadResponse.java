package com.cherry.WeTeBot.domain.response;

import com.cherry.WeTeBot.domain.response.component.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @ClassName FileUploadResponse
 * @Description TODO
 * @Author https://github.com/ChaunceyCX
 * @Date 19-4-24 下午11:19
 * @Version 1.0
 **/
public class FileUploadResponse {

    private BaseResponse BaseResponse;
    private Integer cDNThumbImgHeight;
    private Integer cDNThumbImgWidth;
    private String encryFileName;
    private String mediaId;
    private Integer startPos;

    public com.cherry.WeTeBot.domain.response.component.BaseResponse getBaseResponse() {
        return BaseResponse;
    }

    @JsonProperty
    public void setBaseResponse(com.cherry.WeTeBot.domain.response.component.BaseResponse baseResponse) {
        BaseResponse = baseResponse;
    }

    public Integer getcDNThumbImgHeight() {
        return cDNThumbImgHeight;
    }

    @JsonProperty
    public void setcDNThumbImgHeight(Integer cDNThumbImgHeight) {
        this.cDNThumbImgHeight = cDNThumbImgHeight;
    }

    public Integer getcDNThumbImgWidth() {
        return cDNThumbImgWidth;
    }

    @JsonProperty
    public void setcDNThumbImgWidth(Integer cDNThumbImgWidth) {
        this.cDNThumbImgWidth = cDNThumbImgWidth;
    }

    public String getEncryFileName() {
        return encryFileName;
    }

    @JsonProperty
    public void setEncryFileName(String encryFileName) {
        this.encryFileName = encryFileName;
    }

    public String getMediaId() {
        return mediaId;
    }

    @JsonProperty
    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public Integer getStartPos() {
        return startPos;
    }

    @JsonProperty
    public void setStartPos(Integer startPos) {
        this.startPos = startPos;
    }
}

