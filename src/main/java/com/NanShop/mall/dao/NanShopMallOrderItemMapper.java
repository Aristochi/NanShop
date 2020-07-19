
package com.NanShop.mall.dao;

import com.NanShop.mall.entity.NanShopMallOrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NanShopMallOrderItemMapper {
    int deleteByPrimaryKey(Long orderItemId);

    int insert(NanShopMallOrderItem record);

    int insertSelective(NanShopMallOrderItem record);

    NanShopMallOrderItem selectByPrimaryKey(Long orderItemId);

    /**
     * 根据订单id获取订单项列表
     *
     * @param orderId
     * @return
     */
    List<NanShopMallOrderItem> selectByOrderId(Long orderId);

    /**
     * 根据订单ids获取订单项列表
     *
     * @param orderIds
     * @return
     */
    List<NanShopMallOrderItem> selectByOrderIds(@Param("orderIds") List<Long> orderIds);

    /**
     * 批量insert订单项数据
     *
     * @param orderItems
     * @return
     */
    int insertBatch(@Param("orderItems") List<NanShopMallOrderItem> orderItems);

    int updateByPrimaryKeySelective(NanShopMallOrderItem record);

    int updateByPrimaryKey(NanShopMallOrderItem record);
}