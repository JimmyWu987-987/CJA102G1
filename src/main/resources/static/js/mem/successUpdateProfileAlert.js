
window.addEventListener("load", function(){
 	const successMsg = document.body.dataset.success;
//	一般會員
 	if(successMsg === "修改資料成功"){
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