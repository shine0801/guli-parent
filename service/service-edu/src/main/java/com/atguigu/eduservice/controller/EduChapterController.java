package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.service.EduChapterService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/eduservice/chapter")
@CrossOrigin
public class EduChapterController {

    @Autowired
    private EduChapterService chapterService;

    //1-课程大纲列表
    @ApiOperation("传入课程id，获取大纲列表")
    @GetMapping("getChapterVideo/{courseId}")
    public R getChapterVideo(@PathVariable String courseId){
        List<ChapterVo> list = chapterService.getChapterVideoByCourseId(courseId);
        return R.ok().data("list",list);
    }


    //2-新增章节
    @ApiOperation("新增章节")
    @PostMapping("addChapter")
    public R addChapter(@RequestBody EduChapter eduChapter){
        chapterService.save(eduChapter);
        return R.ok();
    }
    //3-根据id查询章节
    @ApiOperation("根据id查询章节")
    @GetMapping("getChapterInfo/{chapterId}")
    public R getChapterInfo(@PathVariable String chapterId){
        EduChapter eduChapter = chapterService.getById(chapterId);
        return R.ok().data("chapter", eduChapter);
    }

    //4-更新章节
    @ApiOperation("修改章节")
    @PostMapping("updateChapter")
    public R updateChapter(@RequestBody EduChapter eduChapter){
        chapterService.updateById(eduChapter);
        return R.ok();
    }

    //5-删除章节
    @ApiOperation("删除章节")
    @DeleteMapping("{chapterId}")
    public R deleteChapter(@PathVariable String chapterId){
        boolean flag = chapterService.deleteChapter(chapterId);
        if(flag)
            return R.ok();
        else
            return R.error();
    }



}

