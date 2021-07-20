package com.atguigu.eduservice.entity.frontvo;

import lombok.Data;

//登录信息
@Data
public class LoginInfoVo {

    private String id; //用户id（memberId）
    private String nickname; // 昵称
    private String avatar; //头像

}
