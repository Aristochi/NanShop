
package com.NanShop.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.NanShop.mall.dao")
@SpringBootApplication
public class NanShopMallApplication {
    public static void main(String[] args) {
        SpringApplication.run(NanShopMallApplication.class, args);
    }
}
