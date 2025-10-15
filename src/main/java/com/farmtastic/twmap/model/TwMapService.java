package com.farmtastic.twmap.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farmtastic.fmember.model.Fmem;
import com.farmtastic.fmember.model.FmemRepository;

@Service
@Transactional(readOnly = true)
public class TwMapService {
	@Autowired
	private FmemRepository fmemRepository;
	
	//根據城市找全部小農
	public List<Fmem> findByCity(String city) {
		String c = city == null ? "" : city.trim().replace("臺", "台");
		return fmemRepository.findByFmemCity(c);
	}

	public Fmem findOne(Integer id) {
		return fmemRepository.findById(id).orElse(null);
	}

}
