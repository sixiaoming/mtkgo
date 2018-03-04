/*
 * Copyright 2015-2020 msun.com All right reserved.
 */
package com.uuzu.mktgo.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import tk.mybatis.mapper.common.Mapper;

import com.uuzu.mktgo.pojo.Province;

@Repository
public interface ProvinceMapper extends Mapper<Province> {

    List<Province> queryInfo();
}
