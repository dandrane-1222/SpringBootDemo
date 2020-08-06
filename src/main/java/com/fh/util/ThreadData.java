package com.fh.util;

import java.util.Map;

public class ThreadData {
    private static ThreadLocal<Map> threadLocal=new ThreadLocal<>();

    public static void setData(Map map){
        threadLocal.set(map);
    }

    public static Map getData(){
        return threadLocal.get();
    }

}
