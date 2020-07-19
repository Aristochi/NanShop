
package com.NanShop.mall.service;

import com.NanShop.mall.entity.NanShopMallGoods;
import com.NanShop.mall.util.PageQueryUtil;
import com.NanShop.mall.util.PageResult;

import java.util.List;

public interface NanShopMallGoodsService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getNanShopMallGoodsPage(PageQueryUtil pageUtil);

    /**
     * 添加商品
     *
     * @param goods
     * @return
     */
    String saveNanShopMallGoods(NanShopMallGoods goods);

    /**
     * 批量新增商品数据
     *
     * @param nanShopMallGoodsList
     * @return
     */
    void batchSaveNanShopMallGoods(List<NanShopMallGoods> nanShopMallGoodsList);

    /*
    * 删除商品
    * */
    void deleteGoods(NanShopMallGoods goods);

    /**
     * 修改商品信息
     *
     * @param goods
     * @return
     */
    String updateNanShopMallGoods(NanShopMallGoods goods);

    /**
     * 获取商品详情
     *
     * @param id
     * @return
     */
    NanShopMallGoods getNanShopMallGoodsById(Long id);

    /**
     * 批量修改销售状态(上架下架)
     *
     * @param ids
     * @return
     */
    Boolean batchUpdateSellStatus(Long[] ids,int sellStatus);

    /**
     * 商品搜索
     *
     * @param pageUtil
     * @return
     */
    PageResult searchNanShopMallGoods(PageQueryUtil pageUtil);
}
