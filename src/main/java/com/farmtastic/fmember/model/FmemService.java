package com.farmtastic.fmember.model;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mysql.cj.xdevapi.SessionFactory;

@Service("fmemService")
public class FmemService {

	FmemRepository repository;
	
	private SessionFactory sessionFactory;
	
	public void addFmem(Fmem fmem) {
		repository.save(fmem);
	}
	public void updateFmem(Fmem fmem) {
		repository.save(fmem);
	}
	
	public List<Fmem> getAll(){
		return repository.findAll();
	}
}
