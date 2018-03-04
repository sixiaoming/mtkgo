/*
 * Copyright 2015-2020 mob.com All right reserved.
 */
package com.uuzu.mktgo.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uuzu.mktgo.pojo.StandardPortraitModel;
import com.uuzu.mktgo.service.StandardPortraitService;

/**
 * @author zj_pc
 */
@Slf4j
@RequestMapping("mktgo")
@RestController
@Api(description = "mktgo接口")
public class StandardPortraitControl {

    @Autowired
    StandardPortraitService standardPortraitService;

    @PostMapping("/standardPortrait")
    @ApiOperation(value = "标准画像接口", notes = "标准画像接口")
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "form", name = "brand", dataType = "String", required = false, value = "品牌"),
            @ApiImplicitParam(paramType = "form", name = "model", dataType = "String", required = false, value = "机型"),
            @ApiImplicitParam(paramType = "form", name = "price", dataType = "String", required = false, value = "价位"),
            @ApiImplicitParam(paramType = "form", name = "country", dataType = "String", required = false, value = "国家"),
            @ApiImplicitParam(paramType = "form", name = "province", dataType = "String", required = false, value = "省份") })
    public ResponseEntity<StandardPortraitModel> standardPortrait(@RequestParam(value = "brand", required = false) String brand, //
                                                                  @RequestParam(value = "model", required = false) String model,//
                                                                  @RequestParam(value = "price", required = false) String price, //
                                                                  @RequestParam(value = "country", required = false) String country,//
                                                                  @RequestParam(value = "province", required = false) String province) throws Exception {
        try {
            StandardPortraitModel result = standardPortraitService.standardPortrait(brand, model, price, country, province);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.ok(new StandardPortraitModel());
        }
    }
}
