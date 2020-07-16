package com.cyc.poi.util;

import java.util.List;

public interface WriteRowMapper<T> {
    /**
     * .
     * 方法：在导出excel时，用于讲导出的数据组成list，调用者实现
     *
     * @param t 需要导出到excel中的对象
     * @return
     */
    List<String> handleData(T t);
}
