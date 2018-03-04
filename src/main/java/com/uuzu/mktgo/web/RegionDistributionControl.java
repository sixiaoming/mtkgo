package com.uuzu.mktgo.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.uuzu.mktgo.pojo.RegionDistributionModel;
import com.uuzu.mktgo.service.RegionDistributionService;

@Api(description = "地区分布信息")
@RestController
@Slf4j
@RequestMapping("mktgo")
public class RegionDistributionControl {

    @Autowired
    RegionDistributionService regionDistributionService;

    @ApiOperation(value = "地区分布信息", notes = "地区分布信息")
    @PostMapping("/regionDistribution")
    @ResponseBody
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "form", name = "brand", dataType = "String", required = false, value = "品牌"),
            @ApiImplicitParam(paramType = "form", name = "model", dataType = "String", required = false, value = "机型"),
            @ApiImplicitParam(paramType = "form", name = "price", dataType = "String", required = false, value = "价位"),
            @ApiImplicitParam(paramType = "form", name = "country", dataType = "String", required = false, value = "国家"),
            @ApiImplicitParam(paramType = "form", name = "date", dataType = "String", required = false, value = "时间"),
            @ApiImplicitParam(paramType = "form", name = "province", dataType = "String", required = false, value = "省份") })
    public ResponseEntity<RegionDistributionModel> getBrandAndProvince(@RequestParam(value = "brand", required = false) String brand, //
                                                                       @RequestParam(value = "model", required = false) String model,//
                                                                       @RequestParam(value = "price", required = false) String price, //
                                                                       @RequestParam(value = "country", required = false) String country,//
                                                                       @RequestParam(value = "province", required = false) String province, //
                                                                       @RequestParam(value = "date", required = false) String date) throws Exception {
        try {
            return ResponseEntity.ok(regionDistributionService.regionDistribution(brand, model, price, country, date, province));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new RegionDistributionModel());
        }
    }
}
