package com.farmtastic;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.farmtastic.fmember.model.Fmem;
import com.farmtastic.fmember.model.FmemRepository;

import jakarta.persistence.Column;

@SpringBootApplication
public class Test_Application_CommandLineRunner implements CommandLineRunner {
	
	@Autowired
//	MemRepository repository;
	FmemRepository repository;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public static void main(String[] args) {
		SpringApplication.run(Test_Application_CommandLineRunner.class);
	}
	
	public void run(String... args) throws Exception {
		
//		新增fmem OK
//		Fmem fmem = new Fmem();
//
//		fmem.setFId("A123456789");
//		fmem.setFmemAcc("accounttest11");
//		fmem.setFmemPwd("passwordtest11");
//
//
//		fmem.setFmemName("夸父");
//		fmem.setFmemMobile("0912-555888");
//		
//		fmem.setFmemTel("02-22558855");
//		fmem.setFmemEmail("farmer011@gmail.com");
//		fmem.setFmemZipcode("123");
//		fmem.setFmemCity("嘉義縣");
//		fmem.setFmemDist("某某鄉");
//		fmem.setFmemAddr("某某路20號");
//		fmem.setBankCode("700");
//		fmem.setBankAcc("00123350078965");
//
//		repository.save(fmem);
		
		
		
		Fmem fmem = repository.findById(11).orElseThrow();
//		fmem.setFmemId(11);
//		fmem.setFId("A123456789");
//		fmem.setFmemAcc("accounttest11");
//		fmem.setFmemPwd("passwordtest11");
		fmem.setAccStatus((byte)1);
//		fmem.setAccDesc("尚未審核");
		
		
//		fmem.setFmemName("夸父");
//		fmem.setFmemMobile("0912-555888");
//		
//		fmem.setFmemTel("02-22558855");
//		fmem.setFmemEmail("farmer011@gmail.com");
//		fmem.setFmemZipcode("123");
//		fmem.setFmemCity("嘉義縣");
//		fmem.setFmemDist("某某鄉");
//		fmem.setFmemAddr("某某路20號");
//		fmem.setBankCode("700");
//		fmem.setBankAcc("00123350078965");
		
//		fmem.setRegDate(java.sql.Timestamp.valueOf("2025-09-25 22:35:10"));
		
		repository.save(fmem);
		
//		fmem.setcertiStatus("");
//		fmem.setFfmemPic("");
//		fmem.setorganicPic("");
//		fmem.setlandPic("");
//		fmem.setinsurPic("");
//		fmem.setstorePic("");
//		fmem.setstoreName("");
		
//		fmem.setFstoreIntro("");
//		fmem.setstyNo("");
//		fmem.setmktScore("");
	
		
		

//		private Timestamp regDate;
//		private Byte certiStatus;
//		private byte[] fmemPic;
//		private byte[] organicPic;
//		private byte[] landPic;
//		private byte[] insurPic;
//		private byte[] storePic;
//		private String storeName;
//		
//		
//		private String storeIntro;
//		private Byte styNo;
//		private Integer mktScore;
//		private Integer mktCnt;
//		private Integer actScore;
//		private Integer actCnt;
//		private Byte rptCnt;
//		private Integer prodFee;
		
		
		
		
		
		//查詢fmem ALL  ok
//		List<Fmem> fmemList = repository.findAll();
//		for(Fmem fmem : fmemList) {
//			System.out.print(fmem.getFmemId() + ", ");
//			System.out.print(fmem.getFId() + ", ");
//			System.out.println(fmem.getFmemName() + ", ");
//		}
		
		
		
		
		
		//新增mem  ok
//		Mem mem = new Mem();
//		mem.setMemAcc("demoTestAdd");
//		mem.setMemPwd("demoPassword");
//		mem.setMemName("關公");
//		mem.setMemBirthday(java.sql.Date.valueOf("2005-10-05"));
//		mem.setMemMobile("0910-155255");
//		mem.setMemEmail("demo0924@gmail.com");
//		mem.setMemZipcode("500");
//		mem.setMemCity("彰化縣");
//		mem.setMemDist("彰化市");
//		mem.setMemAddr("中正路1號");
//		repository.save(mem);
//		
		
		//修改mem 修改全部欄位OK
//		Mem mem = new Mem();
//		
//		mem.setMemId(21);
//		mem.setMemAcc("demoTestAdd");
//		mem.setMemPwd("demoPassword");
//		mem.setAccStatus((byte) 2);
//		mem.setMemName("關公");
//		mem.setMemBirthday(java.sql.Date.valueOf("2005-10-05"));
//		mem.setMemMobile("0910-155255");
//		mem.setMemEmail("demo0924@gmail.com");
//		mem.setMemZipcode("500");
//		mem.setMemCity("彰化縣");
//		mem.setMemDist("彰化市");
//		mem.setMemAddr("中正路1號");
//		mem.setRegDate(java.sql.Timestamp.valueOf("2025-09-25 10:10:10"));
//		mem.setMemPoint(50);
//		repository.save(mem);
//		
		
		//查詢mem ALL  ok
//		List<Mem> memList = repository.findAll();
//		for(Mem mem : memList) {
//			System.out.print(mem.getMemId() + ",");
//			System.out.print(mem.getMemAcc() + ",");
//			System.out.println(mem.getMemName() + ",");
//		}
	}
}
