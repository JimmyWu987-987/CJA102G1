// 圖片預覽+修改
window.addEventListener('load', function(){
    document.querySelectorAll('.pic').forEach(pic => {
		pic.addEventListener('change', fileChange);
	});
	
	certiStatusToWord();

});


function fileChange(e){
//	限制檔案大小
	let targetPicBox =  e.target.closest('.picBox');
	let file = e.target.files[0];
	
	const MAX_SIZE_MB = 5;
	if (file){
		const sizeMB = file.size / (1024 * 1024);
		if(sizeMB > MAX_SIZE_MB){
		    alert("檔案太大，請選擇小於 " + MAX_SIZE_MB + " MB 的圖片！");
			this.value = "";
			return;
		}
	} 
	
	const readFile = new FileReader();
    readFile.readAsDataURL(file);
    readFile.addEventListener('load', () => {
        let imageElement = document.createElement('img');
        imageElement.src = readFile.result;
		
		const oldImg = targetPicBox.querySelector('img');
		if(oldImg) oldImg.remove();
		
		const addPicEle = targetPicBox.querySelector('.addPic');
		if(addPicEle) addPicEle.style.opacity=0;
		
        targetPicBox.prepend(imageElement);
    })	
	
	
//	顯示檔案資訊
	let fileInfo = '';
	let fileSizeMB =  Math.ceil(file.size / (1024 * 1024) * 10) / 10;
//	let fileSizeMB = (file.size / (1024 * 1024)).toFixed(1);
	fileInfo += '檔案名稱：' + file.name + '<br>';
	fileInfo += '檔案大小：' + fileSizeMB + 'MB<br>';
	let picInfoEle = e.target.closest('.list-pic').querySelector('.picInfo')
	picInfoEle.innerHTML = fileInfo;
}


//認證狀態 數字轉成中文顯示
function certiStatusToWord(){
	let certiStatusEle = document.querySelector('.certiStatus');
	if(certiStatusEle){
		switch (certiStatusEle.value) {
			case '0':
				certiStatusEle.value = '尚未認證';
				break;
			case '1':
				certiStatusEle.value = '有機認證';
				break;
			default: 
				certiStatusEle.value = '無';
				break;
		}
	}
}



