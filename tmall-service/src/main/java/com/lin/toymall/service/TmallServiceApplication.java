package com.lin.toymall.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.lin.toymall.service.mapper")

public class TmallServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TmallServiceApplication.class, args);
    }

}
