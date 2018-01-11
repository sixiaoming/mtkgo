/**
  * Copyright 2017 bejson.com 
  */
package com.uuzu.mktgo.pojo;

import lombok.Data;

/**
 * @author zj_pc
 */
@Data
public class BaseModelByPercent {

    private String key;
    private String value;

    public BaseModelByPercent(String key, String value) {
        this.key = key;
        this.value = value;
    }
}