
window.addEventListener('load', function(){
	
	const activeTab = sessionStorage.getItem("activeTab");
	
	let statusSearchSelect = document.querySelector('.status-search-select');
	
	if(statusSearchSelect){
		if(activeTab) {
			statusSearchSelect.value = activeTab;
			searchByStatus();			
		} else {
			statusSearchSelect.value = 99;
			searchByStatus();
		}
	}

})





	

function searchByStatus(){
	
	// 搜尋 審核狀態
	let statusSearchSelect = document.querySelector('.status-search-select');
	let hasResult = false;
	let statusSearchValue = statusSearchSelect.value;
	let allStatus = document.querySelectorAll('form .status-text > div');
	
			let activeTab = document.querySelector('.status-search-select').value;
			sessionStorage.setItem("activeTab", activeTab);
	
	// 99: 顯示全部
	if(statusSearchValue === '99') {
		document.querySelectorAll('tbody tr').forEach(row => row.style.display = 'table-row');
		hasResult = true;
	} else {	
		// 非99，先隱藏全部，符合條件再打開
		document.querySelectorAll('tbody tr').forEach(row => row.style.display = 'none');
	}
	
	allStatus.forEach(status => {
//		符合條件的顯示出來
		if (statusSearchValue === status.dataset.status){
			status.closest('tr').style.display = 'table-row';
			hasResult = true;
		}
	})
	offResultMsg();
	showResultMsg(hasResult);
}





