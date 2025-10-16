package com.farmtastic.admin.model;

import java.io.Serializable;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "admin_type")
public class AdminType implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "admin_type_id")
	private Integer adminTypeId;
	
	@Column(name = "admin_type_name")
	private String adminTypeName;
	
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "admin_type_func_list",
            joinColumns = @JoinColumn(name = "admin_type_id"),
            inverseJoinColumns = @JoinColumn(name = "admin_func_id")
    )
    private Set<AdminFunction> functions;
}
