package com.fh.controller;

import com.fh.common.JsonData;
import com.fh.common.exception.CountException;
import com.fh.service.OrderService;
import com.fh.util.RedisUse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("orderController")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
        生成订单      收货地址  支付方式
     */
    @RequestMapping("createOrder")
    public JsonData createOrder(Integer addressId, Integer payType, String flag ) throws CountException {
        //处理接口幂等性    同一个请求 发送多次   结果只处理一次
        boolean exists = RedisUse.exists(flag);//判断redis是否存在key
        if(exists==true){//二次请求
            return JsonData.getJsonError(300,"请求处理中");
        }else{
            RedisUse.set(flag,"",10);
        }
        Map map = orderService.creatOrder(addressId, payType);
        return JsonData.getJsonSuccess(map);
    }


    /**
        生成二维码（有效时间30分钟  保证订单30分钟有效）
     */
    @RequestMapping("createMoneyPhoto")//提供访问路径
    public JsonData createMoneyPhoto(Integer orderId) throws Exception {
        Map meonyPhoto = orderService.createMoneyPhoto(orderId);
        return JsonData.getJsonSuccess(meonyPhoto);
    }

    // 1 支付成功  2 支付中  3 未支付
    @RequestMapping("queryPayStatus")
    public JsonData queryPayStatus(Integer orderId) throws Exception {
        Integer status = orderService.queryPayStatus(orderId);
        return JsonData.getJsonSuccess(status);
    }

}
