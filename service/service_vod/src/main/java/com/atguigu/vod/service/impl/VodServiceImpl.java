package com.atguigu.vod.service.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.vod.service.VodService;
import com.atguigu.vod.untils.ConstantPropertiesUtil;
import com.atguigu.vod.untils.InitVodClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class VodServiceImpl implements VodService {

    //上传视频
    @Override
    public String uploadVideoAly(MultipartFile file) {
        try {
            //上传文件的输入流
            InputStream inputStream = file.getInputStream();
            //原始名称
            String originalFilename = file.getOriginalFilename();
            //上传之后显示的名称
            String title = originalFilename.substring(0, originalFilename.lastIndexOf("."));

            UploadStreamRequest request = new UploadStreamRequest(
                    ConstantPropertiesUtil.ACCESS_KEY_ID,
                    ConstantPropertiesUtil.ACCESS_KEY_SECRET,
                    title, originalFilename, inputStream);

            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);
            //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。
            // 其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
            String videoId = null;
            if (response.isSuccess()) {
               videoId = response.getVideoId();
            }else{
                videoId = response.getVideoId();
            }
            return videoId;
        } catch (IOException e) {
            e.printStackTrace();
            throw new GuliException(20001, "guli vod 服务上传失败");
        }

    }

    //删除单个视频
    @Override
    public void removeVideo(String id) {

        try{
            //初始化对象
            DefaultAcsClient client = InitVodClient.initVodClient(
                    ConstantPropertiesUtil.ACCESS_KEY_ID,
                    ConstantPropertiesUtil.ACCESS_KEY_SECRET);
            //创建删除视频request对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            //设置id
            request.setVideoIds(id);
            //调用初始化对象的方法实现
            client.getAcsResponse(request);
        }catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(20001, "视频删除失败");
        }

    }

    //批量删除视频
    @Override
    public void removeMutilAlyVideo(List videoIdList) {
        try{
            //初始化对象
            DefaultAcsClient client = InitVodClient.initVodClient(
                    ConstantPropertiesUtil.ACCESS_KEY_ID,
                    ConstantPropertiesUtil.ACCESS_KEY_SECRET);
            //创建删除视频request对象
            DeleteVideoRequest request = new DeleteVideoRequest();

            //VideoIdList中的值转换为1,2,3,4,5
            String s = StringUtils.join(videoIdList.toArray(), ",");

            //设置id
            request.setVideoIds(s);
            //调用初始化对象的方法实现
            client.getAcsResponse(request);
        }catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(20001, "视频删除失败");
        }

    }


}
