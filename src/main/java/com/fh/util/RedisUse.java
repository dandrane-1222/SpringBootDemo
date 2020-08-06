package com.fh.util;

import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;

public class RedisUse {

    public static void set(String key,String value){
        Jedis jedis = RedisUtils.getJedis();
        jedis.set(key,value);
        RedisUtils.returnJedis(jedis);
    }

    public static void set(String key,String value,int seconds){
        Jedis jedis = RedisUtils.getJedis();
        jedis.setex(key,seconds,value);
        RedisUtils.returnJedis(jedis);
    }

    public static String get(String key){
        Jedis jedis = RedisUtils.getJedis();
        String value=jedis.get(key);
        RedisUtils.returnJedis(jedis);
        return value;
    }

    public static void hset(String key,String filed,String value){
        Jedis jedis = RedisUtils.getJedis();
        Long hset = jedis.hset(key, filed, value);
        RedisUtils.returnJedis(jedis);
    }

    public static String hget(String key,String filed){
        Jedis jedis = RedisUtils.getJedis();
        String hget = jedis.hget(key, filed);
        RedisUtils.returnJedis(jedis);
        return hget;
    }

    //获取hash中key的个数
    public static long hlen(String key){
        Jedis jedis = RedisUtils.getJedis();
        Long hlen = jedis.hlen(key);
        RedisUtils.returnJedis(jedis);
        return hlen;
    }

    public static List<String> hvals(String key){
        Jedis jedis = RedisUtils.getJedis();
        List<String> hvals = jedis.hvals(key);
        RedisUtils.returnJedis(jedis);
        return hvals;
    }

    public static String getAreaNames(String areaIds) {
        StringBuffer sb=new StringBuffer();//中文名
        String[] split = areaIds.split(",");
        for (int i = 0; i <split.length ; i++) {
            String areaId=split[i];
            String areaStr = RedisUse.hget("area_ydd", areaId);
            sb.append(areaStr).append("-");
        }
        return sb.toString();
    }

    public static void hdel(String key,String filed){
        Jedis jedis = RedisUtils.getJedis();
        jedis.hdel(key,filed);
        RedisUtils.returnJedis(jedis);
    }

    public static boolean exists(String key){
        Jedis jedis = RedisUtils.getJedis();
        Boolean exists = jedis.exists(key);
        RedisUtils.returnJedis(jedis);
        return exists;
    }

    public static Map<String, String> hgetAll(String key){
        Jedis jedis = RedisUtils.getJedis();
        Map<String, String> map = jedis.hgetAll(key);
        RedisUtils.returnJedis(jedis);
        return map;
    }

}
