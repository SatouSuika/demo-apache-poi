package com.cyc.poi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.cyc.poi")
public class DemoApachePoiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApachePoiApplication.class, args);
    }

}
