package com.uuzu.mktgo.pojo;

import lombok.Data;

/**
 * @author zhoujin
 */
@Data
public class OverviewBaseModel {
    private String date;
    private double value;

    public OverviewBaseModel(String date, double value) {
        this.date = date;
        this.value = value;
    }
}
