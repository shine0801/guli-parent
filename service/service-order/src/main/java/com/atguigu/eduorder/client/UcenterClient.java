package com.atguigu.eduorder.client;

import com.atguigu.commonutils.ordervo.UCenterMemberOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient("service-ucenter")
public interface UcenterClient {
    //根据id获取用户信息(订单)，和上面的一样，本方法返回的对象是在公共服务中
    @PostMapping("/educenter/member/getUserInfoOrder/{id}")
    public UCenterMemberOrder getUserInfoOrder(@PathVariable String id);
}
