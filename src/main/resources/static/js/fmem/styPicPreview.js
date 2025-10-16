

const stySelect = document.querySelector('.stySelect');
const styPic = document.querySelector('.sty img');

window.addEventListener('load', function(){
	// 圖片跟著select變動
	stySelect.addEventListener('change', function(){
		if(stySelect.value === '1'){
			styPic.src = '/images/sql/sty/1.png';
		} else if(stySelect.value === '2'){
			styPic.src = '/images/sql/sty/2.png';
		} else if(stySelect.value === '3'){
			styPic.src = '/images/sql/sty/3.png';
		}
	})
	
	const imgNail = document.querySelector('.img-nail');
	imgNail.addEventListener('click', previewImage);
	
}) 

// 圖片放大
function previewImage(){
	console.log("click");
	const imgDisplay = document.querySelector('#img-display');
	imgDisplay.style.display = 'flex';
	targetSrc = this.getAttribute('src');
	imgDisplay.innerHTML = '<i class="fa-regular fa-circle-xmark" id="close"></i><img src="' + targetSrc + '" class="img-display" alt="樣式圖片" />';
    
    let close = document.querySelector('#close');
    close.addEventListener('click', function(){
        imgDisplay.style.display = 'none';
    })

}

