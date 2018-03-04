/*
 * Copyright 2015-2020 msun.com All right reserved.
 */
package com.uuzu.mktgo.pojo;

import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "mapping_person_summary_sdk")
public class DictModel {

    private String per_name;
    private String per_key;
    private String per_value;
}
