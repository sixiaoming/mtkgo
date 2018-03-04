package com.uuzu.mktgo.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "conversation_201711", type = "brand_fans_summary", shards = 5, refreshInterval = "-1")
public class BrandFansSummary {

    @Id
    private String row_key;

    private String carrier_new;
    private String agebin;
    private String car;
    private String country;
    private String brand;
    private String segment;
    private String married;
    private String edu;
    private String network_new;
    private String income;
    private String house;
    private String province;
    private String kids;
    private String mnt;
    private String occupation;
    private String gender;

    public BrandFansSummary(String country, String brand, String province, String mnt) {
        this.country = country;
        this.brand = brand;
        this.province = province;
        this.mnt = mnt;
    }

    @Override
    public String toString() {
        return "BrandFansSummary{" + "row_key='" + row_key + '\'' + ", carrier_new='" + carrier_new + '\'' + ", agebin='" + agebin + '\'' + ", car='" + car + '\'' + ", country='" + country + '\'' + ", brand='" + brand
               + '\'' + ", segment='" + segment + '\'' + ", married='" + married + '\'' + ", edu='" + edu + '\'' + ", network_new='" + network_new + '\'' + ", income='" + income + '\'' + ", house='" + house + '\''
               + ", province='" + province + '\'' + ", kids='" + kids + '\'' + ", mnt='" + mnt + '\'' + ", occupation='" + occupation + '\'' + ", gender='" + gender + '\'' + '}';
    }
}
