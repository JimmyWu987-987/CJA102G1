package com.farmtastic.proorder.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;



@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.farmtastic.proorder.model")
public class testProOrder implements CommandLineRunner {

	@Autowired
	ProOrderRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(testProOrder.class);
	}

	@Override
	public void run(String... args) throws Exception {
		
		//新增
//		ProOrderVO proOrderVO = new ProOrderVO();
		
		//修改
		//刪除
		
		
		// 查詢全部
		List<ProOrderVO> list = repository.findAll();
		for (ProOrderVO proOrderVO : list) {
			System.out.println(proOrderVO);
		}

	}

}
