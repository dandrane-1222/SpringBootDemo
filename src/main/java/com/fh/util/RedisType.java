package com.fh.util;

public enum RedisType {
    //type(cn,en)
    STRING("字符串","string"),
    HASH("hash","hash"),
    LIST("有序集合","list"),
    SET("无序集合","set"),
    ZSET("排序的有序集合","zset"),
    ;
    private String en;
    private String cn;

    private RedisType(String cn, String en){
        this.cn=cn;
        this.en=en;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }
}
