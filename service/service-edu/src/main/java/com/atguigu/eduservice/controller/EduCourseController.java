package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author shine
 * @since 2021-07-05
 */
@RestController
@RequestMapping("/eduservice/course")
@CrossOrigin
public class EduCourseController {

    @Autowired
    private EduCourseService eduCourseService;


    @ApiOperation(value = "多条件组合查询课程带分页")
    @PostMapping("pageCourseCondition/{current}/{limit}")
    public R pageTeacherCondition(
            @ApiParam(name = "current", value = "当前页码", required = true)
            @PathVariable long current,
            @ApiParam(name = "limit", value = "每页的记录数", required = true)
            @PathVariable long limit,
            @ApiParam(name="courseQuery", value = "查询对象" , required = false)
            @RequestBody(required = false) CourseQuery courseQuery){

        //1-创建page对象
        Page<EduCourse> pageCourse = new Page<>(current, limit);
        eduCourseService.pageQuery(pageCourse, courseQuery);

        long total = pageCourse.getTotal();
        List<EduCourse> records = pageCourse.getRecords();

        return R.ok().data("total", total).data("rows", records);
    }



    /**
     * 添加课程的基本信息
     * */
    @ApiOperation("添加课程的基本信息")
    @PostMapping("addCourseInfo")
    public R addCourseInfo(
            @ApiParam(name = "CourseInfoVo", value = "课程基本信息", required = true)
            @RequestBody CourseInfoVo courseInfoVo){

        String courseId = eduCourseService.saveCourseInfo(courseInfoVo);
        if(!StringUtils.isEmpty(courseId)){
            return R.ok().data("courseId", courseId); //返回添加后的课程id
        }else{
            return R.error().message("保存失败");
        }
    }

    /**
     * 根据课程id查询出课程的基本信息的接口
     * */
    @ApiOperation("根据课程id查询出课程的基本信息的接口")
    @GetMapping("getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable String courseId){
        CourseInfoVo courseInfo = eduCourseService.getCourseInfo(courseId);
        return R.ok().data("courseInfo",courseInfo);

    }

    /**
     * 修改课程的基本信息
     * */
    @ApiOperation("修改课程的基本信息")
    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        eduCourseService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }

    /**
     *根据课程id查询课程确认信息
     */
    @GetMapping("getPublishCourseInfo/{id}")
    public R getPublishCourseInfo(@PathVariable String id){
        CoursePublishVo coursePublishVo = eduCourseService.publishCourseInfo(id);
        return R.ok().data("publishCourse", coursePublishVo);
    }

    /**
     * 根据id发布课程
     * */
    @ApiOperation(value="根据id发布课程")
    @PostMapping("publishCourse/{id}")
    public R publishCourseById(@PathVariable String id){
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus(EduCourse.COURSE_NORMAL);
        eduCourseService.updateById(eduCourse);
        return R.ok();
    }

    /**
     * 通过id删除课程
     * */
    @ApiOperation(value="通过id删除课程")
    @DeleteMapping("{courseId}")
    public R deleteCourseById(@PathVariable String courseId){
        eduCourseService.deleteCourseById(courseId);
        return R.ok();
    }






}

