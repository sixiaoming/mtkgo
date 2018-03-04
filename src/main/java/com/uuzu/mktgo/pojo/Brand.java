/*
 * Copyright 2015-2020 mob.com All right reserved.
 */
package com.uuzu.mktgo.pojo;

import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "brand_rank_monthly")
public class Brand {

    private String brand;
    private String brand_name;
    private int    rank;
    private int    imei_count;
    private String month;
}
