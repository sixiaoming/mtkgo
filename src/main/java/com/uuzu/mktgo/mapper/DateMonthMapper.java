package com.uuzu.mktgo.mapper;

import com.uuzu.mktgo.pojo.DateMonth;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface DateMonthMapper extends Mapper<DateMonth> {
    String queryMaxMonth();

    List<String> queryAllMonth();

}
