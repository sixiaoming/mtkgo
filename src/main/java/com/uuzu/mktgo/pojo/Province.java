/*
 * Copyright 2015-2020 msun.com All right reserved.
 */
package com.uuzu.mktgo.pojo;

import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "mapping_province_sdk")
public class Province {

    private String province;
    private String ch_name;
}
