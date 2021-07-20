package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduComment;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.HashMap;

/**
 * <p>
 * 评论 服务类
 * </p>
 *
 * @author shine
 * @since 2021-07-20
 */
public interface EduCommentService extends IService<EduComment> {

    //获取评论区列表
    HashMap<String, Object> getCommonList(Page<EduComment> pageParam, String courseId);
}
