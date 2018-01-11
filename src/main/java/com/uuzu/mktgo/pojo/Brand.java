package com.uuzu.mktgo.pojo;

import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name="brand_rank_monthly")
public class Brand {
    private String brand;
    private String brand_name;
    private int rank;
    private int imei_count;
    private String month;

}
