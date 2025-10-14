

let bankCodeData = [];
let bankCodeSelect = document.querySelector('.bankCode');

window.addEventListener('DOMContentLoaded', function () {
	const savedBankCode = bankCodeSelect.dataset.saved;
				
    fetch('/data/taiwanBankCode.json')
        .then(res => {
            if (!res.ok) console.log('fail to load taiwanBankCode.json');
            return res.json();
        }).then(data => {
            bankCodeData = data;
			loadBankCode();
			if(savedBankCode){
				bankCodeSelect.value = savedBankCode;
				console.log('設定後的 value:', bankCodeSelect.value);
			}
			
			
        })
})

function loadBankCode(){
	let jsonbankCodeData = '<option value="">請選擇</option>';
	
	for(let i=0; i<bankCodeData.length; i++){
		const code = bankCodeData[i].code;
		const name = bankCodeData[i].name;
		
		jsonbankCodeData += `<option value="${code}">${code}-${name}</option>`;
	}
	bankCodeSelect.innerHTML = jsonbankCodeData;
	
}