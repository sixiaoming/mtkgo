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
@Document(indexName = "rp_mobeye_mktgo", type = "overview_all", shards = 5, refreshInterval = "-1")
public class PersonaSummary_final {

    @Id
    private String row_key;

    private String married;
    private String agg1;
    private String agg2;
    private String imei_count_incr;
    private String edu;
    private String mnt;
    private String carrier;
    private String sysver;
    private String brand_name;
    private String kids;
    private String brand;
    private String model;
    private String segment;
    private String price;
    private String car;
    private String province;
    private String imei_count;
    private String network;
    private String house;
    private String price_range;
    private String agebin;
    private String gender;
    private String occupation;
    private String country;
    private String screensize;
    private String income;

    @Override
    public String toString() {
        return "PersonaSummary{" + "row_key='" + row_key + '\'' + ", married='" + married + '\'' + ", agg1='" + agg1 + '\'' + ", agg2='" + agg2 + '\'' + ", imei_count_incr='" + imei_count_incr + '\'' + ", edu='" + edu
               + '\'' + ", mnt='" + mnt + '\'' + ", carrier='" + carrier + '\'' + ", sysver='" + sysver + '\'' + ", brand_name='" + brand_name + '\'' + ", kids='" + kids + '\'' + ", brand='" + brand + '\'' + ", model='"
               + model + '\'' + ", segment='" + segment + '\'' + ", price='" + price + '\'' + ", car='" + car + '\'' + ", province='" + province + '\'' + ", imei_count='" + imei_count + '\'' + ", network='" + network
               + '\'' + ", house='" + house + '\'' + ", price_range='" + price_range + '\'' + ", agebin='" + agebin + '\'' + ", gender='" + gender + '\'' + ", occupation='" + occupation + '\'' + ", country='" + country
               + '\'' + ", screensize='" + screensize + '\'' + ", income='" + income + '\'' + '}';
    }

    public PersonaSummary_final(String model, String brand, String price_range, String country, String province, String mnt) {
        this.brand = brand;
        this.model = model;
        this.province = province;
        this.price_range = price_range;
        this.country = country;
        this.mnt = mnt;
    }
}
