package com.uuzu.mktgo.pojo;

import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name="mapping_person_summary_sdk")
public class DictModel {
    private String per_name;
    private String per_key;
    private String per_value;

}
