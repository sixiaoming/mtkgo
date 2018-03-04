/*
 * Copyright 2015-2020 msun.com All right reserved.
 */
package com.uuzu.mktgo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import tk.mybatis.mapper.common.Mapper;

import com.uuzu.mktgo.pojo.DictModel;

/**
 * @author zj_pc
 */
public interface DictMapper extends Mapper<DictModel> {

    /**
     * test
     * 
     * @return
     */

    List<String> queryNameInfo();

    List<DictModel> queryKVInfo(@Param("name") String name);
}
