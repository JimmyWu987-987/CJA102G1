package com.farmtastic.actcpn.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farmtastic.common.enums.DiscountType;
import com.farmtastic.common.enums.IsActive;
import com.farmtastic.common.mapper.ActCpnMapperImp;
import com.farmtastic.memactcpn.model.MemActCpnRepository;
import com.farmtastic.memprocpn.model.SpinServiceImp;

@Service("actCpnService")
public class ActCpnServiceImp implements ActCpnService {
	private final ActCpnRepository actCpnRepo;
	private final MemActCpnRepository memActCpnRepo;
	private final ActCpnMapperImp actCpnMapper;
	private final SpinServiceImp spinServiceImp;

	@Autowired
	public ActCpnServiceImp(ActCpnRepository actCpnRepo, MemActCpnRepository memActCpnRepo,
			ActCpnMapperImp actCpnMapper, SpinServiceImp spinServiceImp) {
		this.actCpnRepo = actCpnRepo;
		this.memActCpnRepo = memActCpnRepo;
		this.actCpnMapper = actCpnMapper;
		this.spinServiceImp = spinServiceImp;
	}

	// 新增活動折價券
	@Override
	public void addActCpn(ActCpnVO actCpnVO) {

		// === 1. 自動命名邏輯 ===
		if (actCpnVO.getCpnName() == null || actCpnVO.getCpnName().isBlank()) {
			String cpnName;

			if (actCpnVO.getDiscType() == DiscountType.PERCENTAGE) {
				// 百分比折扣 → 顯示成「85折」
				int discount = actCpnVO.getDiscValue().intValue();
				cpnName = actCpnVO.getCpnSource().getText() + discount + "折";
			} else {
				// 滿額折抵 → 顯示成「折100元」
				cpnName = actCpnVO.getCpnSource().getText() + "折" + actCpnVO.getDiscValue().intValue();
			}

			actCpnVO.setCpnName(cpnName);
		}

		// === 2. 儲存折價券 ===
		ActCpnVO saved = actCpnRepo.save(actCpnVO);

		// === 3. 根據折價券來源決定行為 ===
		switch (actCpnVO.getCpnSource()) {

		case LOTTERY -> {
			// 抽獎券 → 更新抽獎池
			spinServiceImp.refreshLotteryPool();
			System.out.println("新增抽獎券 → 已同步至抽獎池：" + saved.getCpnName());
		}

		case REGISTRATION -> {
			// 註冊券 → 通知會員服務（未來自動發送）
			System.out.println("新增註冊券 → 系統將於會員註冊時自動發送");
			// 可選：註冊時會自動發放，不需立即處理
		}

		case BIRTHDAY -> {
			// 生日券 → 交由排程發放
			System.out.println("新增生日券 → 由排程自動發放");
			// 可選：不需立即動作
		}

		case EVENT -> {
			// 活動券 → 由活動模組掛載
			System.out.println("新增活動券 → 活動模組可引用此券ID：" + saved.getActCpnId());
		}

		default -> {
			// 一般券 → 不需觸發任何自動邏輯
			System.out.println("新增一般券 → " + saved.getCpnName());
		}
		}
	}

//更新
	@Override
	public void updateActCpn(ActCpnVO actcpnVO) {
		actCpnRepo.save(actcpnVO);
	}

// 刪除
	@Override
	public void deleteActCpn(Integer id) {
		actCpnRepo.deleteById(id);
	}

//查單筆
	// 外部介面層（Controller）用實體或 null
	@Override
	public ActCpnVO getOneActCpn(Integer actCpnId) {
		Optional<ActCpnVO> optional = actCpnRepo.findById(actCpnId);
		return optional.orElse(null);
	}

	// 內部邏輯層（Service）用 Optional
	public Optional<ActCpnVO> getById(Integer actCpnId) {
		return actCpnRepo.findById(actCpnId);
	}

//查全部
	@Override
	public List<ActCpnVO> getAll() {
		return actCpnRepo.findAll();
	}

	// 改變卷狀態 啟用或停用
	@Override
	@Transactional
	public void changeActCpnStatus(Integer actCpnId, IsActive status) {
		ActCpnVO actcpnVO = actCpnRepo.findById(actCpnId)
				.orElseThrow(() -> new RuntimeException("找不到折價券 ID: " + actCpnId));
		if (actcpnVO.getIsActive().equals(status))
			return; // 避免重複設定
		actCpnRepo.updateStatus(actCpnId, status);
	}

	// 查啟用券
	@Override
	public List<ActCpnVO> getActiveActCpn() {
		return actCpnRepo.findByIsActive(IsActive.ACTIVE);
	}

	// 名稱模糊搜尋
	@Override
	public List<ActCpnVO> findByKeyword(String keyword) {
		return actCpnRepo.findByCpnNameContaining(keyword);
	}

	// 查詢指定日期範圍內的折價券
	@Override
	public List<ActCpnVO> filterByDateRange(LocalDate start, LocalDate end) {
		if (start != null && end != null)
			return actCpnRepo.findByStartDateBetween(start, end);
		else if (start != null)
			return actCpnRepo.findByStartDateAfter(start);
		else if (end != null)
			return actCpnRepo.findByStartDateBefore(end);
		else
			return actCpnRepo.findAll();
	}

	// 提供前台可領取清單
	@Override
	public List<ActCpnVO> findAvailableForMember() {
		return actCpnRepo.findAvailableForMember();
	}

	// 3點自動停過期卷
	@Scheduled(cron = "0 0 3 * * *", zone = "Asia/Taipei")
	@Override
	public void deactivateExpiredCoupons() {
		actCpnRepo.deactivateExpiredCoupons();
		System.out.println("[Scheduler] 自動停用過期折價券完成：" + LocalDate.now());
	}

}
