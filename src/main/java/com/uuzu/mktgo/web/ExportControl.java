package com.uuzu.mktgo.web;

import com.uuzu.mktgo.service.ExcelService;
import com.uuzu.mktgo.util.POIUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("mktgo")
@Api(description = "mktgo接口——导出功能")
public class ExportControl {

    @Autowired
    ExcelService excelService;

    @GetMapping("/exportAll")
    @ApiOperation(value = "导出功能", notes = "导出功能接口")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", name = "brand", dataType = "String", required = false, value = "品牌"),
            @ApiImplicitParam(paramType = "form", name = "model", dataType = "String", required = false, value = "机型"),
            @ApiImplicitParam(paramType = "form", name = "reports", dataType = "String", required = true, value = "导出维度 :1品牌概览，2标准画像，3粉丝画像，4换机流动，5地区分布，6换机周期，7媒介分析")
    })
    public void exportAll(@RequestParam(value = "brand", required = false) String brand,
                       @RequestParam(value = "model", required = false) String model,
                       @RequestParam(value = "reports", required = true) String reports,
                       HttpServletResponse response) {

        OutputStream out = null;
        try {
            out = response.getOutputStream();
            String fileName = "MKTGO-导出报告.xls";// 文件名
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));

            PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = patternResolver.getResources("classpath:excel-templates/MKTGO-导出报告1.xls");

            if (resources != null && resources.length > 0) {

                HSSFWorkbook wb = new HSSFWorkbook(resources[0].getInputStream());

                //HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(ResourceUtils.getFile("classpath:excel-templates/MKTGO-导出报告.xls")));
                //HSSFWorkbook wb = new HSSFWorkbook(getExcelInputStream("MKTGO-导出报告.xls"));

                //如果是excel导出
                String[] reportss = reports.split(",");
                HSSFWorkbook wb1 = null, wb2 = null, wb3 = null, wb4 = null, wb5 = null, wb6 = null, wb7 = null, wb8 = null;
                for (String report : reportss) {
                    if ("1".equals(report)) {
                        Resource[] resources1;
                        if(StringUtils.isEmpty(model)){
                            //品牌概览
                            resources1 = patternResolver.getResources("classpath:excel-templates/品牌概览.xls");
                        }else{
                            resources1 = patternResolver.getResources("classpath:excel-templates/机型概览.xls");
                        }
                        wb1 = new HSSFWorkbook(resources1[0].getInputStream());
                        excelService.overViewReport(wb1, brand, model);

                        Resource[]  resources8 = patternResolver.getResources("classpath:excel-templates/全部概览.xls");
                        wb8 = new HSSFWorkbook(resources8[0].getInputStream());
                        excelService.overViewAllReport(wb8);

                    } else if ("2".equals(report)) {
                        //标准画像
                        Resource[] resources2 = patternResolver.getResources("classpath:excel-templates/标准画像.xls");
                        wb2 = new HSSFWorkbook(resources2[0].getInputStream());
                        excelService.standardPortraitReport(wb2, brand, model);
                    } else if ("3".equals(report)) {
                        Resource[] resources3 = patternResolver.getResources("classpath:excel-templates/粉丝画像.xls");
                        wb3 = new HSSFWorkbook(resources3[0].getInputStream());
                        //粉丝画像
                        excelService.fansPortraitReport(wb3, brand, model);
                    } else if ("4".equals(report)) {
                        Resource[] resources4 = patternResolver.getResources("classpath:excel-templates/换机流动.xls");
                        wb4 = new HSSFWorkbook(resources4[0].getInputStream());
                        //换机流动
                        excelService.changePhoneTrendReport(wb4, brand, model);
                    } else if ("5".equals(report)) {
                        Resource[] resources5 = patternResolver.getResources("classpath:excel-templates/地区分布.xls");
                        wb5 = new HSSFWorkbook(resources5[0].getInputStream());
                        //地区分布
                        excelService.regionDistributionReport(wb5, brand, model);
                    } else if ("6".equals(report)) {
                        Resource[] resources6 = patternResolver.getResources("classpath:excel-templates/换机周期.xls");
                        wb6 = new HSSFWorkbook(resources6[0].getInputStream());
                        //换机周期
                        excelService.changePhonePeriodReport(wb6, brand, model);
                    } else if ("7".equals(report)) {
                        Resource[] resources7 = patternResolver.getResources("classpath:excel-templates/媒介分析.xls");
                        wb7 = new HSSFWorkbook(resources7[0].getInputStream());
                        //媒介分析
                        excelService.mediaAnalysisReport(wb7, brand, model);
                    }
                }

                int i = 0;
                if (wb1 != null) {
                    POIUtil.copySheet(wb1, wb, 0, i++);
                }
                if (wb2 != null) {
                    POIUtil.copySheet(wb2, wb, 0, i++);
                }
                if (wb3 != null) {
                    POIUtil.copySheet(wb3, wb, 0, i++);
                }

                if (wb5 != null) {
                    POIUtil.copySheet(wb5, wb, 0, i++);
                }
                if (wb6 != null) {
                    POIUtil.copySheet(wb6, wb, 0, i++);
                }
                if (wb7 != null) {
                    POIUtil.copySheet(wb7, wb, 0, i++);
                }
                if (wb8 != null) {
                    POIUtil.copySheet(wb8, wb, 0, i++);
                }
                if (wb4 != null) {
                    POIUtil.copySheet(wb4, wb, 0, i++);
                }

                wb.write(out);
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }

    @GetMapping("/export")
    @ApiOperation(value = "导出功能", notes = "导出功能接口")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", name = "brand", dataType = "String", required = false, value = "品牌"),
            @ApiImplicitParam(paramType = "form", name = "model", dataType = "String", required = false, value = "机型"),
            @ApiImplicitParam(paramType = "form", name = "reports", dataType = "String", required = true, value = "导出维度 :1品牌概览，2标准画像，3粉丝画像，4换机流动，5地区分布，6换机周期，7媒介分析，8全部概览")
    })
    public void export(@RequestParam(value = "brand", required = false) String brand,
                       @RequestParam(value = "model", required = false) String model,
                       @RequestParam(value = "reports", required = true) String reports,
                       HttpServletResponse response) {

        OutputStream out = null;
        try {
            out = response.getOutputStream();
            String fileName = "MKTGO-导出报告.xls";// 文件名
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));

            PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = patternResolver.getResources("classpath:excel-templates/MKTGO-导出报告.xls");

            if (resources != null && resources.length > 0) {

                HSSFWorkbook wb = new HSSFWorkbook(resources[0].getInputStream());

                //如果是excel导出
                String[] reportss = reports.split(",");
                for (String report : reportss) {
                    if ("1".equals(report)) {
                        //品牌概览
                        excelService.overViewReport(wb, brand, model);
                    } else if ("2".equals(report)) {
                        //标准画像
                        excelService.standardPortraitReport(wb, brand, model);
                    } else if ("3".equals(report)) {
                        //粉丝画像
                        excelService.fansPortraitReport(wb, brand, model);
                    } else if ("4".equals(report)) {
                        //换机流动
                        excelService.changePhoneTrendReport(wb, brand, model);
                    } else if ("5".equals(report)) {
                        //地区分布
                        excelService.regionDistributionReport(wb, brand, model);
                    } else if ("6".equals(report)) {
                        //换机周期
                        excelService.changePhonePeriodReport(wb, brand, model);
                    } else if ("7".equals(report)) {
                        //媒介分析
                        excelService.mediaAnalysisReport(wb, brand, model);
                    } else if("8".equals(report)){
                        //全部概览
                        excelService.overViewAllReport(wb);
                    }
                }
                wb.write(out);
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }


}
