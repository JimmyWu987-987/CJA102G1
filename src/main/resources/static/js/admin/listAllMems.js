
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
	
	switchEditMode();
	
	//欄位顯示切換
	let checkboxes = document.querySelectorAll('.switch-list input[type="checkbox"]');
	checkboxes.forEach(checkbox => 
		checkbox.addEventListener('change', switchDisplayByCheckbox));
	
	if(statusSearchSelect) statusSearchSelect.addEventListener('change', searchByStatus);
	
	let btnFuzzySearch = document.querySelector('.btn-fuzzy-search');
	btnFuzzySearch.addEventListener('click', fuzzySearch);

	let fuzzySearchInput = document.querySelector('.fuzzy-search');
	fuzzySearchInput.addEventListener('keydown', function(e){
		if(e.key === "Enter") fuzzySearch(); //按enter = 搜尋
	})

//	後台管理總覽 更改帳號狀態前 跳出alert視窗
	let fmemForms = document.querySelectorAll(".fmemForm");
	if(fmemForms){
		fmemForms.forEach(fmemForm => {
			 fmemForm.addEventListener('submit', showAlert);
		})
	}
	
	let memForms = document.querySelectorAll(".memForm");
	if(memForms){
		memForms.forEach(memForm => {
			 memForm.addEventListener('submit', showAlert);
		})
	}
})


function showAlert(e) {
    e.preventDefault();
    Swal.fire({
        title: '確定更改帳號狀態',
		text: '送出後系統會自動發送email通知會員',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: '確定',
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


function switchEditMode(){
	
	btnEditBtns = document.querySelectorAll('.btn-edit');
	btnSaveBtns = document.querySelectorAll('.btn-save');
	
	AllStatusText = document.querySelectorAll('.status-text');
	AllStatusSelect = document.querySelectorAll('.status-select');
	
	
	
	// 點"編輯"按鈕 => 進入編輯模式
	btnEditBtns.forEach(btnEdit => {
		btnEdit.addEventListener('click', function(e){
			// 一次只能編輯一個，若非編輯對象，關閉編輯功能
			AllStatusText.forEach(statusText => {
				if(statusText) statusText.style.display = "block";
			} )
			AllStatusSelect.forEach(statusSelect => {
				if(statusSelect) statusSelect.style.display = "none";
			})
			
			// 開啟編輯對象的編輯功能
//			開啟儲存按鈕 隱藏編輯按鈕
			let form = e.target.closest('form');
			let btnEdit = form.querySelector('.btn-edit');
			let btnSave = form.querySelector('.btn-save');
			if(btnEdit) btnEdit.style.display = "none";
			if(btnSave) btnSave.style.display = "block";
			
			targetSelect = form.querySelector('.status-select');
			targetText = form.querySelector('.status-text');
			if(targetSelect) targetSelect.style.display = "block";			
			if(targetText) targetText.style.display = "none";			
		})
	})

	btnSaveBtns.forEach(btnSave => { //送出
		btnSave.addEventListener('click', function(e){
//			e.preventDefault();
			//送出前，記住目前的查詢頁面(審核狀態篩選)，重導後要回到原查詢頁面
//			let activeTab = document.querySelector('.status-search-select').value;
//			sessionStorage.setItem("activeTab", activeTab);
//			e.target.closest('form').submit();
			
//			隱藏儲存按鈕 開啟編輯按鈕
//			let form = e.target.closest('form');
//			let btnEdit = form.querySelector('.btn-edit');
//			let btnSave = form.querySelector('.btn-save');
//			if(btnEdit) btnEdit.style.display = "block";
//			if(btnSave) btnSave.style.display = "none";
		})
	})
}



function switchDisplayByCheckbox(){
	let selector = `.table .${this.id}`;
	targetDatas = document.querySelectorAll(selector);
	
	if(targetDatas) {
		if(this.checked){
			targetDatas.forEach(targetData => {
				targetData.style.display = 'table-cell';
			})
	    } else {
			targetDatas.forEach(targetData => {
				targetData.style.display = 'none';
			})
	    }
	}
}


	

function searchByStatus(){
	
	// 搜尋 審核狀態
	let statusSearchSelect = document.querySelector('.status-search-select');
	let hasResult = false;
	let statusSearchValue = statusSearchSelect.value;
	let allStatus = document.querySelectorAll('form .status-select');
	
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
		if (statusSearchValue === status.value){
			status.closest('tr').style.display = 'table-row';
			hasResult = true;
		}
	})
	offResultMsg();
	showResultMsg(hasResult);
}

// 查詢: 會員編號或姓名
function fuzzySearch(){
	offResultMsg();
	let hasResult = false;
	let fuzzySearchValue = document.querySelector('.fuzzy-search').value.trim();
	document.querySelectorAll('tbody tr').forEach(row => row.style.display = 'none');
	
	if(!isNaN(fuzzySearchValue)){
		let allIds  = document.querySelectorAll('.myId span');
		allIds.forEach(id => {
	//		符合條件的 顯示出來(精準查詢)
	//		if (id.textContent.includes(fuzzySearchValue)){
			if (id.textContent === fuzzySearchValue){
				id.closest('tr').style.display = 'table-row';
				hasResult = true;
			}
		})
	}
	
	let allNames = document.querySelectorAll('.myName span');
	allNames.forEach(name => {
//		符合條件的 顯示出來(模糊查詢)
		if (name.textContent.includes(fuzzySearchValue)){
			name.closest('tr').style.display = 'table-row';
			hasResult = true;
		}
	})
	
	showResultMsg(hasResult);
}

function showResultMsg(hasResult){
	// 顯示查無結果
	if(!hasResult){
		resultMsgTr = document.querySelector('tr.resultMsg');
		if(resultMsgTr) {
			const colCount = document.querySelectorAll('table thead th:not([style*="display: none"])').length;
			resultMsgTr.style.display = 'table-row';
			resultMsgTr.innerHTML = `<td class="resultCell">查無結果</td>`
			resultMsgTr.querySelector('.resultCell').setAttribute('colspan', colCount);
		}
	}
}

function offResultMsg(){
	document.querySelector('tr.resultMsg').style.display = 'none';
}


