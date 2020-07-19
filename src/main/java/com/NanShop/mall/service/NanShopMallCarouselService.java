
package com.NanShop.mall.service;

import com.NanShop.mall.controller.vo.NanShopMallIndexCarouselVO;
import com.NanShop.mall.entity.Carousel;
import com.NanShop.mall.util.PageQueryUtil;
import com.NanShop.mall.util.PageResult;

import java.util.List;

public interface NanShopMallCarouselService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getCarouselPage(PageQueryUtil pageUtil);

    String saveCarousel(Carousel carousel);

    String updateCarousel(Carousel carousel);

    Carousel getCarouselById(Integer id);

    Boolean deleteBatch(Integer[] ids);

    /**
     * 返回固定数量的轮播图对象(首页调用)
     *
     * @param number
     * @return
     */
    List<NanShopMallIndexCarouselVO> getCarouselsForIndex(int number);
}
