// 欄位有輸入資料時，隱藏錯誤訊息
let bankCodeElement = document.querySelector('.bankCode');
let bankAccElement = document.querySelector('.bankAcc');

bankCodeElement.addEventListener('input', function(){
	let errorBankCode = document.querySelector('.error-bankCode');
	
	if(bankCodeElement.value === ''){
		if(errorBankCode) errorBankCode.style.display = 'block';
	} else {
		if(errorBankCode) errorBankCode.style.display = 'none';
	}
	
})

bankAccElement.addEventListener('input', function(){
	let errorBankAcc = document.querySelector('.error-bankAcc');
	
	if(bankAccElement.value === ''){
		if(errorBankAcc) errorBankAcc.style.display = 'block';
	} else {
		if(errorBankAcc) errorBankAcc.style.display = 'none';
	}
	
})

