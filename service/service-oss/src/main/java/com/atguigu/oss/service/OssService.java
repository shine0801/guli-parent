package com.atguigu.oss.service;

import org.springframework.web.multipart.MultipartFile;

public interface OssService {
    /**
     * 文件上传至阿里云Oss
     * @param file
     * @return
     */
    String uploadFileAvatar(MultipartFile file);
}
