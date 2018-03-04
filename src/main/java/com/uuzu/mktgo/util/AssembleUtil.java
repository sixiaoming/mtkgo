package com.uuzu.mktgo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.uuzu.mktgo.pojo.BaseModel;

/**
 * Created by shieh on 2017/10/19.
 */
public class AssembleUtil {

    public static List<BaseModel> subSortedListAndCalculate(List<BaseModel> list, int fromIndex, int toIndex) {
        Collections.sort(list, new Comparator<BaseModel>() {

            @Override
            public int compare(BaseModel o1, BaseModel o2) {
                if (o1.getValue() < o2.getValue()) {
                    return 1;
                } else if (o1.getValue() == o2.getValue()) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
        if (!CollectionUtils.isEmpty(list) && list.size() > (toIndex - fromIndex)) {
            list = list.subList(fromIndex, toIndex);
        }
        // calc sum
        double sum = 0.0;
        for (BaseModel baseModel : list) {
            sum += baseModel.getValue();
        }
        // recalculate value
        for (BaseModel baseModel : list) {
            baseModel.setValue(sum <= 0.0 ? 0.0 : baseModel.getValue() / sum);
        }
        return list;
    }

    public static List<Map<String, Object>> sortAndConvert(List<Map<String, Object>> list, String comparatorParam) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        for (Map<String, Object> map : list) {
            String monthStr = map.get("month").toString();
            try {
                Date date = sdf.parse(monthStr);
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                int month = c.get(Calendar.MONTH) + 1;
                int year = c.get(Calendar.YEAR);
                int quarter = (month + 2) / 3;
                map.put("month", year + "Q" + quarter);
            } catch (ParseException e) {
                continue;
            }
        }
        Set<String> quarterSet = new HashSet<>();
        for (Map<String, Object> map : list) {
            String quarter = map.get("month").toString();
            if (quarterSet.contains(quarter)) continue;
            quarterSet.add(quarter);
        }
        List<String> quarterList = new LinkedList<>(quarterSet);
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (String quarter : quarterList) {
            Map<String, Object> resultMap = new HashMap<>();
            long sumDurationDays = 0;
            long sumChangeTimesCount = 0;
            int num = 0;
            for (Map<String, Object> map : list) {
                String quarterStr = map.get("month").toString();
                long durationDays = Long.parseLong(map.get("duration_days").toString());
                long changeTimesCount = Long.parseLong(map.get("change_times_count").toString());
                if (quarter.equals(quarterStr)) {
                    sumDurationDays += durationDays;
                    sumChangeTimesCount += changeTimesCount;
                    ++num;
                }
            }
            resultMap.put("month", quarter);
            resultMap.put("duration_days", sumDurationDays);
            resultMap.put("change_times_count", sumChangeTimesCount);
            resultMap.put("num", num);
            resultList.add(resultMap);
        }
        Collections.sort(resultList, new Comparator<Map<String, Object>>() {

            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                String s1 = o1.get(comparatorParam).toString();
                String s2 = o2.get(comparatorParam).toString();
                return 0 - s1.compareTo(s2);
            }
        });
        return resultList;
    }

    public static List<String> splitBySymbol(String s, String symbol) {
        if (StringUtils.isEmpty(s)) {
            return null;
        }
        String[] strings = s.split(symbol);
        List<String> result = new ArrayList<>(strings.length);
        for (String s1 : strings) {
            result.add(s1);
        }
        return result;
    }

}
