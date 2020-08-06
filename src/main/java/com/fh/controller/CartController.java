package com.fh.controller;

import com.fh.common.JsonData;
import com.fh.entity.ProductCart;
import com.fh.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("cartController")
public class CartController {

    @Autowired
    private CartService cartService;

    @RequestMapping("addCart")
    public JsonData addCart(@RequestParam("goodsId") Integer proId, Integer count){
        Integer count_type = cartService.addProductToCart(proId, count);

        return JsonData.getJsonSuccess(count_type);
    }

    @RequestMapping("queryCartListByUser")
    public JsonData queryCartListByUser(){
        List list = cartService.queryAllProductCart();

        return JsonData.getJsonSuccess(list);
    }

    /**   更新购物车中商品状态 */
    @RequestMapping("updateCartProductStatus")
    public JsonData updateCartProductStatus(String pids){
        cartService.updateCartProductStatus(pids);
        return JsonData.getJsonSuccess("修改成功");
    }

    /**
        查询购物车中要结算的商品信息
        返回的数据格式 [{"id":id,}]
     */
    @RequestMapping("queryCheckProduct")
    public JsonData queryCheckProduct(){
        List<ProductCart> productCarts = cartService.queryCheckedProductCart();
        return JsonData.getJsonSuccess(productCarts);
    }




}
