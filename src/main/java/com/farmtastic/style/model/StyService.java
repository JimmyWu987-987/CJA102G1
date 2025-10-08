package com.farmtastic.style.model;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class StyService {
	
	StyRepository repository;
	
	public StyService(StyRepository repository) {
		this.repository = repository;
	}
	
	
	public Sty getOneByStyNo(Byte styNo) {
		return repository.findById(styNo).orElse(null);
	}
	
	public List<Sty> getAll(){
		return repository.findAll();
	}
}
