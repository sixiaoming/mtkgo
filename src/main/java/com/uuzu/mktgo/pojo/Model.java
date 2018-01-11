package com.uuzu.mktgo.pojo;

import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name="model_rank_monthly")
public class Model {
    private String brand;
    private String brand_name;
    private String model;
    private int rank1;
    private int rank2;
    private int imei_count;
    private String month;

}
