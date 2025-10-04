// 圖片預覽+修改
window.addEventListener('load', function(){
    document.querySelectorAll('.pic').forEach(pic => {
		pic.addEventListener('change', fileChange);
	});
	
	certiStatusToWord();
});


function fileChange(e){
	let targetPicBox =  e.target.closest('.picBox');
	let file = e.target.files[0];
	
    const readFile = new FileReader();
    readFile.readAsDataURL(file);
    readFile.addEventListener('load', () => {
        let imageElement = document.createElement('img');
        imageElement.src = readFile.result;
		
        targetPicBox.querySelector('.addPic').style.opacity=0;
        targetPicBox.removeChild(targetPicBox.firstElementChild);
        targetPicBox.prepend(imageElement);
    })            
}


//認證狀態 數字轉成中文顯示
function certiStatusToWord(){
	let certiStatusEle = document.querySelector('.certiStatus');
		
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



