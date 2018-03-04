package com.uuzu.mktgo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.uuzu.mktgo.mapper.DictMapper;
import com.uuzu.mktgo.mapper.ProvinceMapper;
import com.uuzu.mktgo.pojo.DictModel;
import com.uuzu.mktgo.pojo.Province;

@Service
public class DictInfoService {

    @Autowired
    private DictMapper     dictMapper;

    @Autowired
    private ProvinceMapper provinceMapper;

    public Map getDictInfo() {

        List<String> nameList = dictMapper.queryNameInfo();
        Map<String, Map> dict = new HashMap();
        for (String name : nameList) {
            List<DictModel> KV = dictMapper.queryKVInfo(name);
            Map singleName = new HashMap();
            for (DictModel bm : KV) {
                singleName.put(bm.getPer_key(), bm.getPer_value());
            }
            dict.put(name, singleName);
        }
        return dict;
    }

    public Map<String, String> getProvinceInfo() {
        List<Province> list = provinceMapper.queryInfo();
        Map<String, String> dict = new HashMap();
        if (CollectionUtils.isEmpty(list)) return dict;

        for (Province province : list) {
            dict.put(province.getProvince(), province.getCh_name());
        }
        return dict;
    }
}
