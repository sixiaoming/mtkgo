/*
 * Copyright 2015-2020 msun.com All right reserved.
 */
package com.uuzu.mktgo.pojo;

import lombok.Data;

/**
 * @author zhoujin
 */
@Data
public class MediaAnalysisModel {

    private String apppkg;
    private String icon;
    private String name;
    private String cate_id;
    private String cate_name;
    private String cnt1;
    private String cnt2;
    private String cnt3;
    private String cnt4;
    private double active_rate;
    private double index;

    private String cate_l2_id;
    private String cate_l2;

}
