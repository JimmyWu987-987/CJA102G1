
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
})

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





