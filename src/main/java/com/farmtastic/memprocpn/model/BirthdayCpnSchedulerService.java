package com.farmtastic.memprocpn.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.farmtastic.common.enums.CpnSource;
import com.farmtastic.common.enums.IsActive;
import com.farmtastic.member.model.Mem;
import com.farmtastic.member.model.MemRepository;
import com.farmtastic.procpn.model.ProCpnRepository;
import com.farmtastic.procpn.model.ProCpnVO;

@Service
public class BirthdayCpnSchedulerService {
	private final MemRepository memRepository;
	private final MemProCpnServiceImp memProCpnService;
	private final ProCpnRepository proCpnRepo;

	public BirthdayCpnSchedulerService(MemRepository memRepository, MemProCpnServiceImp memProCpnService,
			ProCpnRepository proCpnRepo) {
		this.memRepository = memRepository;
		this.memProCpnService = memProCpnService;
		this.proCpnRepo = proCpnRepo;
	}

	@Scheduled(cron = "0 0 2 * * *", zone = "Asia/Taipei")
	public void sendBirthdayCoupons() {
		LocalDate today = LocalDate.now();
		List<Mem> birthdayMembers = memRepository.findByBirthday(today.getMonthValue(), today.getDayOfMonth());

		if (birthdayMembers.isEmpty()) {
			System.out.println("今日無壽星，排程結束。");
			return;
		}

		System.out.printf("今日生日會員數：%d 位%n", birthdayMembers.size());

		ProCpnVO birthdayCpn = proCpnRepo
				.findFirstByCpnSourceAndIsActiveOrderByCrtAtDesc(CpnSource.BIRTHDAY, IsActive.ACTIVE)
				.orElseThrow(() -> new IllegalStateException("⚠️ 尚無啟用中的生日券"));

		for (Mem mem : birthdayMembers) {
			System.out.printf("發送生日券給會員 ID=%d%n", mem.getMemId());
			memProCpnService.giveCoupon(mem.getMemId(), birthdayCpn.getProCpnId());
		}
	}
}
