package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduCourseDescription;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.frontvo.CourseFrontVo;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.atguigu.eduservice.mapper.EduCourseMapper;
import com.atguigu.eduservice.service.*;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author shine
 * @since 2021-07-05
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    //注入课程描述信息的接口
    @Autowired
    private EduCourseDescriptionService eduCourseDescriptionService;
    @Autowired
    private EduVideoService eduVideoService;
    @Autowired
    private EduChapterService eduChapterService;

    //新增基本信息
    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {

        //保存课程信息
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, eduCourse);
        int resultCourseInfo = baseMapper.insert(eduCourse);  //返回成功加入了几条记录
        if(resultCourseInfo == 0){
            throw new GuliException(20001, "课程信息保存失败");
        }

        //保存课程详情信息
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setId(eduCourse.getId()); //统一id
        eduCourseDescription.setDescription(courseInfoVo.getDescription());

        boolean resultDescription = eduCourseDescriptionService.save(eduCourseDescription);
        if(!resultDescription){
            throw new GuliException(20001, "课程详情信息保存失败");
        }
        return eduCourse.getId();
    }

    //根据id查询基本信息
    @Override
    public CourseInfoVo getCourseInfo(String courseId) {

        EduCourse eduCourse = baseMapper.selectById(courseId);
        EduCourseDescription eduCourseDescription = eduCourseDescriptionService.getById(courseId);

        CourseInfoVo courseInfoVo = new CourseInfoVo();

        BeanUtils.copyProperties(eduCourse, courseInfoVo);
        BeanUtils.copyProperties(eduCourseDescription, courseInfoVo);

        return courseInfoVo;
    }

    //更新基本信息
    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {

        //修改课程表
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, eduCourse);
        int update = baseMapper.updateById(eduCourse);
        if(update==0){
            throw new GuliException(20001, "修改课程失败");
        }

        //修改描述表
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        BeanUtils.copyProperties(courseInfoVo, eduCourseDescription);
        eduCourseDescriptionService.updateById(eduCourseDescription);

    }

    //获取发布信息
    @Override
    public CoursePublishVo publishCourseInfo(String id) {
        CoursePublishVo publishCourseInfo = baseMapper.getPublishCourseInfo(id);
        return publishCourseInfo;
    }

    //多条件组合查询课程带分页
    @Override
    public void pageQuery(Page<EduCourse> pageCourse, CourseQuery courseQuery) {
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper();

        //没有条件
        if(courseQuery==null){
            baseMapper.selectPage(pageCourse, queryWrapper);
        }

        //多条件组合
        //课程标题
        if(!StringUtils.isEmpty(courseQuery.getTitle())){
            queryWrapper.like("title", courseQuery.getTitle());
        }

        //讲师
        if(!StringUtils.isEmpty(courseQuery.getTeacherId())){
            queryWrapper.eq("teacher_id", courseQuery.getTeacherId());
        }

        //课程标题
        if(!StringUtils.isEmpty(courseQuery.getSubjectParentId())){
            queryWrapper.eq("subject_parent_Id", courseQuery.getSubjectParentId());
        }

        //课程标题
        if(!StringUtils.isEmpty(courseQuery.getSubjectId())){
            queryWrapper.eq("subject_id", courseQuery.getSubjectId());
        }

        //课程标题
        if(!StringUtils.isEmpty(courseQuery.getStatus())){
            queryWrapper.eq("status", courseQuery.getStatus());
        }

        queryWrapper.orderByDesc("gmt_create");
        baseMapper.selectPage(pageCourse, queryWrapper);
    }

    //通过id删除课程
    @Override
    public void deleteCourseById(String courseId) {

        //删除小节
        eduVideoService.removeVideoByCourseId(courseId);
        //删除章节
        eduChapterService.removeChapterByCourseId(courseId);
        //删除描述表
        eduCourseDescriptionService.removeById(courseId);
        //删除课程（这里是逻辑删除）
        int res = baseMapper.deleteById(courseId);
        if(res==0){
            throw new GuliException(20001,"删除失败");
        }

    }


    //条件查询前台显示的课程列表
    @Override
    public Map<String, Object> getCourseFrontList(Page<EduCourse> pageCourse, CourseFrontVo courseFrontVo) {

        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();

        //没有条件
        if(courseFrontVo==null){
            baseMapper.selectPage(pageCourse, wrapper);
        }

        //多条件组合
        //一级分类
        if(!StringUtils.isEmpty(courseFrontVo.getSubjectParentId())){
            System.out.println(courseFrontVo.getSubjectParentId());
            wrapper.eq("subject_parent_id", courseFrontVo.getSubjectParentId());
        }

        //二级分类
        if(!StringUtils.isEmpty(courseFrontVo.getSubjectId())){
            wrapper.eq("subject_id", courseFrontVo.getSubjectId());
        }

        //销量排序
        if(!StringUtils.isEmpty(courseFrontVo.getBuyCountSort())){
            wrapper.orderByDesc("buy_count");
        }

        //最新时间排序
        if(!StringUtils.isEmpty(courseFrontVo.getGmtCreateSort())){
            wrapper.orderByDesc("gmt_create");
        }

        //价格排序
        if(!StringUtils.isEmpty(courseFrontVo.getPriceSort())){
            wrapper.orderByDesc("price");
        }

        baseMapper.selectPage(pageCourse, wrapper);

        List<EduCourse> records = pageCourse.getRecords();
        long current = pageCourse.getCurrent();
        long pages = pageCourse.getPages();
        long size = pageCourse.getSize();
        long total = pageCourse.getTotal();
        boolean hasNext = pageCourse.hasNext();
        boolean hasPrevious = pageCourse.hasPrevious();

        //把分页的数据获取出来，放入map集合中
        Map<String, Object> map = new HashMap<>();
        map.put("items",records);
        map.put("current",current);
        map.put("pages",pages);
        map.put("size",size);
        map.put("total",total);
        map.put("hasNext",hasNext);
        map.put("hasPrevious",hasPrevious);

        return map;
    }

    //获取前台课程页面
    @Override
    public CourseWebVo getBaseCourseInfo(String courseId) {
        return baseMapper.getBaseCourseInfo(courseId);
    }

}
