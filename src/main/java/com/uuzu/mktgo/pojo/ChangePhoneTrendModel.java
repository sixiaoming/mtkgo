/*
 * Copyright 2015-2020 msun.com All right reserved.
 */
package com.uuzu.mktgo.pojo;

import lombok.Data;

/**
 * Created by shieh on 2017/10/19.
 */
@Data
public class ChangePhoneTrendModel {

    private SourcePortraitModel source;
    private TrendPortraitModel  trend;
    private TrendModel          modelInfo;
}
