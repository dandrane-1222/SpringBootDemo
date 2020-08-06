package com.fh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fh.common.enums.PayStatusEnum;
import com.fh.common.exception.CountException;
import com.fh.dao.OrderDao;
import com.fh.dao.OrderProductDao;
import com.fh.dao.ProductDao;
import com.fh.entity.Order;
import com.fh.entity.OrderProduct;
import com.fh.entity.Product;
import com.fh.entity.ProductCart;
import com.fh.service.OrderService;
import com.fh.util.RedisUse;
import com.github.wxpay.sdk.FeiConfig;
import com.github.wxpay.sdk.WXPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Resource
    private HttpServletRequest request;
    @Resource
    private ProductDao productDao;
    @Resource
    private OrderProductDao orderProductDao;

    @Override
    public Map creatOrder(Integer addressId, Integer payType) throws CountException {

        Map map=new HashMap();

        List<OrderProduct> list=new ArrayList<>();

        Order order=new Order();
        order.setAddressId(addressId);
        order.setCreateDate(new Date());
        order.setPayType(payType);
        order.setPayStatus(PayStatusEnum.PAY_STATUS_INIT.getStatus());
        //设置商品清单个数
        Integer typeCount=0;
        //设置总金额
        BigDecimal totalMoney=new BigDecimal(0);

        Map login_user = (Map) request.getAttribute("login_user");
        String iphone = (String) login_user.get("iphone");

        List<String> productsStr = RedisUse.hvals("cart_" + iphone + "_ydd");
        for (int i = 0; i < productsStr.size(); i++) {
            //将字符串转换为javabean
            ProductCart productCart = JSONObject.parseObject(productsStr.get(i), ProductCart.class);
            //判断是否为订单中的商品
            if(productCart.isCheck()==true){
                //查询数据库的信息      判断库存是否够
                Product product = productDao.selectById(productCart.getGoodsId());
                if(product.getStorageCount()>=productCart.getCount()){

                    typeCount++;
                    totalMoney=totalMoney.add(productCart.getMoney());
                    //维护订单
                    OrderProduct orderProduct=new OrderProduct();
                    orderProduct.setCount(productCart.getCount());
                    orderProduct.setProductId(productCart.getGoodsId());
                    list.add(orderProduct);

                    int i1 = productDao.updateProductCount(product.getId(), productCart.getCount());
                    if(i1==0){//超卖
                        throw new CountException("商品编号为:"+productCart.getGoodsId()+"的库存不足,库存只有："+product.getStorageCount());
                    }
                }else{
                    throw new CountException("商品编号为:"+productCart.getGoodsId()+"的库存不足,库存只有："+product.getStorageCount());
                }
            }

        }
		
        order.setProTypeCount(typeCount);
        order.setTotalMoney(totalMoney);
        orderDao.insert(order);
        //保存订单
        orderProductDao.batchAdd(list,order.getId());
        //删除redis的数据
        for (int i = 0; i < productsStr.size(); i++) {
            //将字符串转换为javabean
            ProductCart productCart = JSONObject.parseObject(productsStr.get(i), ProductCart.class);
            if(productCart.isCheck()==true){
                RedisUse.hdel("cart_" + iphone + "_ydd",productCart.getGoodsId()+"");
            }
        }
        map.put("code",200);
        map.put("orderId",order.getId());
        map.put("totalMoney",totalMoney);
        return map;
    }

    @Override
    public Map createMoneyPhoto(Integer oid) throws Exception {
        Map rs=new HashMap();
        //从redsi中判断是否已经生成过
        String meonyPhotoUrl = RedisUse.get("order_"+oid+"_ydd");
        if(StringUtils.isEmpty(meonyPhotoUrl)!=true){//不为空  已经生成过二维码
            rs.put("code",200);
            rs.put("url",meonyPhotoUrl);
            return rs;
        }
        Order order = orderDao.selectById(oid);
        // 微信支付  natvie   商户生成二维码
        //配置配置信息
        FeiConfig config = new FeiConfig();
        //得到微信支付对象
        WXPay wxpay = new WXPay(config);
        //设置请求参数
        Map<String, String> data = new HashMap<String, String>();
        //对订单信息描述
        data.put("body", "飞狐电商666-订单支付");
        //String payId = System.currentTimeMillis()+"";
        //设置订单号 （保证唯一 ）
        data.put("out_trade_no","weixin1_order_ydd_"+oid);
        //设置币种
        data.put("fee_type", "CNY");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
        Date d=new Date();
        String dateStr = sdf.format(new Date(d.getTime() + 120000000));
        //设置二维码的失效时间
        data.put("time_expire", dateStr);
        //设置订单金额   单位分
        data.put("total_fee","1");
        data.put("notify_url", "http://www.example.com/wxpay/notify");
        //设置支付方式
        data.put("trade_type", "NATIVE");  // 此处指定为扫码支付
        // 统一下单
        Map<String, String> resp = wxpay.unifiedOrder(data);
        //这一块必须用log4j 做记录的
        System.out.println(oid+"下订单结果为:"+ JSONObject.toJSONString(resp));
        if("SUCCESS".equalsIgnoreCase(resp.get("return_code"))&&"SUCCESS".equalsIgnoreCase(resp.get("result_code"))){
            rs.put("code",200);
            rs.put("url",resp.get("code_url"));
            //更新订单状态
            order.setPayStatus(PayStatusEnum.PAY_STATUS_ING.getStatus());
            orderDao.updateById(order);
            //将二维码存入redis  设置失效时间
            RedisUse.set("order_"+oid+"_ydd",resp.get("code_url"),30*60);
        }else {
            rs.put("code",600);
            rs.put("info",resp.get("return_msg"));
        }
        return rs;
    }

    @Override
    public Integer queryPayStatus(Integer oid) throws Exception {
        FeiConfig config = new FeiConfig();
        WXPay wxpay = new WXPay(config);
        Map<String, String> data = new HashMap<String, String>();
        data.put("out_trade_no","weixin1_order_ydd_"+oid);
        // 查询支付状态
        Map<String, String> resp = wxpay.orderQuery(data);
        System.out.println("查询结果："+JSONObject.toJSONString(resp));
        if("SUCCESS".equalsIgnoreCase(resp.get("return_code"))&&"SUCCESS".equalsIgnoreCase(resp.get("result_code"))){
            if("SUCCESS".equalsIgnoreCase(resp.get("trade_state"))){//支付成功
                //更新订单状态
                Order order=new Order();
                order.setId(oid);
                order.setPayStatus(PayStatusEnum.PAY_STATUS_SUCCESS.getStatus());
                orderDao.updateById(order);
                return 1;
            }else if("NOTPAY".equalsIgnoreCase(resp.get("trade_state"))){
                return 3;
            }else if("USERPAYING".equalsIgnoreCase(resp.get("trade_state"))){
                return 2;
            }
        }
        return 0;
    }
}
