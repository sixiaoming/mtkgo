package com.uuzu.mktgo.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "mktgo", type = "conversation_persona_summary", shards = 5, refreshInterval = "-1")
public class ConversationPersonaSummary {

    @Id
    private String row_key;

    private String network;
    private String carrier;
    private String model;
    private String agebin;
    private String car;
    private String brand_new;
    private String country;
    private String brand;
    private String segment;
    private String married;
    private String edu;
    private String income;
    private String house;
    private String province;
    private String kids;
    private String mnt;
    private String price_range;
    private String occupation;
    private String model_new;
    private String gender;

    public ConversationPersonaSummary(String model, String brand_new, String country, String brand, String province, String mnt, String price_range, String model_new) {
        this.model = model;
        this.brand_new = brand_new;
        this.country = country;
        this.brand = brand;
        this.province = province;
        this.mnt = mnt;
        this.price_range = price_range;
        this.model_new = model_new;
    }

    @Override
    public String toString() {
        return "ConversationPersonaSummary{" + "row_key='" + row_key + '\'' + ", network='" + network + '\'' + ", carrier='" + carrier + '\'' + ", model='" + model + '\'' + ", agebin='" + agebin + '\'' + ", car='" + car
               + '\'' + ", brand_new='" + brand_new + '\'' + ", country='" + country + '\'' + ", brand='" + brand + '\'' + ", segment='" + segment + '\'' + ", married='" + married + '\'' + ", edu='" + edu + '\''
               + ", income='" + income + '\'' + ", house='" + house + '\'' + ", province='" + province + '\'' + ", kids='" + kids + '\'' + ", mnt='" + mnt + '\'' + ", price_range='" + price_range + '\''
               + ", occupation='" + occupation + '\'' + ", model_new='" + model_new + '\'' + ", gender='" + gender + '\'' + '}';
    }
}
