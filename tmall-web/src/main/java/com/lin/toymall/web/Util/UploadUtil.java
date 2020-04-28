package com.lin.toymall.web.Util;


import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/***
 * 上传图片/音频文件工具类
 */

public  class UploadUtil {
    public static String uploadImage(MultipartFile multipartFile) {
        String imgOrVedioUrl=FinalValue.TRACKERURL;
        // 获得配置文件的路径
        String trackerConfig=null;
        try{
            trackerConfig=UploadUtil.class.getResource("/tracker.conf").getPath();

        }catch (Exception e){
            e.printStackTrace();
        }

        // 配置fdfs的全局链接地址
        try {
            ClientGlobal.init(trackerConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //获得TrackerServer
        TrackerClient trackerClient = new TrackerClient();

        // 获得一个trackerServer的实例
        TrackerServer trackerServer = null;
        try {
            trackerServer = trackerClient.getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 通过tracker获得一个Storage链接客户端
        StorageClient storageClient = new StorageClient(trackerServer,null);
        try {
            //获得二进制对象
            byte[] bytes=multipartFile.getBytes();
            //获得文件名
            String originalFilename=multipartFile.getOriginalFilename();
            //获取扩展名
            int index=originalFilename.lastIndexOf(".");
            String extName=originalFilename.substring(index+1);
            //加载文件返回信息（文件存储地址）
            String[] uploadInfos = storageClient.upload_file(bytes, extName, null);
            for (String uploadInfo : uploadInfos) {
                imgOrVedioUrl += "/"+uploadInfo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  imgOrVedioUrl;
    }


}
