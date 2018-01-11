package com.uuzu.mktgo.pojo;

import lombok.Data;

import java.util.List;

/**
 * Created by shieh on 2017/10/19.
 */
@Data
public class TrendPortraitModel {
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
    private List<BaseModel> trendModel;
}
