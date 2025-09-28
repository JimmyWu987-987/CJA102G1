package com.farmtastic.proorderitem.model;

import com.farmtastic.proorder.model.ProOrderVO;

import jakarta.persistence.*;


@Entity
@Table(name="pro_order_item")
public class ProOrderItemVO {
	
	@EmbeddedId
	private ProOrderItemId id;
	
	// 等有了ProductVO後再不上
//	@MapsId("proId")
//	@ManyToOne
//	@JoinColumn(name="pro_id")
//	private ProductVO prodrderVO;
	
//	@Id
//	@Column(name="pro_id")
//	private Integer proId; //PK.FK
	
	@MapsId("proOrdId")
	@ManyToOne
	@JoinColumn(name="pro_ord_id")
	private ProOrderVO proOrderVO;
//	@Column(name="pro_ord_id")
//	private Integer proOrdId; //PK.FK
	
	@Column(name="pro_unitprice")
	private Integer proUnitPrice;
	
	@Column(name="pro_amount")
	private Integer proAmount;
	
	@Column(name="pro_subtotal")
	private Integer proSubTota;
	
	
	public ProOrderItemVO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
