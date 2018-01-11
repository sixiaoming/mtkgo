package com.uuzu.mktgo.pojo;

import lombok.Data;

import java.util.List;

/**
 * Created by shieh on 2017/10/25.
 */
@Data
public class TrendModel {
    private String brand;
    private String model;
    private List<String> picAddress;
    private String price;
    private String publicTime;

    public TrendModel(){
        super();
    }
    public TrendModel(String brand, String model, List<String> picAddress, String price, String publicTime) {
        this.brand = brand;
        this.model = model;
        this.picAddress = picAddress;
        this.price = price;
        this.publicTime = publicTime;

    }
}
