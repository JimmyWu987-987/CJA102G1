package com.farmtastic.fmember.model;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("fmemService")
public class FmemService {

	@Autowired
	FmemRepository repository;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public Fmem Login(String fmemAccLogin, String fmemPwdLogin) {
		
		Fmem fmem = repository.findByFmemAcc(fmemAccLogin);
		
		if(fmem == null) {
			return null;
		}
		
		if(!fmem.getFmemPwd().equals(fmemPwdLogin)) {
			return null;
		}
		
		if(fmem.getAccStatus() != 1) {
			throw new IllegalStateException("帳號尚未通過審核或已被停權");
		}
		
		return fmem;
		
	}
	
	public void Register(String fmemAcc) {
		Fmem fmem = repository.findByFmemAcc(fmemAcc);
		if(fmem != null) {
			throw new IllegalStateException("此帳號已有人註冊過");
		}
		
	}
	
	
	
	
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
