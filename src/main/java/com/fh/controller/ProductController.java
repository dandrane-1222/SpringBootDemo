package com.fh.controller;

import com.alibaba.fastjson.JSONObject;
import com.fh.common.JsonData;
import com.fh.dao.ProductDao;
import com.fh.entity.Area;
import com.fh.entity.Product;
import com.fh.entity.ProductVo;
import com.fh.entity.Type;
import com.fh.service.AreaService;
import com.fh.service.ProductService;
import com.fh.service.TypeService;
import com.fh.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.util.List;

@RestController
@RequestMapping("productController")
public class ProductController {

    @Autowired
    private TypeService typeService;
    @Autowired
    private ProductService productService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private ProductDao productDao;

    /** redis查询类型 */
    @RequestMapping("queryTypeRedis")
    public JsonData queryTypeRedis(){
        Jedis jedis= RedisUtils.getJedis();
        List<Type> types = typeService.queryAllData();
        //这些数据 放到redis string
        String jsonString = JSONObject.toJSONString(types);
        jedis.set("typeJson",jsonString);

        RedisUtils.returnJedis(jedis);
        return JsonData.getJsonSuccess(jsonString);
    }

    /** redis查询热销 */
    @RequestMapping("queryHotProductData")
    public JsonData queryHotProductData(){

        Jedis jedis = RedisUtils.getJedis();
        String hotProduct = jedis.get("hotProduct");
        if(StringUtils.isEmpty(hotProduct)== true){
            List<ProductVo> productVos = productService.queryHotProductData();
            hotProduct = JSONObject.toJSONString(productVos);
            jedis.set("hotProduct",hotProduct);
        }
        //用完后放回
        RedisUtils.returnJedis(jedis);
        return JsonData.getJsonSuccess(hotProduct);
    }

    /** redis查询地区*/
    @RequestMapping("queryRedisArea")
    public JsonData queryRedisArea(){
        Jedis jedis= RedisUtils.getJedis();
        if(jedis.get("areaList")!=null){
            String areaList = jedis.get("areaList");
            return JsonData.getJsonSuccess(areaList);
        }
        List<Area> areaList = areaService.queryAllData();
        String s = JSONObject.toJSONString(areaList);
        jedis.set("areaList", s);

        RedisUtils.returnJedis(jedis);
        return JsonData.getJsonSuccess(areaList);
    }

    /** 查询商品数据 */
    @RequestMapping("queryAllProductData")
    public JsonData queryAllProductData(Integer goodsId){

        List<Product> productList = productService.queryProductDataById(goodsId);

        return JsonData.getJsonSuccess(productList);
    }


    @RequestMapping("queryProductDataByTypeId")
    public JsonData queryProductDataByTypeId(Product product){
        List<Product> Product = productService.queryProductDataByTypeId(product);

        return JsonData.getJsonSuccess(Product);
    }


}
