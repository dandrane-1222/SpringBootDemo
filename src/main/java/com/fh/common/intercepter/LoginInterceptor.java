package com.fh.common.intercepter;

import com.fh.common.exception.NoLoginException;
import com.fh.util.JWT;
import com.fh.util.RedisUse;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Map;

public class LoginInterceptor extends HandlerInterceptorAdapter {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从header里取数据
        String token = request.getHeader("token");
        // 验证头信息是否完整
        if(StringUtils.isEmpty(token)){
            // 请求路径记录存入redis
            String requestURI = request.getRequestURI();
            throw new NoLoginException("未登录");
        }
        // 解密  字节数组
        byte[] decode = Base64.getDecoder().decode(token);
        // 字节数组转为字符串  字符串格式: iphone+","+sign
        String signToken = new String(decode);
        //判断数据是否被篡改
        String[] split = signToken.split(",");
        if (split.length != 2){
            throw new NoLoginException("未登录");
        }
        String iphone = split[0];
        // jwt的秘钥
        String sign = split[1];
        Map user = JWT.unsign(sign, Map.class);
        if(user == null){
            System.out.println("未登录");
            //返回json字符串
            throw new NoLoginException("未登录");
        }
        //jwt验证通过
        if(user!=null){
            String sign_redis = RedisUse.get("token_"+iphone);
            if(!sign.equals(sign_redis)){
                //返回json字符串
                throw new NoLoginException("验证过期,请重新登录");
            }
        }
        //前面验证通过后  设置redis key值的有效时间 为30
        RedisUse.set("token_"+user.get("iphone"),sign,60*30);
        //将用户信息放入request中  方便后面需求处理
        request.setAttribute("login_user",user);
        return true;
    }




}
