package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.entity.subject.TwoSubject;
import com.atguigu.eduservice.listener.SubjectExcelListener;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author shine
 * @since 2021-07-05
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    //添加课程分类
    @Override
    public void saveSubject(MultipartFile file, EduSubjectService subjectService) {

        try {
            InputStream inputStream = file.getInputStream();
            EasyExcel.read(inputStream, SubjectData.class, new SubjectExcelListener(subjectService)).sheet().doRead();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //课程分类列表（树形）
    @Override
    public List<OneSubject> getAllOneTwoSubject() {
        //查询出所有的一级分类
        QueryWrapper<EduSubject> wrapperOne = new QueryWrapper<>();
        wrapperOne.eq("parent_id", "0");
        List<EduSubject> oneSubjectsList = baseMapper.selectList(wrapperOne);
        //查询出所有的二级分类
        QueryWrapper<EduSubject> wrapperTwo = new QueryWrapper<>();
        wrapperOne.ne("parent_id", "0");
        List<EduSubject> twoSubjectsList = baseMapper.selectList(wrapperTwo);
        //封装一级分类
        List<OneSubject> finalSubjectList = new ArrayList<>();
        for (EduSubject eduSubject : oneSubjectsList) {
            OneSubject oneSubject = new OneSubject();

            //使用工具类：属性名要相同
            BeanUtils.copyProperties(eduSubject, oneSubject);
            finalSubjectList.add(oneSubject);

            //封装二级分类
            //在一级分类循环遍历查询所有二级分类
            List<TwoSubject> twoFinalSubjectList = new ArrayList<>();
            for (EduSubject tSubject : twoSubjectsList) {
                //判断二级分类parent_id 和 一级分类的id是否相等，相等就添加
                if(tSubject.getParentId().equals(oneSubject.getId())){
                    TwoSubject twoSubject = new TwoSubject();
                    BeanUtils.copyProperties(tSubject, twoSubject);
                    twoFinalSubjectList.add(twoSubject);
                }
            }
            //把一级下面的所有二级分类放到一级分类里面
            oneSubject.setChildren(twoFinalSubjectList);
        }
        return finalSubjectList;
    }
}
