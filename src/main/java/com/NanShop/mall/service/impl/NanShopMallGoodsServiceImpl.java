
package com.NanShop.mall.service.impl;

import com.NanShop.mall.common.ServiceResultEnum;
import com.NanShop.mall.dao.NanShopMallGoodsMapper;
import com.NanShop.mall.util.BeanUtil;
import com.NanShop.mall.util.PageQueryUtil;
import com.NanShop.mall.util.PageResult;
import com.NanShop.mall.controller.vo.NanShopMallSearchGoodsVO;
import com.NanShop.mall.entity.NanShopMallGoods;
import com.NanShop.mall.service.NanShopMallGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class NanShopMallGoodsServiceImpl implements NanShopMallGoodsService {

    @Autowired
    private NanShopMallGoodsMapper goodsMapper;

    @Override
    public PageResult getNanShopMallGoodsPage(PageQueryUtil pageUtil) {
        List<NanShopMallGoods> goodsList = goodsMapper.findNanShopMallGoodsList(pageUtil);
        int total = goodsMapper.getTotalNanShopMallGoods(pageUtil);
        PageResult pageResult = new PageResult(goodsList, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String saveNanShopMallGoods(NanShopMallGoods goods) {
        if (goodsMapper.insertSelective(goods) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public void batchSaveNanShopMallGoods(List<NanShopMallGoods> nanShopMallGoodsList) {
        if (!CollectionUtils.isEmpty(nanShopMallGoodsList)) {
            goodsMapper.batchInsert(nanShopMallGoodsList);
        }
    }

    @Override
    public void deleteGoods(NanShopMallGoods goods) {

    }

    @Override
    public String updateNanShopMallGoods(NanShopMallGoods goods) {
        NanShopMallGoods temp = goodsMapper.selectByPrimaryKey(goods.getGoodsId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        goods.setUpdateTime(new Date());
        if (goodsMapper.updateByPrimaryKeySelective(goods) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public NanShopMallGoods getNanShopMallGoodsById(Long id) {
        return goodsMapper.selectByPrimaryKey(id);
    }
    
    @Override
    public Boolean batchUpdateSellStatus(Long[] ids, int sellStatus) {
        return goodsMapper.batchUpdateSellStatus(ids, sellStatus) > 0;
    }

    @Override
    public PageResult searchNanShopMallGoods(PageQueryUtil pageUtil) {
        List<NanShopMallGoods> goodsList = goodsMapper.findNanShopMallGoodsListBySearch(pageUtil);
        int total = goodsMapper.getTotalNanShopMallGoodsBySearch(pageUtil);
        List<NanShopMallSearchGoodsVO> nanShopMallSearchGoodsVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(goodsList)) {
            nanShopMallSearchGoodsVOS = BeanUtil.copyList(goodsList, NanShopMallSearchGoodsVO.class);
            for (NanShopMallSearchGoodsVO nanShopMallSearchGoodsVO : nanShopMallSearchGoodsVOS) {
                String goodsName = nanShopMallSearchGoodsVO.getGoodsName();
                String goodsIntro = nanShopMallSearchGoodsVO.getGoodsIntro();
                // 字符串过长导致文字超出的问题
                if (goodsName.length() > 28) {
                    goodsName = goodsName.substring(0, 28) + "...";
                    nanShopMallSearchGoodsVO.setGoodsName(goodsName);
                }
                if (goodsIntro.length() > 30) {
                    goodsIntro = goodsIntro.substring(0, 30) + "...";
                    nanShopMallSearchGoodsVO.setGoodsIntro(goodsIntro);
                }
            }
        }
        PageResult pageResult = new PageResult(nanShopMallSearchGoodsVOS, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
}
