package com.NanShop.mall.controller.mall;

import com.NanShop.mall.common.Constants;
import com.NanShop.mall.common.IndexConfigTypeEnum;
import com.NanShop.mall.controller.vo.NanShopMallIndexCarouselVO;
import com.NanShop.mall.controller.vo.NanShopMallIndexCategoryVO;
import com.NanShop.mall.controller.vo.NanShopMallIndexConfigGoodsVO;
import com.NanShop.mall.service.NanShopMallCarouselService;
import com.NanShop.mall.service.NanShopMallCategoryService;
import com.NanShop.mall.service.NanShopMallIndexConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Resource
    private NanShopMallCarouselService nanShopMallCarouselService;
//    @Resource
//    private AdvertisementService advertisementService;
    @Resource
    private NanShopMallIndexConfigService nanShopMallIndexConfigService;

    @Resource
    private NanShopMallCategoryService nanShopMallCategoryService;

    @GetMapping({"/index", "/", "/index.html"})
    public String indexPage(HttpServletRequest request) {
        List<NanShopMallIndexCategoryVO> categories = nanShopMallCategoryService.getCategoriesForIndex();
        if (CollectionUtils.isEmpty(categories)) {
            return "error/error_5xx";
        }
        List<NanShopMallIndexCarouselVO> carousels = nanShopMallCarouselService.getCarouselsForIndex(Constants.INDEX_CAROUSEL_NUMBER);
        List<NanShopMallIndexConfigGoodsVO> hotGoodses = nanShopMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_HOT.getType(), Constants.INDEX_GOODS_HOT_NUMBER);
        List<NanShopMallIndexConfigGoodsVO> newGoodses = nanShopMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_NEW.getType(), Constants.INDEX_GOODS_NEW_NUMBER);
        List<NanShopMallIndexConfigGoodsVO> recommendGoodses = nanShopMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_RECOMMOND.getType(), Constants.INDEX_GOODS_RECOMMOND_NUMBER);
//        List<AdvertisementVO> advertisements = advertisementService.getAdvertisementsForIndex(Constants.INDEX_GOODS_ADVERTISEMENT_NUMBER);

        request.setAttribute("categories", categories);//分类数据
        request.setAttribute("carousels", carousels);//轮播图
        request.setAttribute("hotGoodses", hotGoodses);//热销商品
        request.setAttribute("newGoodses", newGoodses);//新品
        request.setAttribute("recommendGoodses", recommendGoodses);//推荐商品
//        request.setAttribute("advertisements",advertisements);//轮播下的广告
        return "mall/index";
    }
}
