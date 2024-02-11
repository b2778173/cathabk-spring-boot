package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootTest
@MapperScan(basePackages = "com.example.demo.mapper")
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}

}
