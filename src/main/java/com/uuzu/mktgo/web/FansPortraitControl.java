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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uuzu.mktgo.pojo.FansPortraitModel;
import com.uuzu.mktgo.service.FansPortraitService;

/**
 * Created by shieh on 2017/10/18.
 */
@Slf4j
@RequestMapping("mktgo")
@RestController
@Api(description = "fansPortrait")
public class FansPortraitControl {

    @Autowired
    FansPortraitService fansPortraitService;

    @PostMapping("/fansPortrait")
    @ApiOperation(value = "粉丝画像接口", notes = "粉丝画像接口")
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "form", name = "brand", dataType = "String", required = false, value = "品牌"),
            @ApiImplicitParam(paramType = "form", name = "model", dataType = "String", required = false, value = "机型"),
            @ApiImplicitParam(paramType = "form", name = "price", dataType = "String", required = false, value = "价位"),
            @ApiImplicitParam(paramType = "form", name = "country", dataType = "String", required = false, value = "国家"),
            @ApiImplicitParam(paramType = "form", name = "province", dataType = "String", required = false, value = "省份") })
    public ResponseEntity<FansPortraitModel> fansPortrait(@RequestParam(value = "brand", required = false) String brand, //
                                                          @RequestParam(value = "model", required = false) String model,//
                                                          @RequestParam(value = "price", required = false) String price, //
                                                          @RequestParam(value = "country", required = false) String country,//
                                                          @RequestParam(value = "province", required = false) String province) throws Exception {
        try {
            FansPortraitModel result = fansPortraitService.fansPortrait(brand, null, null, country, province);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new FansPortraitModel());
        }
    }
}
