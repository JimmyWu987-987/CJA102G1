package com.farmtastic.member.model;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmtastic.fmember.model.Fmem;

@Service("memService")
public class MemService {
	
	@Autowired
	MemRepository repository;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	
	
	public Mem login(String memAccLogin, String memPwdLogin) {
		
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
	
	public boolean existsByMemAcc(String memAcc) {
		return repository.findByMemAcc(memAcc) != null;
	}

	public boolean existsByMemMobile(String memMobile) {
		return repository.findByMemMobile(memMobile) != null;
	}
	
	
	public Mem forgetPassword(String memMobile, String memEmail) {
		Mem mem = repository.findByMemMobile(memMobile);
		if (mem == null) {
			return null;
		}
		
		if (!mem.getMemEmail().equals(memEmail)) {
			return null;
		}
		
		if (mem.getAccStatus() != 1) {
			throw new IllegalStateException("帳號尚未開通或已被停權");
		}
		
		return mem;
	}
	
	
	
	public void addMem(Mem mem) {
		repository.save(mem);
	}
	
	public void updateMem(Mem mem) {
		System.out.println("memId: " + mem.getMemId());
		repository.save(mem);
	}

	public Mem getOneByMemAcc(String memAcc) {
		return repository.findByMemAcc(memAcc);
	}
	
	public Mem getOneByMemId(Integer memId) {
		return repository.findById(memId).orElse(null);
	}
	
	// for coupon
	public Date getBirthdayByMemId(Integer memId) {
		Mem mem = repository.findById(memId).orElse(null);
		if(mem != null) {
			return mem.getMemBirthday();
		}
		return null;
	}
	/////////////////////
	
	public List<Mem> getAll(){
		return repository.findAll();
	}
	
	public void updateAccStatus(Integer memId, Byte accStatus) {
		Mem mem = repository.findById(memId).orElse(null);
		if(memId != null) {
			mem.setAccStatus(accStatus);
			repository.save(mem);
		}
	}

}
