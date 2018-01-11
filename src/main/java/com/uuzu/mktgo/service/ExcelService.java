package com.uuzu.mktgo.service;

import com.uuzu.mktgo.pojo.*;
import com.uuzu.mktgo.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhoujin
 */
@Service
@Slf4j
public class ExcelService {

    @Autowired
    private ChangePhonePeriodService changePhonePeriodService;

    @Autowired
    ChangePhoneTrendService changePhoneTrendService;

    @Autowired
    RegionDistributionService regionDistributionService;

    @Autowired
    MediaAnalysisService mediaAnalysisService;

    @Autowired
    StandardPortraitService standardPortraitService;

    @Autowired
    OverviewBrandService overviewBrandService;

    @Autowired
    OverviewModelService overviewModelService;

    @Autowired
    FansPortraitService fansPortraitService;

    @Autowired
    OverviewAllService overviewAllService;

    /**
     * @param wb
     * @throws Exception
     */
    public void overViewAllReport(HSSFWorkbook wb) {
        try {
            OverviewModel overviewModel = overviewAllService.overviewAll("", "", "", "");
            ExcelUtil.writeValuesByPoint(wb, 0, overviewModel.getHolding_rank(), 2, 0, true);
            ExcelUtil.writeValuesByPoint(wb, 0, overviewModel.getHot_sell_price_rank(), 2, 3, false);
            ExcelUtil.writeValuesByPoint(wb, 0, overviewModel.getHot_selling_rank(), 15, 0, true);
            ExcelUtil.writeValuesByPoint(wb, 0, overviewModel.getOs_rank(), 15, 3, true);
            ExcelUtil.writeValuesByPoint(wb, 0, overviewModel.getPx_rank(), 28, 0, true);

        } catch (Exception e) {
            log.error("概览画像excel导出错误", e);
        }
    }

    /**
     * @param wb
     * @param brand
     * @param model
     * @throws Exception
     */
    public void overViewReport(HSSFWorkbook wb, String brand, String model) {
        try {
            if (StringUtils.isEmpty(model)) {
                OverviewModel overviewModel = overviewBrandService.overviewBrand(brand, "", "", "", "");
                wb.setSheetName(2, brand + "-品牌概览");
                ExcelUtil.writeValueByPoint(wb.getSheetAt(2), overviewModel.getBrand(), 0, 1);
                ExcelUtil.writeValueByPoint(wb.getSheetAt(2), overviewModel.getHolding() + "", 1, 1);
                ExcelUtil.writeFormatValueByPoint(wb.getSheetAt(2), overviewModel.getMarket_share_rank() + "", 2, 1);

                ExcelUtil.writeOverviewBaseModelValuesByPoint(wb, 2, overviewModel.getMarket_share_brand().get(0).getData(), 6, 0);
                ExcelUtil.writeValuesByPoint(wb, 2, overviewModel.getHot_selling_rank(), 15, 0, true);
                ExcelUtil.writeValuesByPoint(wb, 2, overviewModel.getPx_rank(), 15, 3, true);
                ExcelUtil.writeValuesByPoint(wb, 2, overviewModel.getOs_rank(), 15, 6, true);
            } else {
                ModelOverviewModel overviewModel = overviewModelService.overview_model(model, "", "", "", "");
                wb.setSheetName(1, model + "-机型概览");
                ExcelUtil.writeValueByPoint(wb.getSheetAt(1), overviewModel.getModel(), 0, 1);
                ExcelUtil.writeValueByPoint(wb.getSheetAt(1), overviewModel.getPrice(), 1, 1);
                ExcelUtil.writeValueByPoint(wb.getSheetAt(1), overviewModel.getUptime(), 2, 1);
                ExcelUtil.writeOverviewBaseModelValuesByPoint(wb, 1, overviewModel.getMarket_share_model().get(0).getData(), 6, 0);
                ExcelUtil.writeValuesByPoint(wb, 1, overviewModel.getGenders(), 15, 0, true);
                ExcelUtil.writeValuesByPoint(wb, 1, overviewModel.getAgeBins(), 20, 0, true);
            }
        } catch (Exception e) {
            log.error("概览画像excel导出错误", e);
        }
    }

    /**
     * @param wb
     * @param brand
     * @param model
     * @throws Exception
     */
    public void standardPortraitReport(HSSFWorkbook wb, String brand, String model) {
        try {
            StandardPortraitModel standardPortraitModel = standardPortraitService.standardPortrait(brand, model, "", "", "");
            ExcelUtil.writeValuesByPoint(wb, 3, standardPortraitModel.getGender(), 2, 0, true);
            ExcelUtil.writeValuesByPoint(wb, 3, standardPortraitModel.getAgebin(), 7, 0, true);
            ExcelUtil.writeValuesByPoint(wb, 3, standardPortraitModel.getIncome(), 15, 0, true);
            ExcelUtil.writeValuesByPoint(wb, 3, standardPortraitModel.getOccupation(), 21, 0, true);
            ExcelUtil.writeValuesByPoint(wb, 3, standardPortraitModel.getEdu(), 2, 3, true);
            ExcelUtil.writeValuesByPoint(wb, 3, standardPortraitModel.getKids(), 8, 3, true);
            ExcelUtil.writeValuesByPoint(wb, 3, standardPortraitModel.getNetwork(), 13, 3, true);
            ExcelUtil.writeValuesByPoint(wb, 3, standardPortraitModel.getCarrier(), 21, 3, true);
            ExcelUtil.writeValuesByPoint(wb, 3, standardPortraitModel.getSegment(), 2, 6, true);
        } catch (Exception e) {
            log.error("标准画像excel导出错误", e);
        }
    }

    /**
     * @param wb
     * @param brand
     * @param model
     */
    public void fansPortraitReport(HSSFWorkbook wb, String brand, String model) {
        try {
            FansPortraitModel fansPortraitModel = fansPortraitService.fansPortrait(brand, model, "", "", "");
            ExcelUtil.writeValuesByPoint(wb, 4, fansPortraitModel.getGender(), 2, 0, true);
            ExcelUtil.writeValuesByPoint(wb, 4, fansPortraitModel.getAgebin(), 7, 0, true);
            ExcelUtil.writeValuesByPoint(wb, 4, fansPortraitModel.getIncome(), 15, 0, true);
            ExcelUtil.writeValuesByPoint(wb, 4, fansPortraitModel.getOccupation(), 22, 0, true);
            ExcelUtil.writeValuesByPoint(wb, 4, fansPortraitModel.getEdu(), 2, 3, true);
            ExcelUtil.writeValuesByPoint(wb, 4, fansPortraitModel.getKids(), 8, 3, true);
            ExcelUtil.writeValuesByPoint(wb, 4, fansPortraitModel.getNetwork_new(), 13, 3, true);
            ExcelUtil.writeValuesByPoint(wb, 4, fansPortraitModel.getCarrier_new(), 21, 3, true);
            ExcelUtil.writeValuesByPoint(wb, 4, fansPortraitModel.getSegment(), 2, 6, true);
        } catch (Exception e) {
            log.error("粉丝画像excel导出错误", e);
        }

    }


    /**
     * @param wb
     * @param brand
     * @param model
     */
    public void changePhoneTrendReport(HSSFWorkbook wb, String brand, String model) {
        try {
            ChangePhoneTrendModel changePhoneTrendModel = changePhoneTrendService.changePhoneTrend(brand, model, "", "", "");
            ExcelUtil.writeValuesByPoint(wb, 5, changePhoneTrendModel.getSource().getGender(), 14, 0, true);
            ExcelUtil.writeValuesByPoint(wb, 5, changePhoneTrendModel.getSource().getAgebin(), 19, 0, true);
            ExcelUtil.writeValuesByPoint(wb, 5, changePhoneTrendModel.getSource().getIncome(), 27, 0, true);
            ExcelUtil.writeValuesByPoint(wb, 5, changePhoneTrendModel.getSource().getOccupation(), 34, 0, true);
            ExcelUtil.writeValuesByPoint(wb, 5, changePhoneTrendModel.getSource().getEdu(), 14, 3, true);
            ExcelUtil.writeValuesByPoint(wb, 5, changePhoneTrendModel.getSource().getKids(), 20, 3, true);
            ExcelUtil.writeValuesByPoint(wb, 5, changePhoneTrendModel.getSource().getNetwork(), 25, 3, true);
            ExcelUtil.writeValuesByPoint(wb, 5, changePhoneTrendModel.getSource().getCarrier(), 33, 3, true);
            ExcelUtil.writeValuesByPoint(wb, 5, changePhoneTrendModel.getSource().getSegment(), 14, 6, true);
            ExcelUtil.writeValuesByPoint(wb, 5, changePhoneTrendModel.getTrend().getGender(), 14, 10, true);
            ExcelUtil.writeValuesByPoint(wb, 5, changePhoneTrendModel.getTrend().getAgebin(), 19, 10, true);
            ExcelUtil.writeValuesByPoint(wb, 5, changePhoneTrendModel.getTrend().getIncome(), 27, 10, true);
            ExcelUtil.writeValuesByPoint(wb, 5, changePhoneTrendModel.getTrend().getOccupation(), 34, 10, true);
            ExcelUtil.writeValuesByPoint(wb, 5, changePhoneTrendModel.getTrend().getEdu(), 14, 13, true);
            ExcelUtil.writeValuesByPoint(wb, 5, changePhoneTrendModel.getTrend().getKids(), 20, 13, true);
            ExcelUtil.writeValuesByPoint(wb, 5, changePhoneTrendModel.getTrend().getNetwork(), 25, 13, true);
            ExcelUtil.writeValuesByPoint(wb, 5, changePhoneTrendModel.getTrend().getCarrier(), 33, 13, true);
            ExcelUtil.writeValuesByPoint(wb, 5, changePhoneTrendModel.getTrend().getSegment(), 14, 16, true);
            ExcelUtil.writeValuesByPoint(wb, 5, changePhoneTrendModel.getSource().getSourceModel(), 1, 0, true);
            ExcelUtil.writeValuesByPoint(wb, 5, changePhoneTrendModel.getTrend().getTrendModel(), 1, 10, true);
        } catch (Exception e) {
            log.error("换机流动excel导出错误", e);
        }
    }

    /**
     * @param wb
     * @param brand
     * @param model
     */
    public void regionDistributionReport(HSSFWorkbook wb, String brand, String model) {
        try {
            RegionDistributionModel regionDistributionModel = regionDistributionService.regionDistribution(brand, model, "", "", "", "");
            ExcelUtil.writeValuesByPoint(wb, 6, regionDistributionModel.getProvinceMarketShare().subList(0, 10), 2, 0, true);
            ExcelUtil.writeValuesByPoint(wb, 6, regionDistributionModel.getProvince().subList(0, 10), 15, 0, true);
        } catch (Exception e) {
            log.error("地区分布excel导出错误", e);
        }
    }

    /**
     * @param wb
     * @param brand
     * @param model
     */
    public void changePhonePeriodReport(HSSFWorkbook wb, String brand, String model) {
        try {
            ChangePhonePeriodModel changePhonePeriodModel = changePhonePeriodService.changePhonePeriod(brand, model, "", "", "");
            ExcelUtil.writeValuesByPoint1(wb, 7, changePhonePeriodModel.getPeriod(), 1, 0);
        } catch (Exception e) {
            log.error("换机周期excel导出错误", e);
        }
    }

    /**
     * @param wb
     * @param brand
     * @param model
     */
    public void mediaAnalysisReport(HSSFWorkbook wb, String brand, String model) {
        try {
            List<MediaAnalysisModel> mediaAnalysisModels = mediaAnalysisService.MediaAnalysis(brand, model, "", "", "", "");
            ExcelUtil.writeTop60App(wb, 8, mediaAnalysisModels.subList(0, 60), 1, 1);
        } catch (Exception e) {
            log.error("媒介分析excel导出错误", e);
        }
    }

}
