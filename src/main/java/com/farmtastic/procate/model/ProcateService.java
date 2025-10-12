package com.farmtastic.procate.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("procateService")
public class ProcateService {

    @Autowired
    ProcateRepository procateRepository;

    /**
     * 獲取所有產品類別
     * @return 產品類別列表
     */
    public List<Procate> getAll() {
        return procateRepository.findAll();
    }

}
