package com.farmtastic.fmember.model;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmtastic.member.model.Mem;

@Service("fmemService")
public class FmemService {

	@Autowired
	FmemRepository repository;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public Fmem login(String fmemAccLogin, String fmemPwdLogin) {
		
		Fmem fmem = repository.findByFmemAcc(fmemAccLogin);
		
		if(fmem == null) {
			return null;
		}
		
		if(!fmem.getFmemPwd().equals(fmemPwdLogin)) {
			return null;
		}
		
		if((fmem.getAccStatus() != 2) && (fmem.getAccStatus() != 1)) {
			throw new IllegalStateException("帳號尚未通過審核或已被停權");
		}
		
		return fmem;
	}
	
	
	public boolean existsByFmemAcc(String fmemAcc) {
		return repository.findByFmemAcc(fmemAcc) != null;
	}

	public boolean existsByFmemMobile(String fmemMobile) {
		return repository.findByFmemMobile(fmemMobile) != null;
	}
	
	public boolean existsByFId(String fId) {
		return repository.findByFid(fId) != null;
	}
	

	public Fmem forgetPassword(String fmemMobile, String fmemEmail) {
		Fmem fmem = repository.findByFmemMobile(fmemMobile);
		if (fmem == null) {
			return null;
		}
		
		if (!fmem.getFmemEmail().equals(fmemEmail)) {
			return null;
		}
		
		if ((fmem.getAccStatus() != 2) && (fmem.getAccStatus() != 1)) {
			System.out.println("fmem.getAccStatus()"+ fmem.getAccStatus());
		}
		
		return fmem;
	}
	
	
	public void addFmem(Fmem fmem) {
		repository.save(fmem);
	}
	public void updateFmem(Fmem fmem) {
		repository.save(fmem);
	}
	
	public Fmem getOneByFmemAcc(String fmemAcc) {
		return repository.findByFmemAcc(fmemAcc);
	}
	
	public Fmem getOneByFmemId(Integer fmemId) {
		return repository.findById(fmemId).orElse(null);
	}
	
	public List<Fmem> getAll(){
		return repository.findAll();
	}
	
	public void updateAccStatus(Integer fmemId, Byte accStatus) {
		Fmem fmem = repository.findById(fmemId).orElse(null);
		if(fmem != null) {
			fmem.setAccStatus(accStatus);
			repository.save(fmem);
		}
	}
	
}
