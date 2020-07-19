
package com.NanShop.mall.dao;

import com.NanShop.mall.entity.NanShopMallOrder;
import com.NanShop.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NanShopMallOrderMapper {
    int deleteByPrimaryKey(Long orderId);

    int insert(NanShopMallOrder record);

    int insertSelective(NanShopMallOrder record);

    NanShopMallOrder selectByPrimaryKey(Long orderId);

    NanShopMallOrder selectByOrderNo(String orderNo);

    int updateByPrimaryKeySelective(NanShopMallOrder record);

    int updateByPrimaryKey(NanShopMallOrder record);

    List<NanShopMallOrder> findNanShopMallOrderList(PageQueryUtil pageUtil);

    int getTotalNanShopMallOrders(PageQueryUtil pageUtil);

    List<NanShopMallOrder> selectByPrimaryKeys(@Param("orderIds") List<Long> orderIds);

    int checkOut(@Param("orderIds") List<Long> orderIds);

    int closeOrder(@Param("orderIds") List<Long> orderIds, @Param("orderStatus") int orderStatus);

    int checkDone(@Param("orderIds") List<Long> asList);
}