package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.chapter.VideoVo;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author shine
 * @since 2021-07-05
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService eduVideoService;

    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {

        //根据课程id查询出课程中所有的章节
        QueryWrapper<EduChapter> wrapperChapter = new QueryWrapper<>();
        wrapperChapter.eq("course_id", courseId).orderByAsc("sort");

        List<EduChapter> eduChapterList = baseMapper.selectList(wrapperChapter);

        //根据课程id查询出课程中所有的小节
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();
        wrapperVideo.eq("course_id", courseId);
        List<EduVideo> eduVideoList = eduVideoService.list(wrapperVideo);

        //创建list,用于最终封装数据。
        List<ChapterVo> finalList = new ArrayList<>();
        //遍历查询章节list集合进行封装
        for (EduChapter eduChapter: eduChapterList) {
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter, chapterVo);

            finalList.add(chapterVo);

            List<VideoVo> videoVoList = new ArrayList<>(); //存放小节的列表
            //遍历查询小结list集合，进行封装
            for (EduVideo eduVideo : eduVideoList) {
                if(eduVideo.getChapterId().equals(eduChapter.getId())){
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo, videoVo);
                    videoVoList.add(videoVo);
                }
            }
            chapterVo.setChildren(videoVoList); //把小节放进章节中去
        }
        return finalList;
    }


    //删除章节
    @Override
    public boolean deleteChapter(String chapterId) {
        //根据chapterid查询小结表，能查出就不删除
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id", chapterId);
        int count = eduVideoService.count(wrapper);
        if(count > 0) {
            //查询出数据,不删除
            throw new GuliException(20001,"不能删除");
        }else{
            //删除章节
            int res = baseMapper.deleteById(chapterId);
            return res > 0;
        }

    }


    //通过id删除章节
    @Override
    public void removeChapterByCourseId(String courseId) {
        QueryWrapper<EduChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        baseMapper.delete(queryWrapper);
    }
}
