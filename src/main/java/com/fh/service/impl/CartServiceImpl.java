package com.fh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fh.dao.ProductDao;
import com.fh.entity.Product;
import com.fh.entity.ProductCart;
import com.fh.service.CartService;
import com.fh.util.RedisUse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ProductDao productDao;

    @Override
    public Integer addProductToCart(Integer proId, Integer count) {

        if(count>0){
            //判断库存是否够
            Product product = productDao.selectById(proId);
            if(count>product.getStorageCount()){
                return product.getStorageCount()-count;
            }
        }
        // 获取用户信息
        Map login_user = (Map) request.getAttribute("login_user");
        String iphone = (String) login_user.get("iphone");

        // 获取购物车中指定商品的信息
        String proInfo = RedisUse.hget("cart_" + iphone + "_ydd", proId + "");

        // 判断商品是否存在购物车
        if(StringUtils.isEmpty(proInfo)){
            // 查询商品信息
            ProductCart productCart = productDao.queryProductCartById(proId);
            productCart.setCheck(true);
            productCart.setCount(count);
            // 小计
            BigDecimal money=productCart.getPrice().multiply(new BigDecimal(count));
            productCart.setMoney(money);

            // 转成json字符串
            String productCartString = JSONObject.toJSONString(productCart);
            // 数据放入redis
            RedisUse.hset("cart_"+iphone+"_ydd",proId+"",productCartString);

        }else{ // 已经在购物车中 就修改个数和小计

            // 将json字符串转为java
            ProductCart productCart = JSONObject.parseObject(proInfo, ProductCart.class);
            // 修改个数
            productCart.setCount(productCart.getCount()+count);

            // 判断库存是否够
            Product product = productDao.selectById(proId);//数据库的数量
            if(productCart.getCount()>product.getStorageCount()){//判断库存是否够
                return product.getStorageCount()-productCart.getCount();
            }

            // 计算小计
            BigDecimal money=productCart.getPrice().multiply(new BigDecimal(productCart.getCount()));
            productCart.setMoney(money);
            // 将商品信息 转成json字符串
            String productCartString = JSONObject.toJSONString(productCart);
            // 将数据放入redis
            RedisUse.hset("cart_"+iphone+"_ydd",proId+"",productCartString);

        }

        // 获取商品种类的个数: hlen方法
        long hlen = RedisUse.hlen("cart_" + iphone + "_ydd");
        return (int)hlen;

    }


    @Override
    public List queryAllProductCart() {
        //查询当前用户的所有购物车数据
        //获取用户信息
        Map login_user = (Map) request.getAttribute("login_user");
        String iphone = (String) login_user.get("iphone");

        //获取购物车的所有数据
        List<String> productCarts = RedisUse.hvals("cart_" + iphone + "_ydd");
        return productCarts;
    }

    @Override
    public void updateCartProductStatus(String pids) {
        //查询当前用户的所有购物车数据
        //获取用户信息
        Map login_user = (Map) request.getAttribute("login_user");
        String iphone = (String) login_user.get("iphone");

        //获取购物车的所有数据
        List<String> productCarts = RedisUse.hvals("cart_" + iphone + "_ydd");
        for (int i = 0; i < productCarts.size(); i++) {
            //商品的json字符串
            String productStr = productCarts.get(i);
            //将商品信息转为对象
            ProductCart productCart = JSONObject.parseObject(productStr, ProductCart.class);
            // 判断此商品是否为要修改选中状态
            Integer id = productCart.getGoodsId();
            //判断此商品是否在选中的ids中  (redis 1,2,3,4          选中的 true     1,2     )
            if((","+pids).contains(","+id+",")==true){
                productCart.setCheck(true);
                RedisUse.hset("cart_" + iphone + "_ydd",productCart.getGoodsId()+"",JSONObject.toJSONString(productCart));
            }else{
                productCart.setCheck(false);
                RedisUse.hset("cart_" + iphone + "_ydd",productCart.getGoodsId()+"",JSONObject.toJSONString(productCart));
            }
        }
    }

    @Override
    public List<ProductCart> queryCheckedProductCart() {
        //从redis 取出购物车数据  返回
        //获取登录信息
        //获取用户信息
        Map login_user = (Map) request.getAttribute("login_user");
        String iphone = (String) login_user.get("iphone");

        //获取购物车数据
        //获取购物车的所有数据
        List<String> productCarts = RedisUse.hvals("cart_" + iphone + "_ydd");

        //实际需求的数据
        List<ProductCart> list=new ArrayList<>();
        //处理想要的数据
        for (int i = 0; i <productCarts.size() ; i++) {
            //购物车里的每一个商品信息
            String productCartStr = productCarts.get(i);
            //将字符串转为bean
            ProductCart productCart = JSONObject.parseObject(productCartStr, ProductCart.class);
            // 想要的是选中的数据
            if(productCart.isCheck()==true){
                list.add(productCart);
            }

        }
        return list;
    }

}
