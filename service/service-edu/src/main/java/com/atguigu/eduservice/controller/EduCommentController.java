package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.UcenterClient;
import com.atguigu.eduservice.entity.EduComment;
import com.atguigu.eduservice.entity.frontvo.LoginInfoVo;
import com.atguigu.eduservice.service.EduCommentService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author shine
 * @since 2021-07-20
 */
@RestController
@CrossOrigin
@RequestMapping("/eduservice/educomment")
public class EduCommentController {

    @Autowired
    private EduCommentService commentService;

    @Autowired
    private UcenterClient ucenterClient;

    //分页查询课程评论的方法
    @GetMapping("{page}/{limit}")
    public R index(@PathVariable long page,
                   @PathVariable long limit,
                   String courseId){

        Page<EduComment> pageParam = new Page<>(page, limit);

        HashMap<String, Object> map = commentService.getCommonList(pageParam, courseId);

        return R.ok().data(map);


    }

    //添加评论
    @PostMapping("auth/save")
    public R save(@RequestBody EduComment eduComment, HttpServletRequest request){
        String menberId = JwtUtils.getMemberIdByJwtToken(request);  //获取memberId
        if(StringUtils.isEmpty(menberId)){
            return R.error().code(28004).message("请登录");     //没有登录是不能评论的
        }
        eduComment.setMemberId(menberId);  //设置memberId
        LoginInfoVo loginInfoVo = ucenterClient.getLoginInfo(menberId);

        eduComment.setNickname(loginInfoVo.getNickname()); //设置昵称
        eduComment.setAvatar(loginInfoVo.getAvatar());     //设置头像
        commentService.save(eduComment);                   //保存
        return R.ok();
    }


}

