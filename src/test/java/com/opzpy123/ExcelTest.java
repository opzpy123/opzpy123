package com.opzpy123;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

import java.util.List;
import java.util.Map;

public class ExcelTest {
    public static void main(String[] args) {
        ExcelReader reader = ExcelUtil.getReader("/Users/opzpy/Desktop/六万行早期BOM.xlsx");
        ExcelWriter writer = ExcelUtil.getWriter("/Users/opzpy/Desktop/六万行早期BOMcopy.xlsx");
        List<Map<String, Object>> maps = reader.readAll();
        for (Map<String, Object> map : maps) {
            if (map.containsKey("规则描述") && map.get("规则描述") != null
                    && StrUtil.isNotBlank((String) map.get("规则描述"))
                    && ((String) map.get("规则描述")).length() > 255) {
                map.put("规则描述", ((String) map.get("规则描述")).substring(0, 254));
            }
        }
        writer.setSheet(0);
        for (Map<String, Object> map : maps) {
            writer.writeRow(map, false);
        }
        writer.flush();
    }
}
