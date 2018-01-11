package com.uuzu.mktgo.mapper;

import com.uuzu.mktgo.pojo.Phone;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author zj_pc
 */
public interface PhoneMapper extends Mapper<Phone> {

     /**
      * test
      * @return
      */
     List<Phone> queryPhoneLimit10();
}
