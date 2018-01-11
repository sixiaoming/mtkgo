package com.uuzu.mktgo.pojo;

import lombok.Data;

import javax.persistence.Table;

/**
 * @author zhoujin
 */
@Data
@Table(name="Data_Month")
public class DateMonth {
    String id;
    String month;
}
