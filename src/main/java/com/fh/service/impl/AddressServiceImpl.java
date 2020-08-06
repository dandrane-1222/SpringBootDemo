package com.fh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.dao.AddRessDao;
import com.fh.entity.AddRessInfo;
import com.fh.entity.Address;
import com.fh.service.AddRessService;
import com.fh.util.RedisUse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AddressServiceImpl implements AddRessService {
    @Autowired
    private AddRessDao addRessDao;

    @Autowired
    private HttpServletRequest request;
    @Override
    public List<AddRessInfo> queryAddressInfoList() {
        //获取当前登陆人
        Map user = (Map) request.getAttribute("login_user");
        String iphone = (String) user.get("iphone");
        //根据登陆人 查询收货地址
        //mybatis plus 如何进行条件查询    抽象类和接口有啥区别啊
        QueryWrapper<Address> qw=new QueryWrapper<>();
        qw.eq("vipId",iphone);
        //查询收数据库的数据
        List<Address> addresses = addRessDao.selectList(qw);
        //需求是数据库的数据 不满足
        List<AddRessInfo> list=new ArrayList<>();
        //处理真正想要的数据
        for (int i = 0; i <addresses.size() ; i++) {
            //业务bean
            AddRessInfo temp=new AddRessInfo();
            //数据库的数据
            Address address = addresses.get(i);
            temp.setId(address.getId());
            temp.setIphone(address.getIphone());
            temp.setName(address.getName());
            //取得数据库的数据   id，id，id
            String areaIds = address.getAreaIds();
            //取的地区id 对应的中文名
            String areaNames= RedisUse.getAreaNames(areaIds);
            //设置收货地址  省市县+详细地址
            temp.setAddress(areaNames+address.getDetailAdd());
            temp.setIscheck(address.getIscheck());
            list.add(temp);
        }
        return list;
    }


}
