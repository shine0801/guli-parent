package com.atguigu.eduorder.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.eduorder.client.EduClient;
import com.atguigu.eduorder.client.UcenterClient;
import com.atguigu.eduorder.entity.TOrder;
import com.atguigu.eduorder.service.TOrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.jsonwebtoken.Jwt;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author shine
 * @since 2021-07-20
 */
@RestController
@CrossOrigin
@RequestMapping("/eduorder/torder")
public class TOrderController {

    @Autowired
    private TOrderService orderService;

    @ApiOperation("1-生成订单信息,创建订单返回订单号")
    @PostMapping("createOrder/{courseId}")
    public R saveOrder(@PathVariable String courseId, HttpServletRequest request){

        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        String orderNo = orderService.createOrders(courseId, memberId);

        return  R.ok().data("orderId", orderNo);
    }

    @ApiOperation("2-根据订单id查询订单信息")
    @GetMapping("getOrderInfo/{orderId}")
    public R getOrderInfo(@PathVariable String orderId){
    QueryWrapper<TOrder> wrapper = new QueryWrapper<TOrder>();
    wrapper.eq("order_no", orderId);

    TOrder order = orderService.getOne(wrapper);

    return  R.ok().data("order", order);
    }

}

