package com.uuzu.mktgo.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author zhoujin
 */
@Data
public class OverviewModel {
    private String brand;
    private int holding;
    private double market_share_rank;
    private List<OverviewBaseListModel> market_share_brand;//品牌市场份额趋势
    private List<OverviewBaseListModel> market_share_model;//机型市场份额趋势
    private List<BaseModel> holding_rank;//品牌保有量排名
    private List<BaseModel> hot_selling_rank;//热销机型排名
    private List<BaseModel> px_rank;//分辨率排名
    private List<BaseModel> hot_sell_price_rank;//热销机型价位ModelPriceTop10
    private List<BaseModel> os_rank;//操作系统排名
    private String brandPic;//品牌图片
}
