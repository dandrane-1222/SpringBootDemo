package com.fh.common.config;

import com.fh.common.intercepter.KuaYuInterceptor;
import com.fh.common.intercepter.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//声明是配置文件类
@Configuration
public class TestIntercepterConfig implements WebMvcConfigurer {

    //注册TestInterceptor拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(new KuaYuInterceptor());
        //全部路径被拦截
        registration.addPathPatterns("/**");

        InterceptorRegistration loginInterceptor = registry.addInterceptor(new LoginInterceptor());
        //全部路径被拦截
        loginInterceptor.addPathPatterns("/cartController/**");
        loginInterceptor.addPathPatterns("/addressController/**");
        loginInterceptor.addPathPatterns("/orderController/**");
    }


}
