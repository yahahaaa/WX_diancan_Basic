package com.atzjhydx.weixindiancan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan(basePackages = "com.atzjhydx.weixindiancan.dataobject.mapper")
@EnableCaching
public class WeixindiancanApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeixindiancanApplication.class, args);
    }

}
