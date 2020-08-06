package com.fh.service;

import com.fh.common.exception.CountException;

import java.util.Map;

public interface OrderService {

     Map creatOrder(Integer addressId, Integer payType) throws CountException;

     Map createMoneyPhoto(Integer oid) throws Exception;

     Integer queryPayStatus(Integer oid) throws Exception;

}
