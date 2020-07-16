package com.cyc.poi.util;


import com.cyc.poi.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lihuasheng
 * @since 2020/3/20 09:15
 */
public class UserWriteRowMapper implements WriteRowMapper<User> {
    public static final String[] TITLES = {"姓名", "年龄", "账号", "密码"};

    @Override
    public List<String> handleData(User user) {
        List<String> value = new ArrayList<>(TITLES.length);

        value.add(user.getName());
        value.add(user.getAge());
        value.add(user.getAccount());
        value.add(user.getPassword());
        return value;
    }

}