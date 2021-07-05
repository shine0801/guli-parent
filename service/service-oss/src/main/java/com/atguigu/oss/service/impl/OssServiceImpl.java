package com.atguigu.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.atguigu.oss.service.OssService;
import com.atguigu.oss.utils.ConstantPropertiesUtil;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {
    @Override
    public String uploadFileAvatar(MultipartFile file) {
        //获取阿里云存储相关常量
        String endPoint = ConstantPropertiesUtil.END_POINT;  //地域节点
        String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID; //Id
        String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET; //密钥
        String bucketName = ConstantPropertiesUtil.BUCKET_NAME;
        //文件的路径
        String uploadUrl = null;

        try {
            //判断oss实例是否存在：如果不存在则创建，如果存在则获取
            OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);
            if (!ossClient.doesBucketExist(bucketName)) {
                //创建bucket
                ossClient.createBucket(bucketName);
                //设置oss实例的访问权限：公共读
                ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            }

            //获取上传文件流
            InputStream inputStream = file.getInputStream();

            //文件名：uuid.扩展名
            String filename = file.getOriginalFilename(); //
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            filename = uuid + filename;

            //文件按照日期分类  2019/11/12/01.jpg
            String dataPath = "avatar/"+DateTime.now().toString("yyyy/MM/dd");

            filename = dataPath + "/" + filename;


            //文件上传至阿里云
            ossClient.putObject(bucketName, filename, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();

            //获取url地址
            uploadUrl = "https://" + bucketName + "." + endPoint + "/" + filename;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return uploadUrl;
    }
}
