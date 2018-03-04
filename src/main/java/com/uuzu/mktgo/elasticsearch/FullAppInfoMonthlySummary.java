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
@Document(indexName = "rp_mobeye_mktgo", type = "mktgo_full_app_info_monthly_summary", shards = 5, refreshInterval = "-1")
public class FullAppInfoMonthlySummary {

    @Id
    private String row_key;
    private String model;
    private String country;
    private String brand;
    private String province;
    private String mnt;
    private String apppkg;
    private String price_range;

    public FullAppInfoMonthlySummary(String brand, String model, String price_range, String country, String province, String date) {
        this.model = model;
        this.country = country;
        this.brand = brand;
        this.province = province;
        this.price_range = price_range;
        this.mnt = date;
    }

    @Override
    public String toString() {
        return "FullAppInfoMonthlySummary{" + "model='" + model + '\'' + ", country='" + country + '\'' + ", brand='" + brand + '\'' + ", province='" + province + '\'' + ", row_key='" + row_key + '\'' + ", mnt='" + mnt
               + '\'' + ", apppkg='" + apppkg + '\'' + ", price_range='" + price_range + '\'' + '}';
    }
}
