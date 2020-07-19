
package com.NanShop.mall.dao;

import com.NanShop.mall.entity.NanShopMallShoppingCartItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NanShopMallShoppingCartItemMapper {
    int deleteByPrimaryKey(Long cartItemId);

    int insert(NanShopMallShoppingCartItem record);

    int insertSelective(NanShopMallShoppingCartItem record);

    NanShopMallShoppingCartItem selectByPrimaryKey(Long cartItemId);

    NanShopMallShoppingCartItem selectByUserIdAndGoodsId(@Param("nanShopMallUserId") Long nanShopMallUserId, @Param("goodsId") Long goodsId);

    List<NanShopMallShoppingCartItem> selectByUserId(@Param("nanShopMallUserId") Long nanShopMallUserId, @Param("number") int number);

    int selectCountByUserId(Long nanShopMallUserId);

    int updateByPrimaryKeySelective(NanShopMallShoppingCartItem record);

    int updateByPrimaryKey(NanShopMallShoppingCartItem record);

    int deleteBatch(List<Long> ids);
}