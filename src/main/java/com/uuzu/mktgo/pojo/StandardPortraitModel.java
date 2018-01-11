/**
 * Copyright 2017 bejson.com
 */
package com.uuzu.mktgo.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author zj_pc
 */
@Data
public class StandardPortraitModel {

    private List<BaseModel> gender;
    private List<BaseModel> agebin;
    private List<BaseModel> income;
    private List<BaseModel> segment;
    private List<BaseModel> occupation;
    private List<BaseModel> edu;
    private List<BaseModel> network;
    private List<BaseModel> married;
    private List<BaseModel> kids;
    private List<BaseModel> house;
    private List<BaseModel> car;
    private List<BaseModel> carrier;
}