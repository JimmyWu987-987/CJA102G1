package com.farmtastic.proorder.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farmtastic.member.model.Mem;
import com.farmtastic.proorderitem.model.ProOrderItemRepository;
import com.farmtastic.proorderitem.model.ProOrderItemVO;



@Service
public class ProOrderSevice {

		@Autowired
		ProOrderRepository repository;
		@Autowired
		ProOrderItemRepository proOrderItemRepository;
		
//		@Autowired
//		private SessionFactory sessionFactory;
		
		// 新增
		@Transactional
		public void addProOrder(ProOrderVO proOrderVO,List<ProOrderItemVO> proOrderItemVO) {
	        // 1. 先儲存 ProOrderVO，讓 JPA 自動產生 proOrdId
	        repository.save(proOrderVO);
	        
	        // 2. 取得剛剛生成的主鍵 proOrdId
	        Integer generatedOrdId = proOrderVO.getProOrdId(); 
	        
	        // 3. 遍歷訂單明細，設定好關聯實體和複合主鍵
	        for (ProOrderItemVO item : proOrderItemVO) {
	            // 設定 Many-to-One 關聯：將完整的 ProOrderVO 實體設定給明細
	            item.setProOrderVO(proOrderVO); 
	            
	            // 設定複合主鍵：將剛生成的訂單 ID 設定給 ProOrderItemId
	            item.getId().setProOrdId(generatedOrdId); 
	            
	            // 設定 One-to-Many 關聯：將明細回設給 ProOrderVO 的集合 (可選，但保持物件狀態完整)
	            // 由於你的 ProOrderVO 已經設置了 CascadeType.ALL 和 orphanRemoval = true，
	            // 最佳做法是將明細集合設定給 ProOrderVO，然後只呼叫一次 save(proOrderVO)。
	            // 但如果選擇分兩次 save，這裡可以不用設定，直接進行下一步。
	        }

	        // 4. 儲存所有訂單明細 (現在明細的複合主鍵已完整)
			proOrderItemRepository.saveAll(proOrderItemVO);
		}
		// 修改
		public void updateProOrder(ProOrderVO proOrderVO) {
			repository.save(proOrderVO);
		}
		// 刪除
		public void deleteProOrder(Integer proOrdId) {
			if(repository.existsById(proOrdId)) {
				// 當執行 deleteById 時，JPA 會因為 ProOrderVO 設定的 cascade=ALL
				// 而自動刪除所有關聯的 ProOrderItemVO。
				repository.deleteById(proOrdId);
			}
		}
		// 查全部
		public List<ProOrderVO> getAll(){
			return repository.findAll();
		}
		
		// 訂單編號的單一查詢
		public ProOrderVO getOneProOrder(Integer proOrdId) {
			Optional<ProOrderVO> optional = repository.findById(proOrdId);
			return optional.orElse(null);
		}
		
		// 一般會員查自己的全部訂單
		public List<ProOrderVO> getAllByMemId(Mem MemVo){
			return repository.findByMemVO(MemVo);
		}
		
		// 小農fmem查詢自己的全部表單
		public List<FmemOrderSummary> getAllByFmemId(Integer fmemId){
			return repository.findFmemProOrders(fmemId);
		}
		// 小農fmem查詢該會員有幾筆訂單
		// 小農查詢該商品有幾筆訂單
		// 後台查詢該小農商品有幾筆訂單（回傳多筆）
		// 用復合查詢？
		
	
}
