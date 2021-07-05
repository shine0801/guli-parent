package com.atguigu.oss.controller;


import com.atguigu.commonutils.R;
import com.atguigu.oss.service.OssService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/eduoss/fileoss")
@CrossOrigin
public class OssController {
    @Autowired
    private OssService ossService;

    //上传头像的方法
    @ApiOperation(value="上传头像文件")
    @PostMapping("/uploadFile")
    public R uploadOssFile(MultipartFile file){
        //获取上传方法 MultipartFile
        String url = ossService.uploadFileAvatar(file);
        return R.ok().data("url",url);
    }
}
