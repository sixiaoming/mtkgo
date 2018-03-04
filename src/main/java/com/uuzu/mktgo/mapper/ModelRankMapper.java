/*
 * Copyright 2015-2020 msun.com All right reserved.
 */
package com.uuzu.mktgo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import tk.mybatis.mapper.common.Mapper;

import com.uuzu.mktgo.pojo.Model;

@Repository
public interface ModelRankMapper extends Mapper<Model> {

    List<String> queryModelsByBrand(@Param("brand") String brand);
}
