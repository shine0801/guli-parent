package com.atguigu.ucenterservice.controller;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.ucenterservice.entity.UcenterMember;
import com.atguigu.ucenterservice.service.UcenterMemberService;
import com.atguigu.ucenterservice.utils.ConstantPropertiesUtil;
import com.atguigu.ucenterservice.utils.HttpClientUtils;
import com.google.gson.Gson;
import com.sun.deploy.net.URLEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;


@CrossOrigin
@Controller//注意这里没有配置 @RestController
@RequestMapping("/api/ucenter/wx")
public class WxApiController {
    @Autowired
    private UcenterMemberService memberService;
    //生成微信扫描的二维码
    @GetMapping("login")
    public String genQrConnect(HttpSession session) {
        // 微信开放平台授权baseUrl
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
        // 回调地址
        String redirectUrl = ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL; //获取业务服务器重定向地址
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8"); //url编码
        } catch (UnsupportedEncodingException e) {
            throw new GuliException(20001, e.getMessage());
        }
        // 防止csrf攻击（跨站请求伪造攻击）
        //String state = UUID.randomUUID().toString().replaceAll("-", "");//一般情况下会使用一个随机数
        String state = "imhelen";//为了让大家能够使用我搭建的外网的微信回调跳转服务器，这里填写你在ngrok的前置域名
        // 采用redis等进行缓存state 使用sessionId为key 30分钟后过期，可配置
        //键："wechar-open-state-" + httpServletRequest.getSession().getId()
        //值：satte
        //过期时间：30分钟

        //生成qrcodeUrl
        String qrcodeUrl = String.format(
                baseUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                redirectUrl,
                state);

        //重定向请求的微信地址
        return "redirect:" + qrcodeUrl;
    }

    //获取扫描人信息，添加数据
    @GetMapping("callback")
    public String callback(String code, String state){
        try {
            //1-获取code值，临时票据，类似于验证码

            //2-拿证code请求微信的固定地址，得到两个值，access_id和openid
            String baseAccessTokenUrl =
                    "https://api.weixin.qq.com/sns/oauth2/access_token" +
                            "?appid=%s" +
                            "&secret=%s" +
                            "&code=%s" +
                            "&grant_type=authorization_code";
            //拼接三个参数
            String accessTokenUrl = String.format(baseAccessTokenUrl,
                    ConstantPropertiesUtil.WX_OPEN_APP_ID,
                    ConstantPropertiesUtil.WX_OPEN_APP_SECRET,
                    code);

            //请求者拼接好的地址，得到返回的两个值 access_token 和 openid
            //使用Httpclient发送请求，得到返回结果
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);

            //从accessTokenInfo获取access_token和openid,先转换成Map，才可以取值
            Gson gson = new Gson();
            HashMap map = gson.fromJson(accessTokenInfo, HashMap.class);
            String access_token = (String) map.get("access_token");
            String openid = (String) map.get("openid");

            UcenterMember member = memberService.getByOpenId(openid);
            if(member==null){  //不存在就存入
                //3-拿着access_token和openid，再去请求微信提供固定的地址，获取到扫描人信息
                //访问微信的资源服务器，获取用户信息
                //访问微信的资源服务器，获取用户信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";

                //拼接两个参数
                String userInfoUrl = String.format(baseUserInfoUrl, access_token, openid);
                String resultUserInfo = HttpClientUtils.get(userInfoUrl);
                //解析json获取userInfo字符串扫描信息。
                HashMap mapUserInfo = gson.fromJson(resultUserInfo, HashMap.class);
                String nickname = (String)mapUserInfo.get("nickname"); //昵称
                String headimgurl = (String)mapUserInfo.get("headimgurl"); //头像

                //把扫码人信息加入数据库
                //首先先判断是否存在
                member = new UcenterMember();
                member.setOpenid(openid);
                member.setNickname(nickname);
                member.setAvatar(headimgurl);
                memberService.save(member);
            }
            //生成jwt
            String token = JwtUtils.getJwtToken(member.getId(),member.getNickname());
            //返回到主页
            return "redirect:http://localhost:3000?token=" + token;
        }catch (Exception e){
           throw new GuliException(20001, "登陆失败");
        }
    }

}
