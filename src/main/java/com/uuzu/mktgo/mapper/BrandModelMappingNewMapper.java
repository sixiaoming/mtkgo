/*
 * Copyright 2015-2020 msun.com All right reserved.
 */
package com.uuzu.mktgo.mapper;

import org.springframework.stereotype.Repository;

import tk.mybatis.mapper.common.Mapper;

import com.uuzu.mktgo.pojo.BrandModelMappingNewModel;

@Repository
public interface BrandModelMappingNewMapper extends Mapper<BrandModelMappingNewModel> {

    /**
     * 查询机型信息
     * 
     * @param model
     * @return
     */
    BrandModelMappingNewModel queryModelInfoByModel(String model);

    String queryPrice(String model);
}
