package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eduservice/user")
@CrossOrigin //解决跨域问题
public class EduLoginController {

    //login
    @PostMapping("login")
    public R login(){
        return R.ok().data("token","admin");
    }

    //info()
    @GetMapping("info")
    public R info(){
        return R.ok().data("roles","[admin]").data("name","admin").data("avatar","https://shine0801.oss-cn-beijing.aliyuncs.com/avatar/2021/07/05/81d4220cabda4063b6d02bedcf0fbc28file.png");
    }
}
