package com.fh.controller;

import com.fh.common.JsonData;
import com.fh.entity.AddRessInfo;
import com.fh.service.AddRessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("addressController")
public class AddressController {

    @Autowired
    private AddRessService addRessService;

    /**
       查询当前用户的收货地址
     */
    @RequestMapping("queryAddress")
    public JsonData queryAddress(){

        List<AddRessInfo> addressInfoList = addRessService.queryAddressInfoList();

        return  JsonData.getJsonSuccess(addressInfoList);
    }



}
