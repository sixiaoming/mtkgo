package com.uuzu.mktgo.web;

import com.uuzu.mktgo.service.DictInfoService;
import com.uuzu.mktgo.service.PageInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Api(description = "字典信息")
@RestController
@Slf4j
@RequestMapping("dict")
public class DictInfoControl {
    @Autowired
    DictInfoService dictInfoService;

    @ApiOperation(value = "字典信息" , notes = "")
    @GetMapping("/info")
    @ResponseBody
    public ResponseEntity<Map<String,Map>> getDictInfo(){
        try {
            return ResponseEntity.ok (dictInfoService.getDictInfo());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
