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
	
	
	
	public Mem Login(String memAccLogin, String memPwdLogin) {
		
		// 1.先檢查帳號是否存在
		Mem mem = repository.findByMemAcc(memAccLogin);
		if(mem == null) {
			return null; //帳號不存在
		}
		
		// 2.檢查密碼是否正確
		if(!mem.getMemPwd().equals(memPwdLogin)) {
			return null; //密碼錯誤
		}
		
		// 3.檢查帳號狀態
		if(mem.getAccStatus() != 1) {
			throw new IllegalStateException("帳號尚未開通或已被停權");
		}
		
		return mem; //登入成功
	}
	
	public void Register(String memAcc) {
		//檢查帳號有沒有人使用過
		Mem mem = repository.findByMemAcc(memAcc);
		if(mem != null) {
			throw new IllegalStateException("此帳號已有人註冊過");
		}
	}
	
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
