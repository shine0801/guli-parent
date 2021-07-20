package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author shine
 * @since 2021-06-29
 */
public interface EduTeacherService extends IService<EduTeacher> {

    //多条件组合查询带分页
    void pageQuery(Page<EduTeacher> pageParam, TeacherQuery teacherQuery);


    //讲师分页查询
    Map<String, Object> getTeacherFrontList(Page<EduTeacher> pageTeacher);
}
