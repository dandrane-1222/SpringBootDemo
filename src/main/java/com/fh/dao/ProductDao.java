package com.fh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.entity.Product;
import com.fh.entity.ProductCart;
import com.fh.entity.ProductVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDao extends BaseMapper<Product> {

    List<ProductVo> queryHotProduct();

    List<Product> queryAreaData(Product product);

    List<Product> queryProductDataById(Integer goodsId);

    List<Product> queryProductDataByTypeId(Product product);

    ProductCart queryProductCartById(Integer proId);

    int updateProductCount(@Param("goodsId") Integer pid, @Param("count") Integer count);
}
