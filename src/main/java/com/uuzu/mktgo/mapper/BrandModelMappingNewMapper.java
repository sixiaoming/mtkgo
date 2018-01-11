package com.uuzu.mktgo.mapper;

import com.uuzu.mktgo.pojo.BrandModelMappingNewModel;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface BrandModelMappingNewMapper extends Mapper<BrandModelMappingNewModel> {


        /**
         * 查询机型信息
         * @param model
         * @return
         */
        BrandModelMappingNewModel queryModelInfoByModel(String model);


        String queryPrice(String model);

}
