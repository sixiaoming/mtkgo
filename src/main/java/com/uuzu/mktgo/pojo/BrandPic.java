package com.uuzu.mktgo.pojo;

import lombok.Data;

import javax.persistence.Table;

/**
 * @author zhoujin
 */
@Data
@Table(name="brand_pic")
public class BrandPic {
    private String  ID;
    private String picName;
    private String brand;
}
