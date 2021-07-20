package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.frontvo.CourseFrontVo;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author shine
 * @since 2021-07-05
 */
public interface EduCourseService extends IService<EduCourse> {

    // 添加课程的基本信息
    String saveCourseInfo(CourseInfoVo courseInfoVo);

    //根据id
    CourseInfoVo getCourseInfo(String courseId);

    //更新基本信息
    void updateCourseInfo(CourseInfoVo courseInfoVo);

    //发布信息
    CoursePublishVo publishCourseInfo(String id);

    void pageQuery(Page<EduCourse> pageCourse, CourseQuery courseQuery);

    void deleteCourseById(String courseId);

    Map<String, Object> getCourseFrontList(Page<EduCourse> pageCourse, CourseFrontVo courseFrontVo);

    //前台课程页面获取
    CourseWebVo getBaseCourseInfo(String courseId);

}
