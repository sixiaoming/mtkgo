package com.uuzu.mktgo.pojo;

import lombok.Data;

import java.util.List;

@Data
public class RegionDistributionModel {

    private List<BaseModel> countryMarketShare;
    private List<BaseModel> provinceMarketShare;
    private List<BaseModel> country;
    private List<BaseModel> province;
}
