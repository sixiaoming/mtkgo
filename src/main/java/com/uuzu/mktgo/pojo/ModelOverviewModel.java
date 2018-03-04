/*
 * Copyright 2015-2020 msun.com All right reserved.
 */
package com.uuzu.mktgo.pojo;

import java.util.List;

import lombok.Data;

/**
 * @author zhoujin
 */
@Data
public class ModelOverviewModel {

    private String                      model;
    private String                      price;
    private String                      uptime;
    private List<OverviewBaseListModel> market_share_model; // 机型市场份额趋势
    private List<BaseModel>             genders;
    private List<BaseModel>             ageBins;
    private List<String>                pics;
}
