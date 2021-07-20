package com.atguigu.ucenterservice.service.impl;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.ucenterservice.entity.UcenterMember;
import com.atguigu.ucenterservice.entity.vo.LoginVo;
import com.atguigu.ucenterservice.entity.vo.RegisterVo;
import com.atguigu.ucenterservice.mapper.UcenterMemberMapper;
import com.atguigu.ucenterservice.service.UcenterMemberService;
import com.atguigu.ucenterservice.utils.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author shine
 * @since 2021-07-15
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String ,String> redisTemplate;
    //登录
    @Override
    public String login(UcenterMember member) {
        //获取登录的邮箱和密码
        String mobile = member.getMobile();
        String password = member.getPassword();

        //其一为空就登录事务
        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
           throw new GuliException(20001,"登录失败");
        }

        //判断手机号是否正确
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        UcenterMember ucenterMember = baseMapper.selectOne(wrapper);
        if(ucenterMember==null){//
            throw new GuliException(20001,"该邮箱不存在");
        }

        //判断密码
        if(!MD5.encrypt(password).equals(ucenterMember.getPassword())){
            throw new GuliException(20001,"登陆失败");
        }
        //判断是禁用
        if(ucenterMember.getIsDisabled()){
            throw new GuliException(20001,"登陆失败");
        }

        //登录成功
        //生成token,使用Jwt工具类
        String jwtToken = JwtUtils.getJwtToken(ucenterMember.getId(), ucenterMember.getNickname());
        return jwtToken;
    }


    //注册
    @Override
    public void register(RegisterVo registerVo) {
        //获取注册的数据
        String code = registerVo.getCode(); //验证码
        String mobile = registerVo.getMobile(); //手机号
        String nickname = registerVo.getNickname(); //昵称
        String password = registerVo.getPassword(); //密码

        //非空判断
        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)
           ||StringUtils.isEmpty(code) || StringUtils.isEmpty(nickname) ) {
            throw new GuliException(20001,"注册失败");
        }

        //判断验证码
        //先获取redis中的验证码
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if(!code.equals(redisCode)){
            throw new GuliException(20001,"注册失败");
        }

        //邮箱不能重复
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        int count = baseMapper.selectCount(wrapper);
        if(count>0){
            throw new GuliException(20001,",该邮箱已经注册，可以直接登录！");
        }

        //添加到数据库中
        UcenterMember member = new UcenterMember();
        member.setMobile(mobile);
        member.setNickname(nickname);

        member.setPassword(MD5.encrypt(password));
        member.setAvatar("https://shine0801.oss-cn-beijing.aliyuncs.com/avatar/2021/07/05/c3798677e214411abb01b3aa8e426682file.png");

        baseMapper.insert(member);


    }


    //根据openid查询对象
    @Override
    public UcenterMember getByOpenId(String openid) {
       QueryWrapper<UcenterMember> wrapper = new QueryWrapper();
       wrapper.eq(" openid", openid);
       UcenterMember member = baseMapper.selectOne(wrapper);
       return member;
    }
}
