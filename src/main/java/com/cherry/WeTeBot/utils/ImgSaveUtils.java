package com.cherry.WeTeBot.utils;

import com.cherry.WeTeBot.domain.shared.Message;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * @ClassName ImgSaveUtils
 * @Description TODO
 * @Author https://github.com/ChaunceyCX
 * @Date 19-4-20 下午5:19
 * @Version 1.0
 **/
public class ImgSaveUtils {


    public static String saveImg(Message message, String myID, byte[] datas, String imgRootPath) throws IOException {
//        byte[]
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

}
