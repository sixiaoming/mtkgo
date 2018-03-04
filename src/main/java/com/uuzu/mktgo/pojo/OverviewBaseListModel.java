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
public class OverviewBaseListModel {

    private String                  name;
    private List<OverviewBaseModel> data;

    public OverviewBaseListModel(String name, List<OverviewBaseModel> data) {
        this.name = name;
        this.data = data;
    }
}
