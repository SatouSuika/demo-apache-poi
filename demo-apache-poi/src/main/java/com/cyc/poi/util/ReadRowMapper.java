package com.cyc.poi.util;

import org.apache.poi.ss.usermodel.Row;

import java.util.Map;

public interface ReadRowMapper<T> {
    /**.
     * 方法：在导入excel时，用于自定义处理excel中的每行数据,调用者实现
     * @param row excel中的行数据
     */
    T rowMap(Row row, Map<String, Object> map);
    //实现类示例
    //public School rowMap(Row row, Map<String, Object> map) {
    //                    School school = new School();
    //                    school.setSchoolNumber(((Double)map.get("学校标号")).intValue());
    //                    school.setSchoolName((String)map.get("校名"));
    //                    school.setProvince((String)map.get("所在省"));
    //                    school.setCity((String)map.get("所在市"));
    //                    return school;
    //                }
}
