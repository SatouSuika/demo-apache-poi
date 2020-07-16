package com.cyc.poi.controller;

import cn.hutool.json.JSONUtil;
import com.cyc.poi.model.User;
import com.cyc.poi.util.ExcelUtil;
import com.cyc.poi.util.SimpleExcelUtils;
import com.cyc.poi.util.UserWriteRowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/")
    public String index(Model model) {
        return "index";
    }

    @RequestMapping(value = "/importExcel")
    @ResponseBody
    public String importExcel(@RequestParam("file") MultipartFile file) throws IOException {
        System.out.println("使用ExcelUtil");
//        UserReadRowMapper userReadRowMapper = new UserReadRowMapper();
//        List<User> list = ExcelUtil.importExcel(file, userReadRowMapper);
//        list.forEach(System.out::println);
//        System.out.println("错误信息" + userReadRowMapper.errorMsg);
        System.out.println("使用SimpleExcelUtils");
        List<Map<String, String>> maps = SimpleExcelUtils.readExcel(file);
        maps.forEach(System.out::println);
        return JSONUtil.toJsonStr(maps);
    }

    @RequestMapping(value = "/exportExcel")
    @ResponseBody
    public void exportExcel(HttpServletResponse response) throws Exception {
        System.out.println("使用ExcelUtil");
        List<User> list = new ArrayList<>(Arrays.asList(
                User.builder().name("张三").age("10").account("admin").password("123").build(),
                User.builder().name("张三2").age("22").account("admin").password("123").build(),
                User.builder().name("张三3").age("34").account("admin").password("123").build(),
                User.builder().name("张三4").age("23").account("admin").password("123").build()
        ));
        ExcelUtil.exportExcel(response, "用户信息", "sheet", "", UserWriteRowMapper.TITLES, list, new UserWriteRowMapper());
        System.out.println("使用SimpleExcelUtils");
        LinkedHashMap<String, String> titles = new LinkedHashMap<>();
        titles.put("姓名", "name");
        titles.put("年龄", "age");
        titles.put("账号", "account");
        titles.put("密码", "password");
        SimpleExcelUtils.writeExcel(response, list, titles);
    }

}