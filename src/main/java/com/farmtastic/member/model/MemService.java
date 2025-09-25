package com.farmtastic.member.model;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("memService")
public class MemService {
	
	@Autowired
	MemRepository repository;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public void addMem(Mem mem) {
		repository.save(mem);
	}
	
	public void updateMem(Mem mem) {
		repository.save(mem);
	}
	
//	public void deleteMem(Integer memId) {
//		dao.delete(memId);
//	}
//	
//	public Mem getOneMem(Integer memId){
//		return dao.findByMemId(memId);
//	}
//	public List<Mem> getMems(Byte accStatus){
//		return dao.getMemsByAccStatus(accStatus);
//	}
	
	public List<Mem> getAll(){
		return repository.findAll();
	}

}
