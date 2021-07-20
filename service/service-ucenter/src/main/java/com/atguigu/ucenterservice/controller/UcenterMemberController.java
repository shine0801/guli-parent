package com.atguigu.ucenterservice.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.ordervo.UCenterMemberOrder;
import com.atguigu.ucenterservice.entity.UcenterMember;
import com.atguigu.ucenterservice.entity.vo.LoginVo;
import com.atguigu.ucenterservice.entity.vo.RegisterVo;
import com.atguigu.ucenterservice.service.UcenterMemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author shine
 * @since 2021-07-15
 */
@RestController
@CrossOrigin
@RequestMapping("/educenter/member")
public class UcenterMemberController {
    @Autowired
    private UcenterMemberService memberService;
    //登录
    @PostMapping("login")
    public R loginUser(@RequestBody UcenterMember member){
        //调用service 方法实现登录
        //返回token值，使用JWT使用
        String token = memberService.login(member);
        return R.ok().data("token",token);

    }

    //注册
    @PostMapping("register")
    public R register(@RequestBody RegisterVo registerVo){
        memberService.register(registerVo);
        return R.ok();
    }

    //根据token获取用户信息
    @GetMapping("getMemberInfo")
    public R getMemberInfo(HttpServletRequest request) {
        //调用jwt工具类的方法。根据request对象获取头信息，返回用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //查询数据库，根据用户id查询用户信息
        UcenterMember member = memberService.getById(memberId);
        return R.ok().data("userInfo", member);
    }

    //根据id获取用户信息(用户评论)
    @PostMapping("getInfoUc/{id}")
    public  UcenterMember getInfo(@PathVariable String id ){
        UcenterMember ucenterMember = memberService.getById(id);
        return ucenterMember;
    }

    //根据id获取用户信息(订单)，和上面的一样，本方法返回的对象是在公共服务中
    @PostMapping("getUserInfoOrder/{id}")
    public UCenterMemberOrder getUserInfoOrder(@PathVariable String id){
        UcenterMember member = memberService.getById(id);
        //值的转换
        UCenterMemberOrder uCenterMemberOrder = new UCenterMemberOrder();
        BeanUtils.copyProperties(member, uCenterMemberOrder);
        return uCenterMemberOrder;
    }


}

