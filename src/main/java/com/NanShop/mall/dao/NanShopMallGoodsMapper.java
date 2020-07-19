
package com.NanShop.mall.dao;

import com.NanShop.mall.entity.NanShopMallGoods;
import com.NanShop.mall.entity.StockNumDTO;
import com.NanShop.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NanShopMallGoodsMapper {
    int deleteByPrimaryKey(Long goodsId);

    int insert(NanShopMallGoods record);

    int insertSelective(NanShopMallGoods record);

    NanShopMallGoods selectByPrimaryKey(Long goodsId);

    int updateByPrimaryKeySelective(NanShopMallGoods record);

    int updateByPrimaryKeyWithBLOBs(NanShopMallGoods record);

    int updateByPrimaryKey(NanShopMallGoods record);

    List<NanShopMallGoods> findNanShopMallGoodsList(PageQueryUtil pageUtil);

    int getTotalNanShopMallGoods(PageQueryUtil pageUtil);

    List<NanShopMallGoods> selectByPrimaryKeys(List<Long> goodsIds);

    List<NanShopMallGoods> findNanShopMallGoodsListBySearch(PageQueryUtil pageUtil);

    int getTotalNanShopMallGoodsBySearch(PageQueryUtil pageUtil);

    int batchInsert(@Param("nanShopMallGoodsList") List<NanShopMallGoods> nanShopMallGoodsList);

    int updateStockNum(@Param("stockNumDTOS") List<StockNumDTO> stockNumDTOS);

    int batchUpdateSellStatus(@Param("orderIds")Long[] orderIds,@Param("sellStatus") int sellStatus);

}