
package com.NanShop.mall.service.impl;

import com.NanShop.mall.common.*;
import com.NanShop.mall.controller.vo.*;
import com.NanShop.mall.dao.NanShopMallGoodsMapper;
import com.NanShop.mall.dao.NanShopMallOrderItemMapper;
import com.NanShop.mall.dao.NanShopMallOrderMapper;
import com.NanShop.mall.dao.NanShopMallShoppingCartItemMapper;
import com.NanShop.mall.entity.NanShopMallGoods;
import com.NanShop.mall.entity.StockNumDTO;
import com.NanShop.mall.util.BeanUtil;
import com.NanShop.mall.util.NumberUtil;
import com.NanShop.mall.util.PageQueryUtil;
import com.NanShop.mall.util.PageResult;
import com.NanShop.mall.entity.NanShopMallOrder;
import com.NanShop.mall.entity.NanShopMallOrderItem;
import com.NanShop.mall.service.NanShopMallOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class NanShopMallOrderServiceImpl implements NanShopMallOrderService {

    @Autowired
    private NanShopMallOrderMapper nanShopMallOrderMapper;
    @Autowired
    private NanShopMallOrderItemMapper nanShopMallOrderItemMapper;
    @Autowired
    private NanShopMallShoppingCartItemMapper nanShopMallShoppingCartItemMapper;
    @Autowired
    private NanShopMallGoodsMapper nanShopMallGoodsMapper;

    @Override
    public PageResult getNanShopMallOrdersPage(PageQueryUtil pageUtil) {
        List<NanShopMallOrder> nanShopMallOrders = nanShopMallOrderMapper.findNanShopMallOrderList(pageUtil);
        int total = nanShopMallOrderMapper.getTotalNanShopMallOrders(pageUtil);
        PageResult pageResult = new PageResult(nanShopMallOrders, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    @Transactional
    public String updateOrderInfo(NanShopMallOrder nanShopMallOrder) {
        NanShopMallOrder temp = nanShopMallOrderMapper.selectByPrimaryKey(nanShopMallOrder.getOrderId());
        //不为空且orderStatus>=0且状态为出库之前可以修改部分信息
        if (temp != null && temp.getOrderStatus() >= 0 && temp.getOrderStatus() < 3) {
            temp.setTotalPrice(nanShopMallOrder.getTotalPrice());
            temp.setUserAddress(nanShopMallOrder.getUserAddress());
            temp.setUpdateTime(new Date());
            if (nanShopMallOrderMapper.updateByPrimaryKeySelective(temp) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            }
            return ServiceResultEnum.DB_ERROR.getResult();
        }
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String checkDone(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<NanShopMallOrder> orders = nanShopMallOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (NanShopMallOrder nanShopMallOrder : orders) {
                if (nanShopMallOrder.getIsDeleted() == 1) {
                    errorOrderNos += nanShopMallOrder.getOrderNo() + " ";
                    continue;
                }
                if (nanShopMallOrder.getOrderStatus() != 1) {
                    errorOrderNos += nanShopMallOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //订单状态正常 可以执行配货完成操作 修改订单状态和更新时间
                if (nanShopMallOrderMapper.checkDone(Arrays.asList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行出库操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单的状态不是支付成功无法执行出库操作";
                } else {
                    return "你选择了太多状态不是支付成功的订单，无法执行配货完成操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String checkOut(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<NanShopMallOrder> orders = nanShopMallOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (NanShopMallOrder nanShopMallOrder : orders) {
                if (nanShopMallOrder.getIsDeleted() == 1) {
                    errorOrderNos += nanShopMallOrder.getOrderNo() + " ";
                    continue;
                }
                if (nanShopMallOrder.getOrderStatus() != 1 && nanShopMallOrder.getOrderStatus() != 2) {
                    errorOrderNos += nanShopMallOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //订单状态正常 可以执行出库操作 修改订单状态和更新时间
                if (nanShopMallOrderMapper.checkOut(Arrays.asList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行出库操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单的状态不是支付成功或配货完成无法执行出库操作";
                } else {
                    return "你选择了太多状态不是支付成功或配货完成的订单，无法执行出库操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String closeOrder(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<NanShopMallOrder> orders = nanShopMallOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (NanShopMallOrder nanShopMallOrder : orders) {
                // isDeleted=1 一定为已关闭订单
                if (nanShopMallOrder.getIsDeleted() == 1) {
                    errorOrderNos += nanShopMallOrder.getOrderNo() + " ";
                    continue;
                }
                //已关闭或者已完成无法关闭订单
                if (nanShopMallOrder.getOrderStatus() == 4 || nanShopMallOrder.getOrderStatus() < 0) {
                    errorOrderNos += nanShopMallOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //订单状态正常 可以执行关闭操作 修改订单状态和更新时间
                if (nanShopMallOrderMapper.closeOrder(Arrays.asList(ids), NanShopMallOrderStatusEnum.ORDER_CLOSED_BY_JUDGE.getOrderStatus()) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行关闭操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单不能执行关闭操作";
                } else {
                    return "你选择的订单不能执行关闭操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String saveOrder(NanShopMallUserVO user, List<NanShopMallShoppingCartItemVO> myShoppingCartItems) {
        List<Long> itemIdList = myShoppingCartItems.stream().map(NanShopMallShoppingCartItemVO::getCartItemId).collect(Collectors.toList());
        List<Long> goodsIds = myShoppingCartItems.stream().map(NanShopMallShoppingCartItemVO::getGoodsId).collect(Collectors.toList());
        List<NanShopMallGoods> nanShopMallGoods = nanShopMallGoodsMapper.selectByPrimaryKeys(goodsIds);
        //检查是否包含已下架商品
        List<NanShopMallGoods> goodsListNotSelling = nanShopMallGoods.stream()
                .filter(newBeeMallGoodsTemp -> newBeeMallGoodsTemp.getGoodsSellStatus() != Constants.SELL_STATUS_UP)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(goodsListNotSelling)) {
            //goodsListNotSelling 对象非空则表示有下架商品
            NanShopMallException.fail(goodsListNotSelling.get(0).getGoodsName() + "已下架，无法生成订单");
        }
        Map<Long, NanShopMallGoods> newBeeMallGoodsMap = nanShopMallGoods.stream().collect(Collectors.toMap(NanShopMallGoods::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
        //判断商品库存
        for (NanShopMallShoppingCartItemVO shoppingCartItemVO : myShoppingCartItems) {
            //查出的商品中不存在购物车中的这条关联商品数据，直接返回错误提醒
            if (!newBeeMallGoodsMap.containsKey(shoppingCartItemVO.getGoodsId())) {
                NanShopMallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
            }
            //存在数量大于库存的情况，直接返回错误提醒
            if (shoppingCartItemVO.getGoodsCount() > newBeeMallGoodsMap.get(shoppingCartItemVO.getGoodsId()).getStockNum()) {
                NanShopMallException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
            }
        }
        //删除购物项
        if (!CollectionUtils.isEmpty(itemIdList) && !CollectionUtils.isEmpty(goodsIds) && !CollectionUtils.isEmpty(nanShopMallGoods)) {
            if (nanShopMallShoppingCartItemMapper.deleteBatch(itemIdList) > 0) {
                List<StockNumDTO> stockNumDTOS = BeanUtil.copyList(myShoppingCartItems, StockNumDTO.class);
                int updateStockNumResult = nanShopMallGoodsMapper.updateStockNum(stockNumDTOS);
                if (updateStockNumResult < 1) {
                    NanShopMallException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
                }
                //生成订单号
                String orderNo = NumberUtil.genOrderNo();
                int priceTotal = 0;
                //保存订单
                NanShopMallOrder nanShopMallOrder = new NanShopMallOrder();
                nanShopMallOrder.setOrderNo(orderNo);
                nanShopMallOrder.setUserId(user.getUserId());
                nanShopMallOrder.setUserAddress(user.getAddress());
                //总价
                for (NanShopMallShoppingCartItemVO nanShopMallShoppingCartItemVO : myShoppingCartItems) {
                    priceTotal += nanShopMallShoppingCartItemVO.getGoodsCount() * nanShopMallShoppingCartItemVO.getSellingPrice();
                }
                if (priceTotal < 1) {
                    NanShopMallException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                nanShopMallOrder.setTotalPrice(priceTotal);
                //todo 订单body字段，用来作为生成支付单描述信息，暂时未接入第三方支付接口，故该字段暂时设为空字符串
                String extraInfo = "";
                nanShopMallOrder.setExtraInfo(extraInfo);
                //生成订单项并保存订单项纪录
                if (nanShopMallOrderMapper.insertSelective(nanShopMallOrder) > 0) {
                    //生成所有的订单项快照，并保存至数据库
                    List<NanShopMallOrderItem> nanShopMallOrderItems = new ArrayList<>();
                    for (NanShopMallShoppingCartItemVO nanShopMallShoppingCartItemVO : myShoppingCartItems) {
                        NanShopMallOrderItem nanShopMallOrderItem = new NanShopMallOrderItem();
                        //使用BeanUtil工具类将newBeeMallShoppingCartItemVO中的属性复制到newBeeMallOrderItem对象中
                        BeanUtil.copyProperties(nanShopMallShoppingCartItemVO, nanShopMallOrderItem);
                        //NewBeeMallOrderMapper文件insert()方法中使用了useGeneratedKeys因此orderId可以获取到
                        nanShopMallOrderItem.setOrderId(nanShopMallOrder.getOrderId());
                        nanShopMallOrderItems.add(nanShopMallOrderItem);
                    }
                    //保存至数据库
                    if (nanShopMallOrderItemMapper.insertBatch(nanShopMallOrderItems) > 0) {
                        //所有操作成功后，将订单号返回，以供Controller方法跳转到订单详情
                        return orderNo;
                    }
                    NanShopMallException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                NanShopMallException.fail(ServiceResultEnum.DB_ERROR.getResult());
            }
            NanShopMallException.fail(ServiceResultEnum.DB_ERROR.getResult());
        }
        NanShopMallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
        return ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult();
    }

    @Override
    public NanShopMallOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId) {
        NanShopMallOrder nanShopMallOrder = nanShopMallOrderMapper.selectByOrderNo(orderNo);
        if (nanShopMallOrder != null) {
            //todo 验证是否是当前userId下的订单，否则报错
            List<NanShopMallOrderItem> orderItems = nanShopMallOrderItemMapper.selectByOrderId(nanShopMallOrder.getOrderId());
            //获取订单项数据
            if (!CollectionUtils.isEmpty(orderItems)) {
                List<NanShopOrderItemVO> nanShopOrderItemVOS = BeanUtil.copyList(orderItems, NanShopOrderItemVO.class);
                NanShopMallOrderDetailVO nanShopMallOrderDetailVO = new NanShopMallOrderDetailVO();
                BeanUtil.copyProperties(nanShopMallOrder, nanShopMallOrderDetailVO);
                nanShopMallOrderDetailVO.setOrderStatusString(NanShopMallOrderStatusEnum.getNanShopMallOrderStatusEnumByStatus(nanShopMallOrderDetailVO.getOrderStatus()).getName());
                nanShopMallOrderDetailVO.setPayTypeString(PayTypeEnum.getPayTypeEnumByType(nanShopMallOrderDetailVO.getPayType()).getName());
                nanShopMallOrderDetailVO.setNanShopOrderItemVOS(nanShopOrderItemVOS);
                return nanShopMallOrderDetailVO;
            }
        }
        return null;
    }

    @Override
    public NanShopMallOrder getNanShopMallOrderByOrderNo(String orderNo) {
        return nanShopMallOrderMapper.selectByOrderNo(orderNo);
    }

    @Override
    public PageResult getMyOrders(PageQueryUtil pageUtil) {
        int total = nanShopMallOrderMapper.getTotalNanShopMallOrders(pageUtil);
        List<NanShopMallOrder> nanShopMallOrders = nanShopMallOrderMapper.findNanShopMallOrderList(pageUtil);
        List<NanShopMallOrderListVO> orderListVOS = new ArrayList<>();
        if (total > 0) {
            //数据转换 将实体类转成vo
            orderListVOS = BeanUtil.copyList(nanShopMallOrders, NanShopMallOrderListVO.class);
            //设置订单状态中文显示值
            for (NanShopMallOrderListVO nanShopMallOrderListVO : orderListVOS) {
                nanShopMallOrderListVO.setOrderStatusString(NanShopMallOrderStatusEnum.getNanShopMallOrderStatusEnumByStatus(nanShopMallOrderListVO.getOrderStatus()).getName());
            }
            List<Long> orderIds = nanShopMallOrders.stream().map(NanShopMallOrder::getOrderId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(orderIds)) {
                List<NanShopMallOrderItem> orderItems = nanShopMallOrderItemMapper.selectByOrderIds(orderIds);
                Map<Long, List<NanShopMallOrderItem>> itemByOrderIdMap = orderItems.stream().collect(groupingBy(NanShopMallOrderItem::getOrderId));
                for (NanShopMallOrderListVO nanShopMallOrderListVO : orderListVOS) {
                    //封装每个订单列表对象的订单项数据
                    if (itemByOrderIdMap.containsKey(nanShopMallOrderListVO.getOrderId())) {
                        List<NanShopMallOrderItem> orderItemListTemp = itemByOrderIdMap.get(nanShopMallOrderListVO.getOrderId());
                        //将NewBeeMallOrderItem对象列表转换成NewBeeMallOrderItemVO对象列表
                        List<NanShopOrderItemVO> nanShopOrderItemVOS = BeanUtil.copyList(orderItemListTemp, NanShopOrderItemVO.class);
                        nanShopMallOrderListVO.setNanShopOrderItemVOS(nanShopOrderItemVOS);
                    }
                }
            }
        }
        PageResult pageResult = new PageResult(orderListVOS, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String cancelOrder(String orderNo, Long userId) {
        NanShopMallOrder nanShopMallOrder = nanShopMallOrderMapper.selectByOrderNo(orderNo);
        if (nanShopMallOrder != null) {
            //todo 验证是否是当前userId下的订单，否则报错
            //todo 订单状态判断
            if (nanShopMallOrderMapper.closeOrder(Collections.singletonList(nanShopMallOrder.getOrderId()), NanShopMallOrderStatusEnum.ORDER_CLOSED_BY_MALLUSER.getOrderStatus()) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String finishOrder(String orderNo, Long userId) {
        NanShopMallOrder nanShopMallOrder = nanShopMallOrderMapper.selectByOrderNo(orderNo);
        if (nanShopMallOrder != null) {
            //todo 验证是否是当前userId下的订单，否则报错
            //todo 订单状态判断
            nanShopMallOrder.setOrderStatus((byte) NanShopMallOrderStatusEnum.ORDER_SUCCESS.getOrderStatus());
            nanShopMallOrder.setUpdateTime(new Date());
            if (nanShopMallOrderMapper.updateByPrimaryKeySelective(nanShopMallOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String paySuccess(String orderNo, int payType) {
        NanShopMallOrder nanShopMallOrder = nanShopMallOrderMapper.selectByOrderNo(orderNo);
        if (nanShopMallOrder != null) {
            //todo 订单状态判断 非待支付状态下不进行修改操作
            nanShopMallOrder.setOrderStatus((byte) NanShopMallOrderStatusEnum.OREDER_PAID.getOrderStatus());
            nanShopMallOrder.setPayType((byte) payType);
            nanShopMallOrder.setPayStatus((byte) PayStatusEnum.PAY_SUCCESS.getPayStatus());
            nanShopMallOrder.setPayTime(new Date());
            nanShopMallOrder.setUpdateTime(new Date());
            if (nanShopMallOrderMapper.updateByPrimaryKeySelective(nanShopMallOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public List<NanShopOrderItemVO> getOrderItems(Long id) {
        NanShopMallOrder nanShopMallOrder = nanShopMallOrderMapper.selectByPrimaryKey(id);
        if (nanShopMallOrder != null) {
            List<NanShopMallOrderItem> orderItems = nanShopMallOrderItemMapper.selectByOrderId(nanShopMallOrder.getOrderId());
            //获取订单项数据
            if (!CollectionUtils.isEmpty(orderItems)) {
                List<NanShopOrderItemVO> nanShopOrderItemVOS = BeanUtil.copyList(orderItems, NanShopOrderItemVO.class);
                return nanShopOrderItemVOS;
            }
        }
        return null;
    }
}