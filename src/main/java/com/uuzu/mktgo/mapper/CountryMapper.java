/*
 * Copyright 2015-2020 msun.com All right reserved.
 */
package com.uuzu.mktgo.mapper;

import org.springframework.stereotype.Repository;

import tk.mybatis.mapper.common.Mapper;

import com.uuzu.mktgo.pojo.Country;

@Repository
public interface CountryMapper extends Mapper<Country> {

}
