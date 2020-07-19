
package com.NanShop.mall.controller.mall;

import com.NanShop.mall.common.Constants;
import com.NanShop.mall.common.ServiceResultEnum;
import com.NanShop.mall.controller.vo.NanShopMallShoppingCartItemVO;
import com.NanShop.mall.controller.vo.NanShopMallUserVO;
import com.NanShop.mall.util.Result;
import com.NanShop.mall.util.ResultGenerator;
import com.NanShop.mall.entity.NanShopMallShoppingCartItem;
import com.NanShop.mall.service.NanShopMallShoppingCartService;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ShoppingCartController {

    @Resource
    private NanShopMallShoppingCartService nanShopMallShoppingCartService;

    @GetMapping("/shop-cart")
    public String cartListPage(HttpServletRequest request,
                               HttpSession httpSession) {
        NanShopMallUserVO user = (NanShopMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        int itemsTotal = 0;
        int priceTotal = 0;
        List<NanShopMallShoppingCartItemVO> myShoppingCartItems = nanShopMallShoppingCartService.getMyShoppingCartItems(user.getUserId());
        if (!CollectionUtils.isEmpty(myShoppingCartItems)) {
            //购物项总数
            itemsTotal = myShoppingCartItems.stream().mapToInt(NanShopMallShoppingCartItemVO::getGoodsCount).sum();
            if (itemsTotal < 1) {
                return "error/error_5xx";
            }
            //总价
            for (NanShopMallShoppingCartItemVO nanShopMallShoppingCartItemVO : myShoppingCartItems) {
                priceTotal += nanShopMallShoppingCartItemVO.getGoodsCount() * nanShopMallShoppingCartItemVO.getSellingPrice();
            }
            if (priceTotal < 1) {
                return "error/error_5xx";
            }
        }
        request.setAttribute("itemsTotal", itemsTotal);
        request.setAttribute("priceTotal", priceTotal);
        request.setAttribute("myShoppingCartItems", myShoppingCartItems);
        return "mall/cart";
    }

    @PostMapping("/shop-cart")
    @ResponseBody
    public Result saveNanShopMallShoppingCartItem(@RequestBody NanShopMallShoppingCartItem nanShopMallShoppingCartItem,
                                                 HttpSession httpSession) {
        NanShopMallUserVO user = (NanShopMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        nanShopMallShoppingCartItem.setUserId(user.getUserId());
        //todo 判断数量
        String saveResult = nanShopMallShoppingCartService.saveNanShopMallCartItem(nanShopMallShoppingCartItem);
        //添加成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(saveResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //添加失败
        return ResultGenerator.genFailResult(saveResult);
    }

    @PutMapping("/shop-cart")
    @ResponseBody
    public Result updateNanShopMallShoppingCartItem(@RequestBody NanShopMallShoppingCartItem nanShopMallShoppingCartItem,
                                                   HttpSession httpSession) {
        NanShopMallUserVO user = (NanShopMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        nanShopMallShoppingCartItem.setUserId(user.getUserId());
        //todo 判断数量
        String updateResult = nanShopMallShoppingCartService.updateNanShopMallCartItem(nanShopMallShoppingCartItem);
        //修改成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(updateResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //修改失败
        return ResultGenerator.genFailResult(updateResult);
    }

    @DeleteMapping("/shop-cart/{nanShopMallShoppingCartItemId}")
    @ResponseBody
    public Result updateNanShopMallShoppingCartItem(@PathVariable("nanShopMallShoppingCartItemId") Long nanShopMallShoppingCartItemId,
                                                   HttpSession httpSession) {
        NanShopMallUserVO user = (NanShopMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        Boolean deleteResult = nanShopMallShoppingCartService.deleteById(nanShopMallShoppingCartItemId);
        //删除成功
        if (deleteResult) {
            return ResultGenerator.genSuccessResult();
        }
        //删除失败
        return ResultGenerator.genFailResult(ServiceResultEnum.OPERATE_ERROR.getResult());
    }

    @GetMapping("/shop-cart/settle")
    public String settlePage(HttpServletRequest request,
                             HttpSession httpSession) {
        int priceTotal = 0;
        NanShopMallUserVO user = (NanShopMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        List<NanShopMallShoppingCartItemVO> myShoppingCartItems = nanShopMallShoppingCartService.getMyShoppingCartItems(user.getUserId());
        if (CollectionUtils.isEmpty(myShoppingCartItems)) {
            //无数据则不跳转至结算页
            return "/shop-cart";
        } else {
            //总价
            for (NanShopMallShoppingCartItemVO nanShopMallShoppingCartItemVO : myShoppingCartItems) {
                priceTotal += nanShopMallShoppingCartItemVO.getGoodsCount() * nanShopMallShoppingCartItemVO.getSellingPrice();
            }
            if (priceTotal < 1) {
                return "error/error_5xx";
            }
        }
        request.setAttribute("priceTotal", priceTotal);
        request.setAttribute("myShoppingCartItems", myShoppingCartItems);
        return "mall/order-settle";
    }
}
