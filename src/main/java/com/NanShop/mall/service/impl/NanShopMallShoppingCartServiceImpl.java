
package com.NanShop.mall.service.impl;

import com.NanShop.mall.common.Constants;
import com.NanShop.mall.common.ServiceResultEnum;
import com.NanShop.mall.controller.vo.NanShopMallShoppingCartItemVO;
import com.NanShop.mall.controller.vo.NanShopMallUserVO;
import com.NanShop.mall.dao.NanShopMallGoodsMapper;
import com.NanShop.mall.dao.NanShopMallShoppingCartItemMapper;
import com.NanShop.mall.util.BeanUtil;
import com.NanShop.mall.entity.NanShopMallGoods;
import com.NanShop.mall.entity.NanShopMallShoppingCartItem;
import com.NanShop.mall.service.NanShopMallShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class NanShopMallShoppingCartServiceImpl implements NanShopMallShoppingCartService {

    @Autowired
    private NanShopMallShoppingCartItemMapper nanShopMallShoppingCartItemMapper;

    @Autowired
    private NanShopMallGoodsMapper nanShopMallGoodsMapper;

    //todo 修改session中购物项数量

    @Override
    public String saveNanShopMallCartItem(NanShopMallShoppingCartItem nanShopMallShoppingCartItem) {
        NanShopMallShoppingCartItem temp = nanShopMallShoppingCartItemMapper.selectByUserIdAndGoodsId(nanShopMallShoppingCartItem.getUserId(), nanShopMallShoppingCartItem.getGoodsId());
        if (temp != null) {
            //已存在则修改该记录
            //todo count = tempCount + 1
            temp.setGoodsCount(nanShopMallShoppingCartItem.getGoodsCount());
            return updateNanShopMallCartItem(temp);
        }
        NanShopMallGoods nanShopMallGoods = nanShopMallGoodsMapper.selectByPrimaryKey(nanShopMallShoppingCartItem.getGoodsId());
        //商品为空
        if (nanShopMallGoods == null) {
            return ServiceResultEnum.GOODS_NOT_EXIST.getResult();
        }
        int totalItem = nanShopMallShoppingCartItemMapper.selectCountByUserId(nanShopMallShoppingCartItem.getUserId()) + 1;
        //超出单个商品的最大数量
        if (nanShopMallShoppingCartItem.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        //超出最大数量
        if (totalItem > Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_TOTAL_NUMBER_ERROR.getResult();
        }
        //保存记录
        if (nanShopMallShoppingCartItemMapper.insertSelective(nanShopMallShoppingCartItem) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateNanShopMallCartItem(NanShopMallShoppingCartItem nanShopMallShoppingCartItem) {
        NanShopMallShoppingCartItem nanShopMallShoppingCartItemUpdate = nanShopMallShoppingCartItemMapper.selectByPrimaryKey(nanShopMallShoppingCartItem.getCartItemId());
        if (nanShopMallShoppingCartItemUpdate == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        //超出单个商品的最大数量
        if (nanShopMallShoppingCartItem.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        //todo 数量相同不会进行修改
        //todo userId不同不能修改
        nanShopMallShoppingCartItemUpdate.setGoodsCount(nanShopMallShoppingCartItem.getGoodsCount());
        nanShopMallShoppingCartItemUpdate.setUpdateTime(new Date());
        //修改记录
        if (nanShopMallShoppingCartItemMapper.updateByPrimaryKeySelective(nanShopMallShoppingCartItemUpdate) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public NanShopMallShoppingCartItem getNanShopMallCartItemById(Long nanShopMallShoppingCartItemId) {
        return nanShopMallShoppingCartItemMapper.selectByPrimaryKey(nanShopMallShoppingCartItemId);
    }

    @Override
    public Boolean deleteById(Long nanShopMallShoppingCartItemId) {
        //todo userId不同不能删除
        return nanShopMallShoppingCartItemMapper.deleteByPrimaryKey(nanShopMallShoppingCartItemId) > 0;
    }

    @Override
    public List<NanShopMallShoppingCartItemVO> getMyShoppingCartItems(Long nanShopMallUserId) {
        List<NanShopMallShoppingCartItemVO> nanShopMallShoppingCartItemVOS = new ArrayList<>();
        List<NanShopMallShoppingCartItem> nanShopMallShoppingCartItems = nanShopMallShoppingCartItemMapper.selectByUserId(nanShopMallUserId, Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER);
        if (!CollectionUtils.isEmpty(nanShopMallShoppingCartItems)) {
            //查询商品信息并做数据转换
            List<Long> nanShopMallGoodsIds = nanShopMallShoppingCartItems.stream().map(NanShopMallShoppingCartItem::getGoodsId).collect(Collectors.toList());
            List<NanShopMallGoods> nanShopMallGoods = nanShopMallGoodsMapper.selectByPrimaryKeys(nanShopMallGoodsIds);
            Map<Long, NanShopMallGoods> nanShopMallGoodsMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(nanShopMallGoods)) {
                nanShopMallGoodsMap = nanShopMallGoods.stream().collect(Collectors.toMap(NanShopMallGoods::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
            }
            for (NanShopMallShoppingCartItem nanShopMallShoppingCartItem : nanShopMallShoppingCartItems) {
                NanShopMallShoppingCartItemVO nanShopMallShoppingCartItemVO = new NanShopMallShoppingCartItemVO();
                BeanUtil.copyProperties(nanShopMallShoppingCartItem, nanShopMallShoppingCartItemVO);
                if (nanShopMallGoodsMap.containsKey(nanShopMallShoppingCartItem.getGoodsId())) {
                    NanShopMallGoods nanShopMallGoodsTemp = nanShopMallGoodsMap.get(nanShopMallShoppingCartItem.getGoodsId());
                    nanShopMallShoppingCartItemVO.setGoodsCoverImg(nanShopMallGoodsTemp.getGoodsCoverImg());
                    String goodsName = nanShopMallGoodsTemp.getGoodsName();
                    // 字符串过长导致文字超出的问题
                    if (goodsName.length() > 28) {
                        goodsName = goodsName.substring(0, 28) + "...";
                    }
                    nanShopMallShoppingCartItemVO.setGoodsName(goodsName);
                    nanShopMallShoppingCartItemVO.setSellingPrice(nanShopMallGoodsTemp.getSellingPrice());
                    nanShopMallShoppingCartItemVOS.add(nanShopMallShoppingCartItemVO);
                }
            }
        }
        return nanShopMallShoppingCartItemVOS;
    }
}
