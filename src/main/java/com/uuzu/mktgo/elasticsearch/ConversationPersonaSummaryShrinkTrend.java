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
@Document(indexName = "conversation_201711", type = "conversation_dest", shards = 5, refreshInterval = "-1")
public class ConversationPersonaSummaryShrinkTrend {

    @Id
    private String row_key;
    private String brand;
    private String country;
    private String province;
    private String mnt;
    private String price_range;
    private String model;
    private String car;
    private String married;
    private String house;

    public ConversationPersonaSummaryShrinkTrend(String brand, String country, String province, String mnt, String price_range, String model) {
        this.brand = brand;
        this.country = country;
        this.province = province;
        this.mnt = mnt;
        this.price_range = price_range;
        this.model = model;
    }

    @Override
    public String toString() {
        return "ConversationPersonaSummaryShrinkTrend{" + "row_key='" + row_key + '\'' + ", brand='" + brand + '\'' + ", country='" + country + '\'' + ", province='" + province + '\'' + ", mnt='" + mnt + '\''
               + ", price_range='" + price_range + '\'' + ", model='" + model + '\'' + ", car='" + car + '\'' + ", married='" + married + '\'' + ", house='" + house + '\'' + '}';
    }
}
