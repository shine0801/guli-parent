package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.chapter.VideoVo;
import com.atguigu.eduservice.service.EduVideoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author shine
 * @since 2021-07-05
 */
@RestController
@RequestMapping("/eduservice/video")
@CrossOrigin
public class EduVideoController {

    @Autowired
    private EduVideoService eduVideoService;

    //添加小节
    @ApiOperation("添加小节")
    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo){
        eduVideoService.save(eduVideo);
        return R.ok();
    }
    //删除小节
    //TODO 需要完善的，删除小节的时候，同时把里面的视频删除
    @ApiOperation("删除小节")
    @DeleteMapping("{id}")
    public R deleteVideo(@PathVariable String id){
        eduVideoService.removeById(id);
        return R.ok();
    }

    //回显小节信息
    @ApiOperation("回显小节")
    @GetMapping("getVideoInfo/{videoId}")
    public R getVideoInfo(@PathVariable String videoId){
        EduVideo eduVideo = eduVideoService.getById(videoId);
        return R.ok().data("eduVideo",eduVideo);
    }
    //4-更新章节
    @ApiOperation("修改小节")
    @PostMapping("updateVideo")
    public R updateChapter(@RequestBody EduVideo eduVideo){
        eduVideoService.updateById(eduVideo);
        return R.ok();
    }


}

