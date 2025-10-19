package com.farmtastic.favopro.model;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmtastic.member.model.MemRepository;
import com.farmtastic.pro.model.ProRepository;

import jakarta.transaction.Transactional;

@Service("favoProService")
@Transactional
public class FavoProServiceImp {
	private final FavoCacheService cache;
	private final FavoProRepository favoRepo;
	private final MemRepository memRepo;
	private final ProRepository proRepo;

	// 建構子注入
	@Autowired
	public FavoProServiceImp(FavoCacheService cache, FavoProRepository favoRepo, MemRepository memRepo,
			ProRepository proRepo) {
		this.cache = cache;
		this.favoRepo = favoRepo;
		this.memRepo = memRepo;
		this.proRepo = proRepo;
	}

//Hibernate 用SessionFactory 管理連線與交易
//private SessionFactory sessionFactory;
	// 新增收藏
	public void addFavoPro(Integer memId, Integer proId) {
		System.out.println("開始新增收藏: memId=" + memId + ", proId=" + proId);
		// 判定快取
		if (cache.isFavorite(memId, proId)) {
			System.out.println("⚠️ 已在快取中");
			throw new IllegalStateException("已在收藏清單中（快取）");
		}
		FavoProId favoProId = new FavoProId(memId, proId);

		if (favoRepo.existsById(favoProId)) {
			throw new IllegalStateException("該會員已收藏此商品");
		}
		FavoProVO favo = new FavoProVO();
		favo.setId(favoProId);
		// setMemVO 放入 Optional<Mem>
		favo.setMemVO(memRepo.findById(memId).orElseThrow());
		favo.setProductVO(proRepo.findById(proId).orElseThrow());
		favoRepo.save(favo);
		System.out.print("Service，新增收藏" + favo);

		// 同步更新快取
		cache.addFavorite(memId, proId);
	}

	// 取消收藏
	public void removeFavoPro(Integer memId, Integer proId) {
		FavoProId favoProId = new FavoProId(memId, proId);
		if (!favoRepo.existsById(favoProId)) {
			throw new IllegalArgumentException("收藏紀錄不存在: " + favoProId);
		}
		// 刪除資料庫
		favoRepo.deleteById(favoProId);
		// 刪除快取
		cache.removeFavorite(memId, proId);
	}

	// 檢查是否已收藏
	public boolean isFavorite(Integer memId, Integer proId) {
		return cache.isFavorite(memId, proId) || favoRepo.existsById(new FavoProId(memId, proId));
	}

	// 前台用：查詢某會員收藏清單
	// 快取 + DB
	public List<FavoProVO> getByMember(Integer memId) {
		System.out.print("快取+DB");
		// 取出該會員所有商品ID(從快取過濾)
		Set<FavoProId> all = cache.getAllFavorites();
		// 1.Set 轉成 Stream 逐一處理所有 FavoProId
		// 2.filter(條件 id getMemId()取出會員編號 equals(memId)比對是否等於目前查詢的會員)
		// 3.map(FavoProId 物件轉成它的商品 ID)
		// 4.collect(Collectors.toSet())收集完從STEAM轉回SET
		Set<Integer> proIds = all.stream().filter(id -> id.getMemId().equals(memId)).map(FavoProId::getProId)
				.collect(Collectors.toSet());
		// 若快取空 → 查資料庫 (防止快取不同步)
		if (proIds.isEmpty()) {
			return favoRepo.findByMemVO_MemId(memId);
		}
		// 用商品ID批量查詢完整資料
		return favoRepo.findAllById(proIds.stream().map(pid -> new FavoProId(memId, pid)).collect(Collectors.toSet()));
	}

	// 後台管理用：查詢所有收藏紀錄(目前沒用到)
	public List<FavoProVO> getAll() {
		return favoRepo.findAll();
	}
}
