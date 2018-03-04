/*
 * Copyright 2015-2020 msun.com All right reserved.
 */
package com.uuzu.mktgo.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "conversation_201711", type = "conversation_cycle_summary", shards = 5, refreshInterval = "-1")
public class CycleSummary {

    @Id
    private String row_key;

    private String model;
    private String country;
    private String brand;
    private String province;
    private String mnt;
    private String price_range;
    private String month;

    public CycleSummary(String model, String country, String brand, String province, String mnt, String price_range, String month) {
        this.model = model;
        this.country = country;
        this.brand = brand;
        this.province = province;
        this.mnt = mnt;
        this.price_range = price_range;
        this.month = month;
    }

    @Override
    public String toString() {
        return "CycleSummary{" + "row_key='" + row_key + '\'' + ", model='" + model + '\'' + ", country='" + country + '\'' + ", brand='" + brand + '\'' + ", province='" + province + '\'' + ", mnt='" + mnt + '\''
               + ", price_range='" + price_range + '\'' + ", month='" + month + '\'' + '}';
    }
}
