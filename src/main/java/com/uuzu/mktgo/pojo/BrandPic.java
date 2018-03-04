/*
 * Copyright 2015-2020 mob.com All right reserved.
 */
package com.uuzu.mktgo.pojo;

import javax.persistence.Table;

import lombok.Data;

/**
 * @author zhoujin
 */
@Data
@Table(name = "brand_pic")
public class BrandPic {

    private String ID;
    private String picName;
    private String brand;
}
