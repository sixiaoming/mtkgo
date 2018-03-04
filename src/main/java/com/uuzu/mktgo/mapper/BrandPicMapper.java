/*
 * Copyright 2015-2020 msun.com All right reserved.
 */
package com.uuzu.mktgo.mapper;

import tk.mybatis.mapper.common.Mapper;

import com.uuzu.mktgo.pojo.BrandPic;

/**
 * @author zhoujin
 */
public interface BrandPicMapper extends Mapper<BrandPic> {

    String queryPicByBrand(String brand);
}
