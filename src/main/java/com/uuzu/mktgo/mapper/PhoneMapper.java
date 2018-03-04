/*
 * Copyright 2015-2020 msun.com All right reserved.
 */
package com.uuzu.mktgo.mapper;

import java.util.List;

import tk.mybatis.mapper.common.Mapper;

import com.uuzu.mktgo.pojo.Phone;

/**
 * @author zj_pc
 */
public interface PhoneMapper extends Mapper<Phone> {

    List<Phone> queryPhoneLimit10();
}
