package com.uuzu.mktgo.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uuzu.mktgo.mapper.*;
import com.uuzu.mktgo.pojo.Brand;
import com.uuzu.mktgo.pojo.Country;
import com.uuzu.mktgo.pojo.Province;

@Service
public class PageInfoService {

    @Autowired
    private ProvinceMapper  provinceMapper;

    @Autowired
    private BrandMapper     brandMapper;

    @Autowired
    private ModelRankMapper modelMapper;
    @Autowired
    private CountryMapper   countryMapper;
    @Autowired
    private DateMonthMapper dateMonthMapper;

    public List<Arrays> getAllInfo() {

        List pageInfoList = new ArrayList<>();
        Object[] provinceArray = provinceMapper.selectAll().toArray();
        pageInfoList.add(provinceArray);

        // 获取brand map
        List<Brand> brandList = brandMapper.queryBrandsByRank();
        Map[] brandMapArray = new Map[brandList.size()];
        int index = 0;
        for (Brand brand : brandList) {
            Map brandName = new HashMap();
            brandName.put("key", brand.getBrand_name());
            brandName.put(brand.getBrand(), modelMapper.queryModelsByBrand(brand.getBrand()));
            brandName.put("queryKey", brand.getBrand());
            brandMapArray[index] = brandName;
            index++;
        }
        pageInfoList.add(brandMapArray);
        // 国家
        Object[] countryArray = countryMapper.selectAll().toArray();
        pageInfoList.add(countryArray);
        return pageInfoList;
    }

    public List<Country> getCountry() {

        return countryMapper.selectAll();
    }

    public List<Province> getProvince() {

        return provinceMapper.selectAll();
    }

    public Map<String, String> getProvinceMap() {
        List<Province> provinces = provinceMapper.selectAll();
        return provinces.stream().collect(Collectors.toMap(Province::getProvince, Province::getCh_name));
    }

    public List<Brand> getBrandByRank() {

        return brandMapper.queryBrandsByRank();
    }

    public List<String> getModelByBrank(String brand) {

        return modelMapper.queryModelsByBrand(brand);
    }

    public List<String> getMonth() {
        return dateMonthMapper.queryAllMonth();
    }
}
