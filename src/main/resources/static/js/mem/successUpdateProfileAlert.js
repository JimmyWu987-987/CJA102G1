
window.addEventListener("load", function(){
 	const successMsg = document.body.dataset.success;
	const statusMsg = document.body.dataset.status;
	 	if(statusMsg === "已啟用"){
			const warningMsg = document.querySelector('.warning-msg');
			const starTags = document.querySelectorAll('.star-tag');
//			if(warningMsg) {
//				warningMsg.style.color = "#f26744"
//			}
			
	 		Swal.fire({
	             icon: 'success',
	             title: '恭喜完成開店流程！',
				 text: '商店已可開始上架及販售商品，點擊左上角小農logo可預覽個人商店頁面',
	             showConfirmButton: false,
				 customClass: {
					 icon: 'custom-icon',
				     popup: 'custom-swal',
				     title: 'custom-swal-title',
				     htmlContainer: 'custom-swal-content'
				  }
	         });
	 	} else if(successMsg === "修改資料成功"){
	 		Swal.fire({
	             icon: 'success',
	             title: successMsg,
	             showConfirmButton: false,
				 customClass: {
					 icon: 'custom-icon',
				     popup: 'custom-swal',
				     title: 'custom-swal-title',
				     htmlContainer: 'custom-swal-content'
				 }
	         });
	 	} else if(successMsg === "生日填寫完成"){
	 		Swal.fire({
	             icon: 'success',
	             title: successMsg,
	             showConfirmButton: false,
				 customClass: {
					 icon: 'custom-icon',
				     popup: 'custom-swal',
				     title: 'custom-swal-title',
				     htmlContainer: 'custom-swal-content'
				 }
	         });
 		}
	
	
 })