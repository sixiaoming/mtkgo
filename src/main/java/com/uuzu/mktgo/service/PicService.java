package com.uuzu.mktgo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uuzu.mktgo.mapper.BrandPicMapper;

/**
 * @author zhoujin
 */
@Service
public class PicService {

    @Autowired
    BrandPicMapper brandPicMapper;

    /**
     * 通过品牌获取图片信息
     * 
     * @param brand
     * @return
     */
    public String getPicUrl(String brand) {
        String picUrl = brandPicMapper.queryPicByBrand(brand);
        return "http://oy0hr93xz.bkt.clouddn.com/" + picUrl;
    }
}
