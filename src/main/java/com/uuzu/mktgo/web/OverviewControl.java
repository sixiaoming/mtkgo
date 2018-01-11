package com.uuzu.mktgo.web;

import com.uuzu.mktgo.pojo.ModelOverviewModel;
import com.uuzu.mktgo.pojo.OverviewModel;
import com.uuzu.mktgo.service.OverviewAllService;
import com.uuzu.mktgo.service.OverviewBrandService;
import com.uuzu.mktgo.service.OverviewModelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author zj_pc
 */
@Slf4j
@RequestMapping("mktgo")
@RestController
@Api(description = "mktgo接口——概览")
public class OverviewControl {
    @Autowired
    OverviewModelService overviewModelService;
    @Autowired
    OverviewBrandService overviewBrandService;

    @Autowired
    OverviewAllService overviewAllService;

    @PostMapping("/overview_all")
    @ApiOperation(value = "全部概览接口", notes = "全部概览接口")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", name = "price", dataType = "String", required = false, value = "价位"),
            @ApiImplicitParam(paramType = "form", name = "country", dataType = "String", required = false, value = "国家"),
            @ApiImplicitParam(paramType = "form", name = "province", dataType = "String", required = false, value = "省份"),
            @ApiImplicitParam(paramType = "form", name = "date", dataType = "String", required = false, value = "时间")
    })
    public ResponseEntity<OverviewModel> overview_all(
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "price", required = false) String price,
            @RequestParam(value = "country", required = false) String country,
            @RequestParam(value = "province", required = false) String province) {
        try {
            OverviewModel result = overviewAllService.overviewAll(price, country, province, date);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return ResponseEntity.ok(new OverviewModel());

    }

    @PostMapping("/overview_brand")
    @ApiOperation(value = "品牌概览接口", notes = "品牌概览接口")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", name = "brand", dataType = "String", required = true, value = "品牌"),
            @ApiImplicitParam(paramType = "form", name = "price", dataType = "String", required = false, value = "价位"),
            @ApiImplicitParam(paramType = "form", name = "country", dataType = "String", required = false, value = "国家"),
            @ApiImplicitParam(paramType = "form", name = "province", dataType = "String", required = false, value = "省份"),
            @ApiImplicitParam(paramType = "form", name = "date", dataType = "String", required = false, value = "时间")
    })
    public ResponseEntity<OverviewModel> overview_brand(
            @RequestParam(value = "brand", required = true) String brand,
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "price", required = false) String price,
            @RequestParam(value = "country", required = false) String country,
            @RequestParam(value = "province", required = false) String province) {
        try {
            OverviewModel result = overviewBrandService.overviewBrand(brand, price, country, province, date);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return ResponseEntity.ok(new OverviewModel());
    }

    @PostMapping("/overview_model")
    @ApiOperation(value = "机型概览接口", notes = "机型概览接口")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", name = "model", dataType = "String", required = true, value = "机型"),
            @ApiImplicitParam(paramType = "form", name = "price", dataType = "String", required = false, value = "价位"),
            @ApiImplicitParam(paramType = "form", name = "country", dataType = "String", required = false, value = "国家"),
            @ApiImplicitParam(paramType = "form", name = "province", dataType = "String", required = false, value = "省份"),
            @ApiImplicitParam(paramType = "form", name = "date", dataType = "String", required = false, value = "时间")
    })
    public ResponseEntity<ModelOverviewModel> overview_model(
            @RequestParam(value = "model", required = true) String model,
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "price", required = false) String price,
            @RequestParam(value = "country", required = false) String country,
            @RequestParam(value = "province", required = false) String province) {
        try {
            ModelOverviewModel result = overviewModelService.overview_model(model, price, country, province, date);
            return ResponseEntity.ok(result);
        } catch (Exception e) {

            log.error(e.getMessage(), e);
        }
        return ResponseEntity.ok(new ModelOverviewModel());

    }


}
