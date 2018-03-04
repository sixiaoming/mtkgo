package com.uuzu.mktgo.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.Arrays;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.uuzu.mktgo.pojo.Brand;
import com.uuzu.mktgo.pojo.Country;
import com.uuzu.mktgo.pojo.Province;
import com.uuzu.mktgo.service.PageInfoService;

@Api(description = "页面初始化信息")
@RestController
@Slf4j
@RequestMapping("page")
public class PageInfoControl {

    @Autowired
    PageInfoService pageInfoService;

    @ApiOperation(value = "页面初始化所需信息", notes = "")
    @GetMapping("/info")
    @ResponseBody
    public ResponseEntity<List<Arrays>> getAllInfo() {
        try {
            return ResponseEntity.ok(pageInfoService.getAllInfo());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 要将信息拆分开来
    // 1国家信息
    @ApiOperation(value = "页面初始化国家信息", notes = "")
    @GetMapping("/country")
    @ResponseBody
    public ResponseEntity<List<Country>> getCountry() {
        try {
            return ResponseEntity.ok(pageInfoService.getCountry());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 要将信息拆分开来
    // 2城市信息
    @ApiOperation(value = "页面初始化城市信息", notes = "")
    @GetMapping("/province")
    @ResponseBody
    public ResponseEntity<List<Province>> getProvince() {
        try {
            return ResponseEntity.ok(pageInfoService.getProvince());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 要将信息拆分开来
    // 3 brand信息
    @ApiOperation(value = "页面初始化brand信息", notes = "")
    @GetMapping("/brand")
    @ResponseBody
    public ResponseEntity<List<Brand>> getBrandByRank() {
        try {
            return ResponseEntity.ok(pageInfoService.getBrandByRank());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 4 model信息
    @ApiOperation(value = "页面初始化model信息", notes = "")
    @GetMapping("/model")
    @ResponseBody
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "form", name = "brand", dataType = "String", required = true, value = "品牌") })
    public ResponseEntity<List<String>> getModelByBrand(@RequestParam(value = "brand", required = true) String brand) {
        try {
            return ResponseEntity.ok(pageInfoService.getModelByBrank(brand));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 5 month信息
    @ApiOperation(value = "页面初始化month信息", notes = "")
    @GetMapping("/month")
    @ResponseBody
    public ResponseEntity<List<String>> getMonth() {
        try {
            return ResponseEntity.ok(pageInfoService.getMonth());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
