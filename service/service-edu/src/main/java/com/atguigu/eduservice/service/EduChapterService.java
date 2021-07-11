package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author shine
 * @since 2021-07-05
 */
public interface EduChapterService extends IService<EduChapter> {

    //获取课程大纲
    List<ChapterVo> getChapterVideoByCourseId(String courseId);

    //删除章节
    boolean deleteChapter(String chapterId);

    void removeChapterByCourseId(String courseId);
}
