


window.addEventListener('load', function(){
	
	const statusRadios = document.querySelectorAll('.status-option input');
	statusRadios.forEach(statusRadio => {
		statusRadio.addEventListener('change', showDescRadios);
	})
	
	const descRadios = document.querySelectorAll('.desc-option input');
	descRadios.forEach(descRadio => {
		descRadio.addEventListener('change', openTextarea);
	})
	
	const imgNails = document.querySelectorAll('.img-nail');
	imgNails.forEach(imgNail => {
	    imgNail.addEventListener('click', previewImage);
	})
	
	//送出表單前確認alert
	document.querySelector("#review").addEventListener("submit", showAlert);
})


function showAlert(e) {
    e.preventDefault(); // 先不要真的送出
    Swal.fire({
        title: '送出審核結果',
		text: '送出後系統會自動發送email通知小農審核結果',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: '確認送出',
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



function showDescRadios(e){
//	console.log(e.target);
	if(e.target.checked){
		const descOptions = document.querySelector('.desc-options');
		if(e.target.id === 'fail'){
			if(descOptions) descOptions.style.display = 'flex';
		} else if(e.target.id === 'pass'){
			if(descOptions) descOptions.style.display = 'none';
		}
	}
}

function openTextarea(e){
	if(e.target.checked){
		const descTextarea = document.querySelector('.desc-option textarea');
		if(e.target.id === 'other'){
			if(descTextarea) descTextarea.disabled = false;
		} else {
			if(descTextarea) descTextarea.disabled = true;
		}
	}
}


function previewImage(){
	const imgDisplay = document.querySelector('#img-display');
	imgDisplay.style.display = 'flex';
	targetSrc = this.getAttribute('src');
	imgDisplay.innerHTML = '<i class="fa-regular fa-circle-xmark" id="close"></i><img src="' + targetSrc + '" class="img-display" alt="樣式圖片" />';
    
    let close = document.querySelector('#close');
    close.addEventListener('click', function(){
        imgDisplay.style.display = 'none';
    })
    let container = document.querySelector('.container');
    imgDisplay.addEventListener('click', function(e){
        if(e.target === imgDisplay){
            imgDisplay.style.display = 'none';
        }
    })
}





