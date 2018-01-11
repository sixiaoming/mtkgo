package com.uuzu.mktgo.mapper;

import com.uuzu.mktgo.pojo.Brand;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
@Repository
public interface BrandMapper extends Mapper<Brand> {

//     List<Phone> queryPhoneLimit10();

        List<Brand> queryBrandsByRank();


}
