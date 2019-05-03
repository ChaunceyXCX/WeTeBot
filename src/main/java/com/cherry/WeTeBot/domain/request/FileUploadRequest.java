package com.cherry.WeTeBot.domain.request;

import com.cherry.WeTeBot.component.WeChat;
import com.cherry.WeTeBot.domain.request.component.BaseRequest;
import com.cherry.WeTeBot.utils.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName FileUploadRequest
 * @Description TODO
 * @Author https://github.com/ChaunceyCX
 * @Date 19-4-24 下午11:19
 * @Version 1.0
 **/
public class FileUploadRequest {
    String id;
    String name;
    String type;
    // '%m/%d/%Y, %H:%M:%S GMT+0800 (CST)'
    String lastModifiedDate;
    Long size;
    String mediatype;
    UploadMediaRequest uploadmediarequest;
    /*{"UploadType":2,
            "BaseRequest":{"Uin":1423809842,"Sid":"uMhnbxBFQl79GKfv","Skey":"@crypt_d4ab6753_965e3c020b78bc1b3a5ceb79c8916408","DeviceID":"e773502167699263"},
        "ClientMediaId":1556118062561,
        "TotalLen":106639,
        "StartPos":0,
        "DataLen":106639,
        "MediaType":4,
        "FromUserName":"@1e38822cc68075d502189d2157aa3536b394151323485a1fece51869b4409dd4",
        "ToUserName":"@c03be2d5590541f9312b3985bd673b18bcb83ef8f2776ff14296fa5d6d955ec8",
        "FileMd5":"b89ce7d9626ad06d47faca06c2fec6b0"}
        */
    String webwx_data_ticket;
    String pass_ticket;
    HttpEntity<ClassPathResource> filename;


    public FileUploadRequest(WeChat weChat, String filePath, HttpEntity<ClassPathResource> entity) {
        this.id = "WU_FILE_0";
        this.name = FileUtils.getFileName(filePath);
        this.type = FileUtils.getFileType(filePath);
        String dateFormat = "MM/dd/yyyy, HH:mm:ss GMT+0800 (CST)";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        Date date = new Date();
        this.lastModifiedDate = simpleDateFormat.format(date);
        this.size = FileUtils.getFileLength(filePath);
        this.mediatype = "pic";
        UploadMediaRequest uploadMediaRequest = new UploadMediaRequest(date,weChat.getBaseRequest(),size);
        this.uploadmediarequest = uploadMediaRequest;
        this.webwx_data_ticket = weChat.getCookies().getWebwx_data_ticket();
        this.pass_ticket = weChat.getPassTicket();
        this.filename = entity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getMediatype() {
        return mediatype;
    }

    public void setMediatype(String mediatype) {
        this.mediatype = mediatype;
    }

    public UploadMediaRequest getUploadmediarequest() {
        return uploadmediarequest;
    }

    public void setUploadmediarequest(UploadMediaRequest uploadmediarequest) {
        this.uploadmediarequest = uploadmediarequest;
    }

    public String getWebwx_data_ticket() {
        return webwx_data_ticket;
    }

    public void setWebwx_data_ticket(String webwx_data_ticket) {
        this.webwx_data_ticket = webwx_data_ticket;
    }

    public String getPass_ticket() {
        return pass_ticket;
    }

    public void setPass_ticket(String pass_ticket) {
        this.pass_ticket = pass_ticket;
    }

    public org.springframework.http.HttpEntity<ClassPathResource> getFilename() {
        return filename;
    }

    public void setFilename(org.springframework.http.HttpEntity<ClassPathResource> filename) {
        this.filename = filename;
    }

    private class UploadMediaRequest {
        Integer UploadType;
        BaseRequest BaseRequest;
        Long ClientMediaId;
        Long TotalLen;
        Integer StartPos;
        Long DataLen;
        Integer MediaType;
        String FromUserName;
        String ToUserName;
        String FileMd5;

        public UploadMediaRequest(Date date, BaseRequest baseRequest, Long fileLength) {
            // UploadType = uploadType;
            BaseRequest = baseRequest;
            ClientMediaId = date.getTime();
            TotalLen = fileLength;
            StartPos = 0;
            DataLen = fileLength;
            MediaType = 4;
            // FromUserName = fromUserName;
            // ToUserName = toUserName;
            // FileMd5 = fileMd5;
        }

        public Integer getUploadType() {
            return UploadType;
        }

        public void setUploadType(Integer uploadType) {
            UploadType = uploadType;
        }

        public com.cherry.WeTeBot.domain.request.component.BaseRequest getBaseRequest() {
            return BaseRequest;
        }

        public void setBaseRequest(com.cherry.WeTeBot.domain.request.component.BaseRequest baseRequest) {
            BaseRequest = baseRequest;
        }

        public Long getClientMediaId() {
            return ClientMediaId;
        }

        public void setClientMediaId(Long clientMediaId) {
            ClientMediaId = clientMediaId;
        }

        public Long getTotalLen() {
            return TotalLen;
        }

        public void setTotalLen(Long totalLen) {
            TotalLen = totalLen;
        }

        public Integer getStartPos() {
            return StartPos;
        }

        public void setStartPos(Integer startPos) {
            StartPos = startPos;
        }

        public Long getDataLen() {
            return DataLen;
        }

        public void setDataLen(Long dataLen) {
            DataLen = dataLen;
        }

        public Integer getMediaType() {
            return MediaType;
        }

        public void setMediaType(Integer mediaType) {
            MediaType = mediaType;
        }

        public String getFromUserName() {
            return FromUserName;
        }

        public void setFromUserName(String fromUserName) {
            FromUserName = fromUserName;
        }

        public String getToUserName() {
            return ToUserName;
        }

        public void setToUserName(String toUserName) {
            ToUserName = toUserName;
        }

        public String getFileMd5() {
            return FileMd5;
        }

        public void setFileMd5(String fileMd5) {
            FileMd5 = fileMd5;
        }
    };
}
