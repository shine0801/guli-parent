package com.atguigu.eduorder.service.impl;

import com.atguigu.commonutils.ordervo.CourseWebOrderVo;
import com.atguigu.commonutils.ordervo.UCenterMemberOrder;
import com.atguigu.eduorder.client.EduClient;
import com.atguigu.eduorder.client.UcenterClient;
import com.atguigu.eduorder.entity.TOrder;
import com.atguigu.eduorder.mapper.TOrderMapper;
import com.atguigu.eduorder.service.TOrderService;
import com.atguigu.eduorder.utils.OrderNoUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author shine
 * @since 2021-07-20
 */
@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements TOrderService {

    @Autowired
    private EduClient eduClient;

    @Autowired
    private UcenterClient ucenterClient;

    // 1- 生成订单信息,创建订单返回订单号
    @Override
    public String createOrders(String courseId, String memberId) {
        //通过远程调用通过用户id获取用户信息
        UCenterMemberOrder ucenterMember = ucenterClient.getUserInfoOrder(memberId);
        //通过远程调用通过课程id获取课程信息
        CourseWebOrderVo courseDto = eduClient.getCourseInfoOrder(courseId);

        //创建对象，添加；
        TOrder order = new TOrder();
        order.setOrderNo(OrderNoUtil.getOrderNo());
        order.setCourseId(courseId);
        order.setCourseTitle(courseDto.getTitle());
        order.setCourseCover(courseDto.getCover());
        order.setTeacherName(courseDto.getTeacherName());
        order.setTotalFee(courseDto.getPrice());
        order.setMemberId(memberId);
        order.setMobile(ucenterMember.getMobile());
        order.setNickname(ucenterMember.getNickname());
        order.setStatus(0);  // 订单状态
        order.setPayType(1); //1-微信

        baseMapper.insert(order);
        //返回订单号
        return order.getOrderNo();
    }
}
