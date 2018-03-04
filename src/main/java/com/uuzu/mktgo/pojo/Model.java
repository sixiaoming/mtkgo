/*
 * Copyright 2015-2020 msun.com All right reserved.
 */
package com.uuzu.mktgo.pojo;

import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "model_rank_monthly")
public class Model {

    private String brand;
    private String brand_name;
    private String model;
    private int    rank1;
    private int    rank2;
    private int    imei_count;
    private String month;
}
