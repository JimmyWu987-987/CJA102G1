
window.addEventListener('load', function(){
	const supplementForm = document.querySelector('form#supplement');
	if(supplementForm) supplementForm.addEventListener('submit', showAlert);
})


function showAlert(e) {
    e.preventDefault();
    Swal.fire({
        title: '確定送出補件資料',
		text: '已確認內容無誤，送出後即無法再修改',
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
