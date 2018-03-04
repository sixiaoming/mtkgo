/*
 * Copyright 2015-2020 msun.com All right reserved.
 */
package com.uuzu.mktgo.pojo;

import java.util.List;

import lombok.Data;

/**
 * Created by shieh on 2017/10/18.
 */
@Data
public class FansPortraitModel {

    private List<BaseModel> gender;
    private List<BaseModel> agebin;
    private List<BaseModel> income;
    private List<BaseModel> segment;
    private List<BaseModel> occupation;
    private List<BaseModel> edu;
    private List<BaseModel> network_new;
    private List<BaseModel> married;
    private List<BaseModel> kids;
    private List<BaseModel> house;
    private List<BaseModel> car;
    private List<BaseModel> carrier_new;
}
