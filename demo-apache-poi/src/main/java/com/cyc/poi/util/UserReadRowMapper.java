package com.cyc.poi.util;

import com.cyc.poi.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lihuasheng
 * @since 2019/12/24 16:43
 */
public class UserReadRowMapper implements ReadRowMapper<User> {
    public Map<Integer, String> errorMsg = new HashMap<>();

    @Override
    public User rowMap(Row row, Map<String, Object> map) {
        User user = new User();
        if (StringUtils.isBlank((String)map.get("密码"))) {
            errorMsg.put(row.getRowNum() + 1, "密码缺失");
        } else {
            user.setPassword((String) map.get("密码"));
        }
        user.setAccount((String) map.get("账号"));
        user.setAge((String) map.get("年龄"));
        user.setName((String) map.get("姓名"));
        return user;
    }
}