package com.atguigu.eduorder.client;

import com.atguigu.commonutils.ordervo.CourseWebOrderVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient("service-edu")
public interface EduClient {
    /**
     *根据课程id查询课程确认信息（远程调用）
     */
    @GetMapping("/eduservice/course/getCourseInfoOrder/{id}")
    public CourseWebOrderVo getCourseInfoOrder(@PathVariable String id);
}
