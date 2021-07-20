package com.atguigu.eduservice.client;

import com.atguigu.commonutils.R;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class VodClientImpl implements  VodClient{
    //出错之后执行
    @Override
    public R removeAlyVideo(String id) {
        return R.error().message("time out");
    }

    @Override
    public R deleteBatch(List<String> videoIdList) {
        return R.error().message("time out");
    }
}
