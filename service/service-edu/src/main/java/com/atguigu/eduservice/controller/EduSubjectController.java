package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.service.EduSubjectService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author shine
 * @since 2021-07-05
 */
@RestController
@RequestMapping("/eduservice/subject")
@CrossOrigin
public class EduSubjectController {

    @Autowired
    private EduSubjectService subjectService;


    /**
    *  添加课程分类：获取上传过来 的文件，把文件内容读取出来
    * */
    @ApiOperation(value = "添加课程分类")
    @PostMapping("addSubject")
    public R addSubject(MultipartFile file){
        //上传过来的文件
        subjectService.saveSubject(file, subjectService);
        return R.ok();
    }

    /**
     * 课程分类的列表功能
     **/
    @ApiOperation(value = "查询课程分类树")
    @GetMapping("getAllSubject")
    public R getAllSubject(){

        //集合的泛型是一级分类
        List<OneSubject> list = subjectService.getAllOneTwoSubject();
        return R.ok().data("list", list);
    }

}
