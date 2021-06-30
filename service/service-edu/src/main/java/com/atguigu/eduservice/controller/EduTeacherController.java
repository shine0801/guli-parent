package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduTeacherService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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
    @ApiOperation(value="所有讲师列表")
    @GetMapping("findAll")
    public R findAllTeacher(){
        List<EduTeacher> list = teacherService.list(null);
        return R.ok().data("items", list);
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
    public R removeById(
            @ApiParam(name="id", value = "讲师ID", required = true)
            @PathVariable String id){
        boolean b = teacherService.removeById(id);
        if(b)
            return R.ok();
        else
            return R.error();
    }

    //3-分页查询讲师方法
    @ApiOperation(value = "分页查询讲师列表")
    @GetMapping("pageTeacher/{current}/{limit}")
    public R pageListTeacher(
            @ApiParam(name = "current", value = "当前页码",  required = true)
            @PathVariable long current,
            @ApiParam(name = "limit", value = "每页的记录数", required = true)
            @PathVariable long limit){

        //创建page对象
        Page<EduTeacher> pageTeacher = new Page<>(current, limit);

        //调用方法时候，底层封装，把分页数据封装到pageTeacher对象里面
        teacherService.page(pageTeacher, null);

        long total = pageTeacher.getTotal(); //总记录数

        List<EduTeacher> records = pageTeacher.getRecords(); //数据list集合
        return R.ok().data("total", total).data("rows", records);
    }

    //多条件组合查询带分页
    @ApiOperation(value = "多条件组合查询带分页")
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(
            @ApiParam(name = "current", value = "当前页码", required = true)
            @PathVariable long current,
            @ApiParam(name = "limit", value = "每页的记录数", required = true)
            @PathVariable long limit,
            @ApiParam(name="teacherQuery", value = "查询对象" , required = false)
            @RequestBody(required = false) TeacherQuery teacherQuery){
        // RequestBody 需要post提交方式，使用json传递数据

        //创建page对象
        Page<EduTeacher> pageTeacher = new Page<>(current,limit);

        teacherService.pageQuery(pageTeacher, teacherQuery);

        long total = pageTeacher.getTotal(); //总记录数
        List<EduTeacher> records = pageTeacher.getRecords(); //数据的list集合

        return R.ok().data("total", total).data("rows", records);

    }

    //新增讲师
    @ApiOperation(value="新增讲师")
    @PostMapping("addTeacher")
    public R addTeacher(
            @ApiParam(name = "eduTeacher", value = "教师信息", required = true)
            @RequestBody EduTeacher eduTeacher){
        boolean f = teacherService.save(eduTeacher);
        return f ? R.ok():R.error();
    }

    //根据id查询
    @ApiOperation(value="根据id查询")
    @GetMapping("selectTeacherById/{id}")
    public R selectTeacherById(
            @ApiParam(name = "id", value = "教师id", required = true)
            @PathVariable String id){

        try {
            int x = 10 / 0;
        }catch (Exception e){
            throw new GuliException(20001, "出现自定义异常");
        }

        EduTeacher teacher = teacherService.getById(id);
        return R.ok().data("teacher", teacher);
    }
    
    //根据id进行修改
    @ApiOperation(value="根据id修改讲师")
    @PostMapping("updateTeacherById/{id}")
    public R updateTeacherById(
            @ApiParam(name = "id", value = "教师id", required = true)
            @PathVariable String id,
            @ApiParam(name = "eduTeacher", value = "教师对象", required = true)
            @RequestBody EduTeacher eduTeacher){

        eduTeacher.setId(id);
        boolean f= teacherService.updateById(eduTeacher);
        return f ? R.ok():R.error();
    }
}

