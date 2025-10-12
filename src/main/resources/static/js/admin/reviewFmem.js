
window.addEventListener('load', function(){
	
	const statusRadios = document.querySelectorAll('.status-option input');
	statusRadios.forEach(statusRadio => {
		statusRadio.addEventListener('change', showDescRadios);
	})
	
	const descRadios = document.querySelectorAll('.desc-option input');
	descRadios.forEach(descRadio => {
		descRadio.addEventListener('change', openTextarea);
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