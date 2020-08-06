package com.fh.service.impl;

import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import com.fh.dao.AreaDao;
import com.fh.dao.ProductDao;
import com.fh.entity.Product;
import com.fh.entity.ProductVo;
import com.fh.service.ProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductDao productDao;
    @Resource
    private AreaDao areaDao;

    /** 热销 */
    @Override
    public List<ProductVo> queryHotProductData() {

        return productDao.queryHotProduct();
    }

    /** 地区 */
    @Override
    public List<Product> queryAreaData(Product product) {
        List<Product> list = productDao.queryAreaData(product);

        for (int i = 0; i < list.size(); i++) {
            String areaIds = list.get(i).getAreaIds();
            if(StringUtils.isEmpty(areaIds)==false){
                List<String> strings = areaDao.queryAreaNameByIds(areaIds);
                StringBuffer sb=new StringBuffer();
                for (int j = 0; j < strings.size(); j++) {
                    sb.append(strings.get(j)).append(",");
                }
                list.get(i).setAreaIds(sb.toString().substring(0,sb.lastIndexOf(",")));
            }
        }
        return list;
    }

    /** 全部商品数据 */
    @Override
    public List<Product> queryProductDataById(Integer goodsId) {
        List<Product> list = productDao.queryProductDataById(goodsId);

        for (int i = 0; i < list.size(); i++) {
            String areaIds = list.get(i).getAreaIds();
            if(StringUtils.isEmpty(areaIds)==false){
                List<String> strings = areaDao.queryAreaNameByIds(areaIds);
                StringBuffer ff=new StringBuffer();
                for (int j = 0; j < strings.size(); j++) {
                    ff.append(strings.get(j)).append(" ");
                }
                list.get(i).setAreaIds(ff.toString());
            }
        }
        return list;
    }

    @Override
    public List<Product> queryProductDataByTypeId(Product product) {
        List<Product> Product = productDao.queryProductDataByTypeId(product);
        return Product;
    }




}
