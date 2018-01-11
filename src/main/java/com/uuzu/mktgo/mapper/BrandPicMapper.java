package com.uuzu.mktgo.mapper;

import com.uuzu.mktgo.pojo.BrandPic;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author zhoujin
 */
public interface BrandPicMapper extends Mapper<BrandPic> {

    String queryPicByBrand(String brand);
}
