package com.fh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@SpringBootApplication //声明启动类
@MapperScan("com.fh.dao")
public class App {

    //main方法
    public static void main( String[] args ) {
        SpringApplication.run(App.class,args);
    }


}
