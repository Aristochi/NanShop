
package com.NanShop.mall.service;

import com.NanShop.mall.controller.vo.NanShopMallShoppingCartItemVO;
import com.NanShop.mall.entity.NanShopMallShoppingCartItem;

import java.util.List;

public interface NanShopMallShoppingCartService {

    /**
     * 保存商品至购物车中
     *
     * @param nanShopMallShoppingCartItem
     * @return
     */
    String saveNanShopMallCartItem(NanShopMallShoppingCartItem nanShopMallShoppingCartItem);

    /**
     * 修改购物车中的属性
     *
     * @param nanShopMallShoppingCartItem
     * @return
     */
    String updateNanShopMallCartItem(NanShopMallShoppingCartItem nanShopMallShoppingCartItem);

    /**
     * 获取购物项详情
     *
     * @param nanShopMallShoppingCartItemId
     * @return
     */
    NanShopMallShoppingCartItem getNanShopMallCartItemById(Long nanShopMallShoppingCartItemId);

    /**
     * 删除购物车中的商品
     *
     * @param nanShopMallShoppingCartItemId
     * @return
     */
    Boolean deleteById(Long nanShopMallShoppingCartItemId);

    /**
     * 获取我的购物车中的列表数据
     *
     * @param nanShopMallUserId
     * @return
     */
    List<NanShopMallShoppingCartItemVO> getMyShoppingCartItems(Long nanShopMallUserId);
}
