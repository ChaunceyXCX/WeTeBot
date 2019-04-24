package com.cherry.WeTeBot.component;

import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @ClassName FileUpload
 * @Description 文件上传实体
 * @Author https://github.com/ChaunceyCX
 * @Date 19-4-23 下午9:38
 * @Version 1.0
 **/

@Component
public class FileUpload {

    private String id;
    private String fileName;
    private String type;
    private String lastModifiedDate;
    private Long size;
    private String mediatype;
    private UploadMediaRequest uploadMediaRequest;
    //TODO https://github.com/liuwons/wxBot/blob/master/wxbot.py
    //TODO 1025
    private String webwx_data_ticket;
    private String pass_ticket;
    private File filename;

}
