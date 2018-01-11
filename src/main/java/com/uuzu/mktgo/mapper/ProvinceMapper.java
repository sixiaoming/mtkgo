package com.uuzu.mktgo.mapper;

import com.uuzu.mktgo.pojo.Province;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface ProvinceMapper extends Mapper<Province> {
    List<Province> queryInfo();


}
