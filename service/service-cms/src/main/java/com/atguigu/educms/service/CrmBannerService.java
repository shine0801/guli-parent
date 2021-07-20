package com.atguigu.educms.service;

import com.atguigu.educms.entity.CrmBanner;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author shine
 * @since 2021-07-13
 */
public interface CrmBannerService extends IService<CrmBanner> {

    //前台主页查询
    List<CrmBanner> selectAllBanner();

    //管理员分页查询
    void getAdminBanner(Page<CrmBanner> pageBanner);
}
