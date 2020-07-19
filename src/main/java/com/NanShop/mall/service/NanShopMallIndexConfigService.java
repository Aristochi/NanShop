
package com.NanShop.mall.service;

import com.NanShop.mall.controller.vo.NanShopMallIndexConfigGoodsVO;
import com.NanShop.mall.entity.IndexConfig;
import com.NanShop.mall.util.PageQueryUtil;
import com.NanShop.mall.util.PageResult;

import java.util.List;

public interface NanShopMallIndexConfigService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getConfigsPage(PageQueryUtil pageUtil);

    String saveIndexConfig(IndexConfig indexConfig);

    String updateIndexConfig(IndexConfig indexConfig);

    IndexConfig getIndexConfigById(Long id);

    /**
     * 返回固定数量的首页配置商品对象(首页调用)
     *
     * @param number
     * @return
     */
    List<NanShopMallIndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number);

    Boolean deleteBatch(Long[] ids);
}
