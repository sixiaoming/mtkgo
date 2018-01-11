package com.uuzu.mktgo.config.mybatis;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 *
 * @author lixing
 * @date 2017/4/26
 *
 * 所有数据层Mapper都需要继承该接口
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
