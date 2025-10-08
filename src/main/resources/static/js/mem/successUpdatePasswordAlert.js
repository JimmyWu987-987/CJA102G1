
window.addEventListener("load", function(){
 	const successMsg = document.body.dataset.success;
//	一般會員
 	if(successMsg === "修改密碼成功"){
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
 	} else if(successMsg === "重設密碼成功"){
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
	} else if(successMsg === "成功發送驗證信"){
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
	
	
	
 	const failMsg = document.body.dataset.fail;
 	if(failMsg === "驗證碼失效或不存在"){
 		Swal.fire({
             icon: 'warning',
             title: failMsg,
			 text: '請重新操作',
             showConfirmButton: false,
			 customClass: {
				 icon: 'custom-icon',
			     popup: 'custom-swal',
			     title: 'custom-swal-title',
			     htmlContainer: 'custom-swal-content'
			  }
         });
 	} else if(failMsg === "使用者不存在"){
		Swal.fire({
        	icon: 'warning',
        	title: failMsg,
 			text: '請重新操作',
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