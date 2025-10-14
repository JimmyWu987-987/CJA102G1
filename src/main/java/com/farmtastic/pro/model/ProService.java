package com.farmtastic.pro.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("proService")
public class ProService {

	@Autowired
	ProRepository repository;

	@Caching(evict = { 
		@CacheEvict(value = "pro", allEntries = true),
		@CacheEvict(value = "pros", allEntries = true)
	})
	public Pro addPro(Pro pro) {
		return repository.save(pro);
	}


	@Transactional
	@Caching(evict = { 
		@CacheEvict(value = "pro", key = "#proFromForm.proId"), 
		@CacheEvict(value = "pros", allEntries = true) 
	})
	public Pro updatePro(Pro proFromForm) {
	    // 1. 從資料庫讀取原始的產品資料
	    Optional<Pro> existingProOpt = repository.findById(proFromForm.getProId());

	    if (existingProOpt.isPresent()) {
	        Pro existingPro = existingProOpt.get();

	        // 2. 將表單提交的新資料，逐一更新到原始物件上
	        existingPro.setProName(proFromForm.getProName());
	        existingPro.setProStock(proFromForm.getProStock());
	        existingPro.setProPrice(proFromForm.getProPrice());
	        existingPro.setProStatus(proFromForm.getProStatus());
	        existingPro.setProFrom(proFromForm.getProFrom());
	        existingPro.setProDes(proFromForm.getProDes());
	        existingPro.setProcateId(proFromForm.getProcateId());
	        
	        // ✅ 明確保留 fmemId（如果表單沒有傳入）
	        if (proFromForm.getFmemId() != null) {
	            existingPro.setFmemId(proFromForm.getFmemId());
	        }
	        // 如果 proFromForm.getFmemId() 是 null，就不更新，保留原值

	        // 3. 儲存更新後的物件
	        return repository.save(existingPro);
	    } else {
	        // 如果找不到對應的產品，直接儲存
	        return repository.save(proFromForm);
	    }
	}


	@Caching(evict = { 
		@CacheEvict(value = "pro", key = "#proId"), 
		@CacheEvict(value = "pros", allEntries = true) 
	})
	
	public void deletePro(Integer proId) { 
		if (repository.existsById(proId))
			repository.deleteById(proId);
	}
	

	@Cacheable(value = "pro", key = "#proId", unless = "#result == null")
	public Pro getOnePro(Integer proId) { 
		Optional<Pro> optional = repository.findById(proId);
		return optional.orElse(null);
	}

	@Cacheable(value = "pros") // 第一次查詢時快取 getAll 的結果
	public List<Pro> getAll() {
		return repository.findAll();
	}
	

	public Page<Pro> findPaginated(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		return repository.findAll(pageable);
	}

//	public List<Pro> getRandomPros(int count) {
//		List<Pro> allPros = repository.findAll();
//		Collections.shuffle(allPros);
//		return allPros.stream().limit(count).collect(Collectors.toList());
//	}

	public List<Pro> getAll(Map<String, String[]> map) {
		Specification<Pro> spec = (root, query, criteriaBuilder) -> {
			
			if (query.getResultType() != Long.class && query.getResultType() != long.class) {
				root.fetch("fmemId", JoinType.LEFT);
				root.fetch("procateId", JoinType.LEFT);
			}
			query.distinct(true); 
			
			List<Predicate> predicates = new ArrayList<>();
			for (Map.Entry<String, String[]> entry : map.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue()[0];
				if (value == null || value.trim().isEmpty() || "action".equals(key)) {
					continue;
				}
				switch (key) {
					case "proId": // ●●● 複合查詢也使用 proId ●●●
						predicates.add(criteriaBuilder.equal(root.get("proId"), Integer.valueOf(value)));
						break;
					case "proName":
					case "proFrom":
						predicates.add(criteriaBuilder.like(root.get(key), "%" + value + "%"));
						break;
					case "procateId":
						predicates.add(criteriaBuilder.equal(root.get("procateId").get("proCateId"), Integer.valueOf(value)));
						break;
					case "fmemId":
						predicates.add(criteriaBuilder.equal(root.get("fmemId").get("fmemId"), Integer.valueOf(value)));
						break;
				}
			}
			if (predicates.isEmpty()) {
				return criteriaBuilder.conjunction();
			} else {
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			}
		};
		return repository.findAll(spec);
	}
	
    public List<Pro> findByFmemId(Integer fmemId) {
        return repository.findByFmemId_FmemId(fmemId);
    }
}