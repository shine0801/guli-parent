package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.mapper.EduTeacherMapper;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author shine
 * @since 2021-06-29
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {


    //多条件组合查询带分页实现
    @Override
    public void pageQuery(Page<EduTeacher> pageParam, TeacherQuery teacherQuery) {
        QueryWrapper<EduTeacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");
        if(teacherQuery == null){
            baseMapper.selectPage(pageParam, queryWrapper);
        }
        //多条件组合
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        //判断条件是否为空，如果不为空拼接条件
        if(!StringUtils.isEmpty(name)){
            queryWrapper.like("name", name);
        }
        if(!StringUtils.isEmpty(level)){
            queryWrapper.eq("level", level);  //数据库字段名称
        }
        if(!StringUtils.isEmpty(begin)){
            queryWrapper.ge("gmt_create", begin);
        }
        if(!StringUtils.isEmpty(end)){
            queryWrapper.le("gmt_create", end);
        }
        baseMapper.selectPage(pageParam, queryWrapper);
    }
}
