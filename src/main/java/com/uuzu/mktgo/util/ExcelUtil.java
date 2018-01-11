package com.uuzu.mktgo.util;

import com.uuzu.mktgo.pojo.BaseModel;
import com.uuzu.mktgo.pojo.BaseModelByPercent;
import com.uuzu.mktgo.pojo.MediaAnalysisModel;
import com.uuzu.mktgo.pojo.OverviewBaseModel;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author zhoujin
 */
public class ExcelUtil {

    /**
     * excel通过坐标写值
     *
     * @param sheet
     * @param newContent
     * @param beginRow
     * @param beginCell
     */
    public static void writeValueByPoint(HSSFSheet sheet, String newContent, int beginRow, int beginCell) {
        HSSFRow row = sheet.getRow(beginRow);
        if (null == row) {
            row = sheet.createRow(beginRow);
        }
        HSSFCell cell = row.getCell(beginCell);
        if (null == cell) {
            cell = row.createCell(beginCell);
        }
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(newContent);
    }


    /**
     * excel通过坐标写值  格式化
     *
     * @param sheet
     * @param newContent
     * @param beginRow
     * @param beginCell
     */
    public static void writeFormatValueByPoint(HSSFSheet sheet, String newContent, int beginRow, int beginCell) {
        DecimalFormat df = new DecimalFormat("0.00%");
        HSSFRow row = sheet.getRow(beginRow);
        if (null == row) {
            row = sheet.createRow(beginRow);
        }
        HSSFCell cell = row.getCell(beginCell);
        if (null == cell) {
            cell = row.createCell(beginCell);
        }
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(df.format(Double.parseDouble(newContent)));
    }

    /**
     * excel通过坐标写入list值
     *
     * @param wb
     * @param sheetNo
     * @param baseModels
     * @param beginRow
     * @param beginCell
     */
    public static void writeValuesByPoint1(HSSFWorkbook wb, int sheetNo, List<BaseModel> baseModels, int beginRow, int beginCell) {
        HSSFSheet sheet = wb.getSheetAt(sheetNo);
        int x = beginRow;
        for (BaseModel baseModel : baseModels) {
            String key = baseModel.getKey();
            String value = baseModel.getValue() + "";
            if ("-1".equals(key) || "unknown".equals(key) || "other".equals(key)) continue;
            writeValueByPoint(sheet, key, x, beginCell);
            writeValueByPoint(sheet, value, x, beginCell + 1);
            x++;
        }
    }


    /**
     * excel通过坐标写入list值
     *
     * @param wb
     * @param sheetNo
     * @param baseModels
     * @param beginRow
     * @param beginCell
     */
    public static void writeValuesByPoint(HSSFWorkbook wb, int sheetNo, List<BaseModel> baseModels, int beginRow, int beginCell, boolean formatFlag) {
        HSSFSheet sheet = wb.getSheetAt(sheetNo);
        if (formatFlag) {
            int x = beginRow;
            DecimalFormat df = new DecimalFormat("0.00%");
            for (BaseModel baseModel : baseModels) {
                String key = baseModel.getKey();
                String value = baseModel.getValue() + "";
                writeValueByPoint(sheet, key, x, beginCell);
                writeValueByPoint(sheet, df.format(Double.parseDouble(value)), x, beginCell + 1);
                x++;
            }
        } else {
            int x = beginRow;
            for (BaseModel baseModel : baseModels) {
                String key = baseModel.getKey();
                String value = baseModel.getValue() + "";
                writeValueByPoint(sheet, key, x, beginCell);
                writeValueByPoint(sheet, value, x, beginCell + 1);
                x++;
            }
        }

    }

    /**
     * excel通过坐标写入list值
     *
     * @param wb
     * @param sheetNo
     * @param baseModels
     * @param beginRow
     * @param beginCell
     */
    public static void writeOverviewBaseModelValuesByPoint(HSSFWorkbook wb, int sheetNo, List<OverviewBaseModel> baseModels, int beginRow, int beginCell) {
        HSSFSheet sheet = wb.getSheetAt(sheetNo);
        int x = beginRow;
        DecimalFormat df = new DecimalFormat("0.00%");
        for (OverviewBaseModel baseModel : baseModels) {
            String key = baseModel.getDate();
            String value = baseModel.getValue() + "";
            writeValueByPoint(sheet, key, x, beginCell);
            writeValueByPoint(sheet, df.format(Double.parseDouble(value)), x, beginCell + 1);
            x++;
        }
    }


    /**
     * excel通过坐标写入list值
     *
     * @param wb
     * @param sheetNo
     * @param mediaAnalysisModels
     * @param beginRow
     * @param beginCell
     */
    public static void writeTop60App(HSSFWorkbook wb, int sheetNo, List<MediaAnalysisModel> mediaAnalysisModels, int beginRow, int beginCell) {
        HSSFSheet sheet = wb.getSheetAt(sheetNo);
        int x = beginRow;
        for (MediaAnalysisModel mediaAnalysisModel : mediaAnalysisModels) {
            writeValueByPoint(sheet, mediaAnalysisModel.getName(), x, beginCell);
            writeValueByPoint(sheet, mediaAnalysisModel.getCate_name(), x, beginCell + 1);
            writeValueByPoint(sheet, mediaAnalysisModel.getActive_rate() + "", x, beginCell + 2);
            writeValueByPoint(sheet, mediaAnalysisModel.getIndex() + "", x, beginCell + 3);

            x++;
        }
    }


}
