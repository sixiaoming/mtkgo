package com.uuzu.mktgo.mapper;

import com.uuzu.mktgo.pojo.BaseModel;
import com.uuzu.mktgo.pojo.DictModel;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import java.util.List;


/**
 * @author zj_pc
 */
public interface DictMapper extends Mapper<DictModel> {

     /**
      * test
      * @return
      */

     List<String> queryNameInfo();


     List<DictModel> queryKVInfo(@Param("name") String name);
}
