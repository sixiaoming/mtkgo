package com.uuzu.mktgo.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author zhoujin
 */
@Data
public class OverviewBaseListModel {
    private String name;
    private List<OverviewBaseModel> data;

    public OverviewBaseListModel(String name, List<OverviewBaseModel> data) {
        this.name = name;
        this.data = data;
    }
}