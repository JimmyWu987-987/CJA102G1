
// 註冊完成跳出"註冊成功"alert

window.addEventListener("load", function(){
 	const successMsg = document.body.dataset.success;
//	一般會員
 	if(successMsg === "註冊成功"){
 		Swal.fire({
             icon: 'success',
             title: successMsg,
			 text: "請至Email驗證以開通帳號",
             showConfirmButton: false,
			 customClass: {
				 icon: 'custom-icon',
			     popup: 'custom-swal',
			     title: 'custom-swal-title',
			     htmlContainer: 'custom-swal-content'
			  }
         });
 	} else if(successMsg === "小農會員註冊成功"){
		Swal.fire({
             icon: 'success',
             title: successMsg,
			 text: '請等候後台審核，約需1~2天，審核完成後將以Email發送通知，請留意收信',
             showConfirmButton: false,
			 customClass: {
				 icon: 'custom-icon',
			     popup: 'custom-swal',
			     title: 'custom-swal-title',
			     htmlContainer: 'custom-swal-content'
			  }
         });
	} else if(successMsg === "驗證成功，帳號已啟用"){
		Swal.fire({
             icon: 'success',
             title: successMsg,
//			 text: '',
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
