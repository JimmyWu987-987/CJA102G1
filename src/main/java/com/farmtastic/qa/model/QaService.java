package com.farmtastic.qa.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class QaService {

	@Autowired
	private	QaRepository qaRepository;
	
	public List<Qa> findAll(){
		return qaRepository.findAll();
	}
	
	public Optional<Qa> findById(Integer id){
		return qaRepository.findById(id);
	}
	
    public Qa save(Qa qaList) {
        return qaRepository.save(qaList);
    }

    public void deleteById(Integer id) {
        qaRepository.deleteById(id);
    }
	
}
