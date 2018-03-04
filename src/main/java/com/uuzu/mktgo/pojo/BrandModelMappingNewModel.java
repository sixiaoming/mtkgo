/*
 * Copyright 2015-2020 msun.com All right reserved.
 */
package com.uuzu.mktgo.pojo;

import javax.persistence.Table;

import lombok.Data;

/**
 * @author zhoujin
 */
@Data
@Table(name = "brand_model_mapping_new")
public class BrandModelMappingNewModel {

    private String brand_cn;
    private String clean_brand;
    private String clean_model;
    private String brand;
    private String model;
    private String price;
    private String public_time;
    private String model_pic_address;
}
