package com.uuzu.mktgo.pojo;

import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name="mapping_province_sdk")
public class Province {
    private String province;
    private String ch_name;

}
