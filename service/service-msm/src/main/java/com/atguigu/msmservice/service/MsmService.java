package com.atguigu.msmservice.service;

import java.util.Map;

public interface MsmService {
    //发送短信验证码
    boolean send(Map<String, Object> param, String phone);

    //发送邮件获取验证码
    boolean sendMail(String code, String mail);
}
