package com.fh.controller;

import com.fh.common.JsonData;
import com.fh.util.JWT;
import com.fh.util.RedisUse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("vipController")
public class VipController {

    @RequestMapping("sendMessage")
    public JsonData sendMessage(String iphone){
        //发短信
        //String code = MessageUtils.sendMsg(iphone);
        String code="1111";
        //存redis
        RedisUse.set(iphone+"_ydd",code,60*5);
        return JsonData.getJsonSuccess("短信发送成功");
    }

    /** 登陆 */
    @RequestMapping("login")
    public JsonData login(String iphone, String code, HttpServletRequest request){

        Map logMap=new HashMap();
        // 正确将用户信息存入session中 获取code
        String redis_code = RedisUse.get(iphone + "_ydd");
        if(redis_code!=null && redis_code.equals(code)){

            Map user=new HashMap();
            user.put("iphone",iphone);
            String sign = JWT.sign(user,1000 * 60 * 60 * 24);
            // 加签 防止篡改数据
            String token = Base64.getEncoder().encodeToString((iphone+","+sign).getBytes());
            // 将最新的秘钥保存到redis中  生成多个秘钥  只有最新的有用
            RedisUse.set("token_"+iphone,sign,60*30);

            logMap.put("status","200");
            logMap.put("message","登录成功");
            // 签名
            logMap.put("token",token);
        }else{
            logMap.put("status","300");
            logMap.put("message","用户不存在 或 验证码错误");
        }
        return JsonData.getJsonSuccess(logMap);

    }


    @RequestMapping("test")
    public JsonData test(String name, Integer age){
        return JsonData.getJsonSuccess("success");
    }



}
