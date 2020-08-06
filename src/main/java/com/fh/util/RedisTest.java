package com.fh.util;

import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;

public class RedisTest {


    public static void main(String[] args) {
        Jedis jedis = new Jedis("120.53.100.89");
        Set<String> keys = jedis.keys("*");

        for (String key:keys) {
            String type = jedis.type(key);
            //如果获取的集合类型等于String
            if(RedisType.STRING.getEn().equals(type)){
                String s = jedis.get(key);
                System.out.println("key:"+key+",类型："+ RedisType.STRING.getCn()+",值："+s);
            //如果获取的集合类型等于Hash
            }else if(RedisType.HASH.getEn().equals(type)){
                Set<String> fileds = jedis.hkeys(key);
                for (String filed:fileds) {
                    System.out.println("key:" + key + ",类型：" + RedisType.HASH.getCn() + ",fileds：" + filed + ",value：" + jedis.hget(key, filed));
                }
            //如果获取的集合类型等于List
            }else if(RedisType.LIST.getEn().equals(type)){
                List<String> teacher = jedis.lrange(key, 0, 3);
                for (String tea:teacher) {
                    System.out.println("key:" + key + ",类型：" + RedisType.LIST.getCn() + ",fileds：" + tea + ",value：" + jedis.lrange(key,0,3));
                }
            }




        }



    }







}
