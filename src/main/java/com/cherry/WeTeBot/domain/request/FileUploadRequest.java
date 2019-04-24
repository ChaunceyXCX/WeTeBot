package com.cherry.WeTeBot.domain.request;

import com.cherry.WeTeBot.domain.request.component.BaseRequest;
import org.apache.http.HttpEntity;

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
    HttpEntity filename;

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
    };
}
