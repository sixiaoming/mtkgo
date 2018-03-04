/*
 * Copyright 2015-2020 msun.com All right reserved.
 */
package com.uuzu.mktgo.pojo;

import java.util.List;

import lombok.Data;

@Data
public class RegionDistributionModel {

    private List<BaseModel> countryMarketShare;
    private List<BaseModel> provinceMarketShare;
    private List<BaseModel> country;
    private List<BaseModel> province;
}
