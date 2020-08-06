package com.fh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.entity.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDao extends BaseMapper<Order> {

}
