package com.cherry.WeTeBot.utils;

import com.cherry.WeTeBot.component.Message;
import com.cherry.WeTeBot.component.WeChat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * @ClassName FileUtils
 * @Description TODO
 * @Author https://github.com/ChaunceyCX
 * @Date 19-4-20 下午5:19
 * @Version 1.0
 **/
public class FileUtils {

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
      * @Author https://github.com/ChaunceyCX
      * @Description //保存图片
      * @Date 下午6:42 19-4-22
      * @Param [message, myID, datas, imgRootPath]
      * @return java.lang.String
      **/

    public static String saveImg(Message message, String myID, byte[] datas, String imgRootPath) throws IOException {
        // byte[];
        String usrPath = message.getFromUserName().equalsIgnoreCase(myID) ? "syncImg":message.getFromUserName();
        String imgName = "/" + new Date().getTime() + ".jpg";
        if(isPathExis(usrPath)){
            FileOutputStream fos = new FileOutputStream(imgRootPath+usrPath+imgName);
            fos.write(datas);
            fos.close();
        }
        return imgRootPath+usrPath+imgName;
    }

    /**
      * @Author https://github.com/ChaunceyCX
      * @Description //上传图片
      * @Date 下午6:45 19-4-22
      * @Param []
      * @return java.lang.String
      **/

    public static String upLoadFile(WeChat weChat, String fileUrl, String fileUrl2, String WECHAT_URL_UPLOAD_MEDIA, String filePath, boolean isImg) {
        if(!isFileExists(filePath)){
            logger.error("file not exists: " + filePath);
            return null;
        }
        String url1 = "https://file." + weChat.getHostUrl() + WECHAT_URL_UPLOAD_MEDIA;
        String url2 = "https://file2." + weChat.getHostUrl() + WECHAT_URL_UPLOAD_MEDIA;
        File file = new File(filePath);
        long fileLength = file.length();
        String fileType = getFileType(filePath);

        return null;
    }


    /**
      * @Author https://github.com/ChaunceyCX
      * @Description 目录校验,不存在就创建一个
      * @Date 下午8:38 19-4-21
      * @Param
      * @return
      **/
    public static boolean isPathExis(String path){
        File file = new File("./images/"+path);
        if(!file.exists()){
            file.mkdir();
            return true;
        }
        return true;
    }

    /**
      * @Author https://github.com/ChaunceyCX
      * @Description //判断文件是否存在
      * @Date 下午6:57 19-4-22
      * @Param [filePath]
      * @return boolean
      **/

    public static boolean isFileExists(String filePath){
        File file = new File(filePath);
        return file.exists();
    }

    /**
      * @Author https://github.com/ChaunceyCX
      * @Description //获取文件后缀名
      * @Date 下午7:48 19-4-23
      * @Param [filePath]
      * @return java.lang.String
      **/

    public static String getFileType(String filePath) {
        String fileType = "application/octet-stream";
        if(!(filePath ==null || filePath.isEmpty())) {
            fileType = filePath.substring(filePath.indexOf("."));
        }else {
            fileType = null;
        }
        return fileType;
    }

    /**
      * @Author https://github.com/ChaunceyCX
      * @Description //获取文件名
      * @Date 下午9:33 19-4-23
      * @Param [filePath]
      * @return java.lang.String
      **/

    public static String getFileName(String filePath) {

        if(!(filePath ==null || filePath.isEmpty())) {
            return filePath.substring(filePath.lastIndexOf("//")+1);
        }else
            return null;
    }

    /**
      * @Author https://github.com/ChaunceyCX
      * @Description //获取文件大小
      * @Date 下午9:03 19-4-27
      * @Param [filePath]
      * @return java.lang.Long
      **/

    public static Long getFileLength(String filePath) {
        if(!(filePath ==null || filePath.isEmpty())) {
            File file = new File(filePath);
            file.length();
            return file.length();
        }else
            return (long)0;
    }

}
