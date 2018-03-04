/*
 * Copyright 2015-2020 msun.com All right reserved.
 */
package com.uuzu.mktgo.pojo;

import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "mapping_country_sdk")
public class Country {

    private String  zone;
    private String  en_name;
    private String  ch_name;
    private Integer is_hot;
}
