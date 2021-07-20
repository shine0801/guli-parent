package com.atguigu.eduorder.service;

import com.atguigu.eduorder.entity.TOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author shine
 * @since 2021-07-20
 */
public interface TOrderService extends IService<TOrder> {

    //生成订单信息,创建订单返回订单号
    String createOrders(String courseId, String memberId);
}
