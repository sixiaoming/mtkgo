package com.uuzu.mktgo.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "conversation_201711", type = "conversation_source", shards = 5, refreshInterval = "-1")
public class ConversationPersonaSummaryShrink {
    @Id
    private String row_key;
    private String brand_new;
    private String country;
    private String province;
    private String mnt;
    private String price_range;
    private String model_new;
    private String car;
    private String married;
    private String house;

    public ConversationPersonaSummaryShrink(String brand_new, String country, String province, String mnt, String price_range, String model_new) {
        this.brand_new = brand_new;
        this.country = country;
        this.province = province;
        this.mnt = mnt;
        this.price_range = price_range;
        this.model_new = model_new;
    }

    @Override
    public String toString() {
        return "ConversationPersonaSummaryShrink{" +
                "row_key='" + row_key + '\'' +
                ", brand_new='" + brand_new + '\'' +
                ", country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", mnt='" + mnt + '\'' +
                ", price_range='" + price_range + '\'' +
                ", model_new='" + model_new + '\'' +
                ", car='" + car + '\'' +
                ", married='" + married + '\'' +
                ", house='" + house + '\'' +
                '}';
    }
}
