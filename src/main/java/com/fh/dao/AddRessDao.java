package com.fh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.entity.Address;
import org.springframework.stereotype.Repository;

@Repository
public interface AddRessDao extends BaseMapper<Address> {
}
