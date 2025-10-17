
window.addEventListener('load', function(){
	const addToCartForms = document.querySelectorAll('.addToCartForm');
	if(addToCartForms.length > 0){
		addToCartForms.forEach(addToCartForm => {
			const successId = addToCartForm.dataset.success;
			const added = addToCartForm.querySelector('.added');
			if(successId === addToCartForm.querySelector('.proId').value){
				added.classList.remove('hidden');
				added.classList.add('show');
				setTimeout(() => {
					added.classList.remove('show');
					
					setTimeout(() => { 
						added.classList.add('hidden');
					}, 900)
					
				}, 900);
			}
		})
	}
	
})
