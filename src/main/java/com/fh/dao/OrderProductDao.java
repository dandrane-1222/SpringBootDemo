package com.fh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.entity.OrderProduct;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderProductDao extends BaseMapper<OrderProduct> {

    public void batchAdd(@Param("list") List<OrderProduct> list, @Param("oid") Integer oid);

}
