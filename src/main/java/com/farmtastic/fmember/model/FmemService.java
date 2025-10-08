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
		
		if(fmem.getAccStatus() != 1) {
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
	
//	public void register(String fmemAcc) {
//		Fmem fmem = repository.findByFmemAcc(fmemAcc);
//		if(fmem != null) {
//			throw new IllegalStateException("此帳號已有人註冊過");
//		}
//		
//	}

	public Fmem forgetPassword(String fmemMobile, String fmemEmail) {
		Fmem fmem = repository.findByFmemMobile(fmemMobile);
		if (fmem == null) {
			return null;
		}
		
		if (!fmem.getFmemEmail().equals(fmemEmail)) {
			return null;
		}
		
		if (fmem.getAccStatus() != 1) {
			throw new IllegalStateException("帳號尚未開通或已被停權");
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
	
	public Optional<Fmem> getOneByFmemId(Integer fmemId) {
		return repository.findById(fmemId);
	}
	
	public List<Fmem> getAll(){
		return repository.findAll();
	}
}
