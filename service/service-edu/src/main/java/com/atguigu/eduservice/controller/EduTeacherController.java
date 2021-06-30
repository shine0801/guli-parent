package com.atguigu.eduservice.controller;


import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author shine
 * @since 2021-06-29
 */
@Api("讲师管理")
@RestController
@RequestMapping("/eduservice/teacher")
public class EduTeacherController {

    //注入service
    @Autowired
    private EduTeacherService teacherService;

    //1 查询讲师表中所有数据
    //restful风格

    @ApiOperation(value="所有讲师列表")
    @GetMapping("findAll")
    public List<EduTeacher> findAllTeacher(){
        List<EduTeacher> list = teacherService.list(null);
        return list;
    }


    /*
    * 1-逻辑插件
    * 2-实体类逻辑注解
    * 3-控制器
    * 4-delete提交没有办法在浏览器测试
    * 5-可借助一些工具进行测试
 * */
    @ApiOperation("根据ID讲师删除讲师")
    @DeleteMapping("{id}")
    public boolean removeById(
            @ApiParam(name="id", value = "讲师ID", required = true)
            @PathVariable String id){
        return teacherService.removeById(id);
    }



}

