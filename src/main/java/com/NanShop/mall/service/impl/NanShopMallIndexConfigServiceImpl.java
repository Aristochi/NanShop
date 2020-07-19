
package com.NanShop.mall.service.impl;

import com.NanShop.mall.common.ServiceResultEnum;
import com.NanShop.mall.controller.vo.NanShopMallIndexConfigGoodsVO;
import com.NanShop.mall.dao.IndexConfigMapper;
import com.NanShop.mall.dao.NanShopMallGoodsMapper;
import com.NanShop.mall.entity.NanShopMallGoods;
import com.NanShop.mall.util.BeanUtil;
import com.NanShop.mall.util.PageQueryUtil;
import com.NanShop.mall.util.PageResult;
import com.NanShop.mall.entity.IndexConfig;
import com.NanShop.mall.service.NanShopMallIndexConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NanShopMallIndexConfigServiceImpl implements NanShopMallIndexConfigService {

    @Autowired
    private IndexConfigMapper indexConfigMapper;

    @Autowired
    private NanShopMallGoodsMapper goodsMapper;

    @Override
    public PageResult getConfigsPage(PageQueryUtil pageUtil) {
        List<IndexConfig> indexConfigs = indexConfigMapper.findIndexConfigList(pageUtil);
        int total = indexConfigMapper.getTotalIndexConfigs(pageUtil);
        PageResult pageResult = new PageResult(indexConfigs, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String saveIndexConfig(IndexConfig indexConfig) {
        //todo 判断是否存在该商品
        if (indexConfigMapper.insertSelective(indexConfig) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateIndexConfig(IndexConfig indexConfig) {
        //todo 判断是否存在该商品
        IndexConfig temp = indexConfigMapper.selectByPrimaryKey(indexConfig.getConfigId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        if (indexConfigMapper.updateByPrimaryKeySelective(indexConfig) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public IndexConfig getIndexConfigById(Long id) {
        return null;
    }

    @Override
    public List<NanShopMallIndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number) {
        List<NanShopMallIndexConfigGoodsVO> nanShopMallIndexConfigGoodsVOS = new ArrayList<>(number);
        List<IndexConfig> indexConfigs = indexConfigMapper.findIndexConfigsByTypeAndNum(configType, number);
        if (!CollectionUtils.isEmpty(indexConfigs)) {
            //取出所有的goodsId
            List<Long> goodsIds = indexConfigs.stream().map(IndexConfig::getGoodsId).collect(Collectors.toList());
            List<NanShopMallGoods> nanShopMallGoods = goodsMapper.selectByPrimaryKeys(goodsIds);
            nanShopMallIndexConfigGoodsVOS = BeanUtil.copyList(nanShopMallGoods, NanShopMallIndexConfigGoodsVO.class);
            for (NanShopMallIndexConfigGoodsVO nanShopMallIndexConfigGoodsVO : nanShopMallIndexConfigGoodsVOS) {
                String goodsName = nanShopMallIndexConfigGoodsVO.getGoodsName();
                String goodsIntro = nanShopMallIndexConfigGoodsVO.getGoodsIntro();
                // 字符串过长导致文字超出的问题
                if (goodsName.length() > 30) {
                    goodsName = goodsName.substring(0, 30) + "...";
                    nanShopMallIndexConfigGoodsVO.setGoodsName(goodsName);
                }
                if (goodsIntro.length() > 22) {
                    goodsIntro = goodsIntro.substring(0, 22) + "...";
                    nanShopMallIndexConfigGoodsVO.setGoodsIntro(goodsIntro);
                }
            }
        }
        return nanShopMallIndexConfigGoodsVOS;
    }

    @Override
    public Boolean deleteBatch(Long[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //删除数据
        return indexConfigMapper.deleteBatch(ids) > 0;
    }
}
