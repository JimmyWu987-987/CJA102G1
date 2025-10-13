

function showAlert(e) {
    e.preventDefault(); // 先不要真的送出
    Swal.fire({
        title: '確定更改狀態',
//		text: '已確認資料無誤，送出後即無法再修改',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: '確定',
        cancelButtonText: '取消',
		customClass: {
			icon: 'custom-icon',
		    popup: 'custom-swal',
		    title: 'custom-swal-title',
		    htmlContainer: 'custom-swal-content',
		    confirmButton: 'custom-swal-confirm',
		    cancelButton: 'custom-swal-cancel'
		}
    }).then((result) => {
        if (result.isConfirmed) {
            e.target.submit(); // 使用者點確認再送出表單
        }
    });
}
	
