
package com.NanShop.mall.controller.admin;

import com.NanShop.mall.common.Constants;
import com.NanShop.mall.common.NanShopMallCategoryLevelEnum;
import com.NanShop.mall.common.ServiceResultEnum;
import com.NanShop.mall.entity.NanShopMallGoods;
import com.NanShop.mall.service.NanShopMallGoodsService;
import com.NanShop.mall.util.PageQueryUtil;
import com.NanShop.mall.util.Result;
import com.NanShop.mall.util.ResultGenerator;
import com.NanShop.mall.entity.GoodsCategory;
import com.NanShop.mall.service.NanShopMallCategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/admin")
public class NanShopMallGoodsController {

    @Resource
    private NanShopMallGoodsService nanShopMallGoodsService;
    @Resource
    private NanShopMallCategoryService nanShopMallCategoryService;

    @GetMapping("/goods")
    public String goodsPage(HttpServletRequest request) {
        request.setAttribute("path", "nanShop_mall_goods");
        return "admin/nanShop_mall_goods";
    }

    @GetMapping("/goods/edit")
    public String edit(HttpServletRequest request) {
        request.setAttribute("path", "edit");
        //查询所有的一级分类
        List<GoodsCategory> firstLevelCategories = nanShopMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), NanShopMallCategoryLevelEnum.LEVEL_ONE.getLevel());
        if (!CollectionUtils.isEmpty(firstLevelCategories)) {
            //查询一级分类列表中第一个实体的所有二级分类
            List<GoodsCategory> secondLevelCategories = nanShopMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(firstLevelCategories.get(0).getCategoryId()), NanShopMallCategoryLevelEnum.LEVEL_TWO.getLevel());
            if (!CollectionUtils.isEmpty(secondLevelCategories)) {
                //查询二级分类列表中第一个实体的所有三级分类
                List<GoodsCategory> thirdLevelCategories = nanShopMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondLevelCategories.get(0).getCategoryId()), NanShopMallCategoryLevelEnum.LEVEL_THREE.getLevel());
                request.setAttribute("firstLevelCategories", firstLevelCategories);
                request.setAttribute("secondLevelCategories", secondLevelCategories);
                request.setAttribute("thirdLevelCategories", thirdLevelCategories);
                request.setAttribute("path", "goods-edit");
                return "admin/nanShop_mall_goods_edit";
            }
        }
        return "error/error_5xx";
    }

    @GetMapping("/goods/edit/{goodsId}")
    public String edit(HttpServletRequest request, @PathVariable("goodsId") Long goodsId) {
        request.setAttribute("path", "edit");
        NanShopMallGoods nanShopMallGoods = nanShopMallGoodsService.getNanShopMallGoodsById(goodsId);
        if (nanShopMallGoods == null) {
            return "error/error_400";
        }
        if (nanShopMallGoods.getGoodsCategoryId() > 0) {
            if (nanShopMallGoods.getGoodsCategoryId() != null || nanShopMallGoods.getGoodsCategoryId() > 0) {
                //有分类字段则查询相关分类数据返回给前端以供分类的三级联动显示
                GoodsCategory currentGoodsCategory = nanShopMallCategoryService.getGoodsCategoryById(nanShopMallGoods.getGoodsCategoryId());
                //商品表中存储的分类id字段为三级分类的id，不为三级分类则是错误数据
                if (currentGoodsCategory != null && currentGoodsCategory.getCategoryLevel() == NanShopMallCategoryLevelEnum.LEVEL_THREE.getLevel()) {
                    //查询所有的一级分类
                    List<GoodsCategory> firstLevelCategories = nanShopMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), NanShopMallCategoryLevelEnum.LEVEL_ONE.getLevel());
                    //根据parentId查询当前parentId下所有的三级分类
                    List<GoodsCategory> thirdLevelCategories = nanShopMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(currentGoodsCategory.getParentId()), NanShopMallCategoryLevelEnum.LEVEL_THREE.getLevel());
                    //查询当前三级分类的父级二级分类
                    GoodsCategory secondCategory = nanShopMallCategoryService.getGoodsCategoryById(currentGoodsCategory.getParentId());
                    if (secondCategory != null) {
                        //根据parentId查询当前parentId下所有的二级分类
                        List<GoodsCategory> secondLevelCategories = nanShopMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondCategory.getParentId()), NanShopMallCategoryLevelEnum.LEVEL_TWO.getLevel());
                        //查询当前二级分类的父级一级分类
                        GoodsCategory firestCategory = nanShopMallCategoryService.getGoodsCategoryById(secondCategory.getParentId());
                        if (firestCategory != null) {
                            //所有分类数据都得到之后放到request对象中供前端读取
                            request.setAttribute("firstLevelCategories", firstLevelCategories);
                            request.setAttribute("secondLevelCategories", secondLevelCategories);
                            request.setAttribute("thirdLevelCategories", thirdLevelCategories);
                            request.setAttribute("firstLevelCategoryId", firestCategory.getCategoryId());
                            request.setAttribute("secondLevelCategoryId", secondCategory.getCategoryId());
                            request.setAttribute("thirdLevelCategoryId", currentGoodsCategory.getCategoryId());
                        }
                    }
                }
            }
        }
        if (nanShopMallGoods.getGoodsCategoryId() == 0) {
            //查询所有的一级分类
            List<GoodsCategory> firstLevelCategories = nanShopMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), NanShopMallCategoryLevelEnum.LEVEL_ONE.getLevel());
            if (!CollectionUtils.isEmpty(firstLevelCategories)) {
                //查询一级分类列表中第一个实体的所有二级分类
                List<GoodsCategory> secondLevelCategories = nanShopMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(firstLevelCategories.get(0).getCategoryId()), NanShopMallCategoryLevelEnum.LEVEL_TWO.getLevel());
                if (!CollectionUtils.isEmpty(secondLevelCategories)) {
                    //查询二级分类列表中第一个实体的所有三级分类
                    List<GoodsCategory> thirdLevelCategories = nanShopMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondLevelCategories.get(0).getCategoryId()), NanShopMallCategoryLevelEnum.LEVEL_THREE.getLevel());
                    request.setAttribute("firstLevelCategories", firstLevelCategories);
                    request.setAttribute("secondLevelCategories", secondLevelCategories);
                    request.setAttribute("thirdLevelCategories", thirdLevelCategories);
                }
            }
        }
        request.setAttribute("goods", nanShopMallGoods);
        request.setAttribute("path", "goods-edit");
        return "admin/nanShop_mall_goods_edit";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/goods/list", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(nanShopMallGoodsService.getNanShopMallGoodsPage(pageUtil));
    }

    /**
     * 添加
     */
    @RequestMapping(value = "/goods/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(@RequestBody NanShopMallGoods nanShopMallGoods) {
        if (StringUtils.isEmpty(nanShopMallGoods.getGoodsName())
                || StringUtils.isEmpty(nanShopMallGoods.getGoodsIntro())
                || StringUtils.isEmpty(nanShopMallGoods.getTag())
                || Objects.isNull(nanShopMallGoods.getOriginalPrice())
                || Objects.isNull(nanShopMallGoods.getGoodsCategoryId())
                || Objects.isNull(nanShopMallGoods.getSellingPrice())
                || Objects.isNull(nanShopMallGoods.getStockNum())
                || Objects.isNull(nanShopMallGoods.getGoodsSellStatus())
                || StringUtils.isEmpty(nanShopMallGoods.getGoodsCoverImg())
                || StringUtils.isEmpty(nanShopMallGoods.getGoodsDetailContent())) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = nanShopMallGoodsService.saveNanShopMallGoods(nanShopMallGoods);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }


    /**
     * 修改
     */
    @RequestMapping(value = "/goods/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(@RequestBody NanShopMallGoods nanShopMallGoods) {
        if (Objects.isNull(nanShopMallGoods.getGoodsId())
                || StringUtils.isEmpty(nanShopMallGoods.getGoodsName())
                || StringUtils.isEmpty(nanShopMallGoods.getGoodsIntro())
                || StringUtils.isEmpty(nanShopMallGoods.getTag())
                || Objects.isNull(nanShopMallGoods.getOriginalPrice())
                || Objects.isNull(nanShopMallGoods.getSellingPrice())
                || Objects.isNull(nanShopMallGoods.getGoodsCategoryId())
                || Objects.isNull(nanShopMallGoods.getStockNum())
                || Objects.isNull(nanShopMallGoods.getGoodsSellStatus())
                || StringUtils.isEmpty(nanShopMallGoods.getGoodsCoverImg())
                || StringUtils.isEmpty(nanShopMallGoods.getGoodsDetailContent())) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = nanShopMallGoodsService.updateNanShopMallGoods(nanShopMallGoods);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }
//    /**
//    *删除
//    */
//    @RequestMapping(value = "/goods/delete", method = RequestMethod.POST)
//    @ResponseBody
//    public Result delete(@RequestBody NanShopMallGoods NanShopMallGoods) {
//        if (Objects.isNull(NanShopMallGoods.getGoodsId())
//                || StringUtils.isEmpty(NanShopMallGoods.getGoodsName())
//                || StringUtils.isEmpty(NanShopMallGoods.getGoodsIntro())
//                || StringUtils.isEmpty(NanShopMallGoods.getTag())
//                || Objects.isNull(NanShopMallGoods.getOriginalPrice())
//                || Objects.isNull(NanShopMallGoods.getSellingPrice())
//                || Objects.isNull(NanShopMallGoods.getGoodsCategoryId())
//                || Objects.isNull(NanShopMallGoods.getStockNum())
//                || Objects.isNull(NanShopMallGoods.getGoodsSellStatus())
//                || StringUtils.isEmpty(NanShopMallGoods.getGoodsCoverImg())
//                || StringUtils.isEmpty(NanShopMallGoods.getGoodsDetailContent())) {
//            return ResultGenerator.genFailResult("参数异常！");
//        }
//        String result = nanShopMallGoodsService.deleteGoods(NanShopMallGoods);
//        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
//            return ResultGenerator.genSuccessResult();
//        } else {
//            return ResultGenerator.genFailResult(result);
//        }
//    }

    /**
     * 详情
     */
    @GetMapping("/goods/info/{id}")
    @ResponseBody
    public Result info(@PathVariable("id") Long id) {
        NanShopMallGoods goods = nanShopMallGoodsService.getNanShopMallGoodsById(id);
        if (goods == null) {
            return ResultGenerator.genFailResult(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return ResultGenerator.genSuccessResult(goods);
    }

    /**
     * 批量修改销售状态
     */
    @RequestMapping(value = "/goods/status/{sellStatus}", method = RequestMethod.PUT)
    @ResponseBody
    public Result delete(@RequestBody Long[] ids, @PathVariable("sellStatus") int sellStatus) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (sellStatus != Constants.SELL_STATUS_UP && sellStatus != Constants.SELL_STATUS_DOWN) {
            return ResultGenerator.genFailResult("状态异常！");
        }
        if (nanShopMallGoodsService.batchUpdateSellStatus(ids, sellStatus)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("修改失败");
        }
    }

}