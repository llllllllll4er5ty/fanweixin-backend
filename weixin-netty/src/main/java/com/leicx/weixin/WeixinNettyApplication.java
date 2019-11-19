package com.leicx.weixin;

import com.leicx.weixin.util.SpringUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.leicx.weixin.mapper")
@ComponentScan(basePackages = {"com.leicx.weixin", "org.n3r.idworker"})
public class WeixinNettyApplication {

	@Bean
	public SpringUtil getSpringUtil() {
		return new SpringUtil();
	}

	public static void main(String[] args) {
		SpringApplication.run(WeixinNettyApplication.class, args);
	}

}
