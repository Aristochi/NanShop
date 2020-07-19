
package com.NanShop.mall.service;

import com.NanShop.mall.controller.vo.NanShopMallOrderDetailVO;
import com.NanShop.mall.controller.vo.NanShopOrderItemVO;
import com.NanShop.mall.controller.vo.NanShopMallShoppingCartItemVO;
import com.NanShop.mall.controller.vo.NanShopMallUserVO;
import com.NanShop.mall.entity.NanShopMallOrder;
import com.NanShop.mall.util.PageQueryUtil;
import com.NanShop.mall.util.PageResult;

import java.util.List;

public interface NanShopMallOrderService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getNanShopMallOrdersPage(PageQueryUtil pageUtil);

    /**
     * 订单信息修改
     *
     * @param nanShopMallOrder
     * @return
     */
    String updateOrderInfo(NanShopMallOrder nanShopMallOrder);

    /**
     * 配货
     *
     * @param ids
     * @return
     */
    String checkDone(Long[] ids);

    /**
     * 出库
     *
     * @param ids
     * @return
     */
    String checkOut(Long[] ids);

    /**
     * 关闭订单
     *
     * @param ids
     * @return
     */
    String closeOrder(Long[] ids);

    /**
     * 保存订单
     *
     * @param user
     * @param myShoppingCartItems
     * @return
     */
    String saveOrder(NanShopMallUserVO user, List<NanShopMallShoppingCartItemVO> myShoppingCartItems);

    /**
     * 获取订单详情
     *
     * @param orderNo
     * @param userId
     * @return
     */
    NanShopMallOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId);

    /**
     * 获取订单详情
     *
     * @param orderNo
     * @return
     */
    NanShopMallOrder getNanShopMallOrderByOrderNo(String orderNo);

    /**
     * 我的订单列表
     *
     * @param pageUtil
     * @return
     */
    PageResult getMyOrders(PageQueryUtil pageUtil);

    /**
     * 手动取消订单
     *
     * @param orderNo
     * @param userId
     * @return
     */
    String cancelOrder(String orderNo, Long userId);

    /**
     * 确认收货
     *
     * @param orderNo
     * @param userId
     * @return
     */
    String finishOrder(String orderNo, Long userId);

    String paySuccess(String orderNo, int payType);

    List<NanShopOrderItemVO> getOrderItems(Long id);
}
