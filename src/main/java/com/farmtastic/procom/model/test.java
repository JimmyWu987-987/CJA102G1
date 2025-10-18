package com.farmtastic.procom.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

public class test {
	
	@Autowired
	static
	ProComService proComSvc;
	
	public static void main(String[] args) {
		List<ProComVO> list = proComSvc.getAll();
		System.out.println("查詢到的評論數量是: " + list.size()); // 新增這行！
	}
	
}
