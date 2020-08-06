package com.fh.service;

import com.fh.entity.Product;
import com.fh.entity.ProductVo;

import java.util.List;

public interface ProductService {


     List<ProductVo> queryHotProductData();

     List<Product> queryAreaData(Product product);

     List<Product> queryProductDataById(Integer goodsId);

     List<Product> queryProductDataByTypeId(Product product);



}
