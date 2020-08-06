package com.fh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.entity.Area;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository //声明持久层
public interface AreaDao extends BaseMapper<Area> {

    List<String> queryAreaNameByIds(@Param("areaIds") String areaIds);

}
