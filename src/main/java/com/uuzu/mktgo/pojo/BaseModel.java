/*
 * Copyright 2015-2020 mob.com All right reserved.
 */
package com.uuzu.mktgo.pojo;

import lombok.Data;

/**
 * @author zj_pc
 */
@Data
public class BaseModel {

    private String key;
    private double value;

    public BaseModel(String key, double value) {
        this.key = key;
        this.value = value;
    }
}
