package com.atguigu.msmservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.msmservice.service.MsmService;
import com.atguigu.msmservice.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin
@RequestMapping("/edumsm/msm")
public class MsmController {

    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //发送短信的方法
    @GetMapping("send/{phone}")
    public R sendMsm(@PathVariable String phone){
        //先从redis中获取验证码，如果获取直接返回
        String code = redisTemplate.opsForValue().get(phone);

        if(!StringUtils.isEmpty(code)){
            return R.ok();
        }

        //如果取不到就直接发送
        //生成随机值，传递阿里云进行发送
        code = RandomUtil.getFourBitRandom();
        Map<String, Object> param = new HashMap<>();
        param.put("code", code);
        //调用service调用短信发送
        boolean isSend = msmService.send(param,phone);
        if(isSend) {
            //发送成功，就把发送成功的验证码放到redis中
            //设置有效时间
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
            return R.ok();
        }
        else
            return R.error().message("短信发送失败");
    }

    //邮箱验证的方法
    @GetMapping("/sendMail/{mail}")
    public R sendMail(@PathVariable String mail){

        System.out.println("进入控制器");

        //先从redis中获取验证码，如果获取直接返回
        String code = redisTemplate.opsForValue().get(mail);
        System.out.println(code);

        if(!StringUtils.isEmpty(code)){
            return R.ok();
        }
        //如果取不到就直接发送
        //生成随机值，传递阿里云进行发送
        code = RandomUtil.getFourBitRandom();
        //调用service调用短信发送
        boolean isSend = msmService.sendMail(code,mail);

        if(isSend) {
            //发送成功，就把发送成功的验证码放到redis中
            //设置有效时间
            redisTemplate.opsForValue().set(mail, code, 5, TimeUnit.MINUTES);

            return R.ok();
        }
        else {
            return R.error().message("邮件发送失败");
        }
    }
}
