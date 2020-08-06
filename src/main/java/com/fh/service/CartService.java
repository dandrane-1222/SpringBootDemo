package com.fh.service;

import com.fh.entity.ProductCart;

import java.util.List;

public interface CartService {

     Integer addProductToCart(Integer proId, Integer count);

     List queryAllProductCart();

     void updateCartProductStatus(String pids);

     List<ProductCart> queryCheckedProductCart();
}
