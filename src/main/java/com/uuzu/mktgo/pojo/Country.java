package com.uuzu.mktgo.pojo;

import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name="mapping_country_sdk")
public class Country {
    private String zone;
    private String en_name;
    private String ch_name;
    private Integer is_hot;

}
