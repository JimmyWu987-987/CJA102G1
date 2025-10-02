


// 地址4欄位有輸入資料時，隱藏錯誤訊息(zipcode, city, dist, addr互相影響)
let cityElement = document.querySelector('.city');
let distElement = document.querySelector('.dist');

cityElement.addEventListener('change', function(){
	let errorDistElement = document.querySelector('.error-dist');
	let errorCityElement = document.querySelector('.error-city');
	
	if(errorDistElement) errorDistElement.style.display = 'block';
	
	if(cityElement.value === ''){
		if(errorCityElement) errorCityElement.style.display = 'block';
	} else {
		if(errorCityElement) errorCityElement.style.display = 'none';
	}
})
distElement.addEventListener('change', function(){
	let errorDist = document.querySelector('.error-dist');
	let errorZipcode = document.querySelector('.error-zipcode');
	
	if(distElement.value === ''){
		if(errorDist) errorDist.style.display = 'block';
		if(errorZipcode) errorZipcode.style.display = 'block';
	} else {
		
		if(errorDist) errorDist.style.display = 'none';
		if(errorZipcode) errorZipcode.style.display = 'none';
	}
	
})


// "error-reg此帳號有人註冊過"的錯誤訊息，跟其他欄位顯示時機不太一樣，另外寫
let accElement = document.querySelector('.acc');
let errorRegElement = accElement.closest('li').querySelector('.error-reg');
accElement.addEventListener('input', function(){
	if(errorRegElement) errorRegElement.style.display = 'none';
})



// 欄位有輸入資料時，隱藏錯誤訊息
let inputElements = document.querySelectorAll('ul input');
inputElements.forEach(inputElement => {
	inputElement.addEventListener('input', function(){
		if(inputElement.value.trim()){
			switchInputError(inputElement, 'none');
		} else {
			switchInputError(inputElement, 'inline-block');
		}
	})
});




function switchInputError(inputElement, displayValue){
	let errorElement = inputElement.closest('li').querySelector('.error');
	if(errorElement){
		errorElement.style.display = displayValue;
	}
	
	// 因為兩個input欄位(zipcode和addr)放在同一個li底下，要另外寫
	if(inputElement.classList.contains('zipcode')){
		let errorZipcodeElement = inputElement.closest('li').querySelector('.error-zipcode');
		if(errorZipcodeElement){
			errorZipcodeElement.style.display = displayValue;					
		}
	}
	if(inputElement.classList.contains('addr')){
		let errorAddrElement = inputElement.closest('li').querySelector('.error-addr');
		if(errorAddrElement){
			errorAddrElement.style.display = displayValue;				
		}
	}
}



// ------------------------Email自動填入提示-----Start-------------------

let domainData = [];
let emailElement = document.querySelector('.email');

window.addEventListener('load', function () {
    fetch('/data/domainData.json')
        .then(res => {
            if (!res.ok) console.log('fail to load domainData.json');
            return res.json();
        }).then(data => {
            domainData = data;
            emailElement.addEventListener('input', showEmailSuggestions);
        })
})

let emailSuggestions = document.querySelector('#email-suggestions');
function showEmailSuggestions() {
    let memEmailInput = emailElement.value;
    let emailSuggestionsData = '';
    let atCount = memEmailInput.split('@').length - 1;  //計算@數量
    
    // @數量不等於1個
    if (atCount !== 1){
        emailSuggestions.innerHTML = '';
    }

    // EX: 輸入 abc123@ 時
    // 輸入字串結尾是@ 且只有1個@
    if(memEmailInput.endsWith('@') && atCount===1){
        let memEmailInputSub = memEmailInput.slice(0, -1);
        // 只顯示前3個
        for(let i=0; i<3; i++){
            let emailSuggestionsStr = memEmailInputSub + domainData[i].domain;
            emailSuggestionsData += '<option value="' + emailSuggestionsStr + '"></option>';
        }
        emailSuggestions.innerHTML = emailSuggestionsData;

    } else {
        // EX: 輸入 abc123@gxxxx 時
        // 顯示@gxxxx字元提示
        let atIndex = memEmailInput.indexOf('@'); //@在字串中的位置
        let memEmailInputLength = memEmailInput.length; //輸入的字串總長度
        let memEmailInputAfterAt = memEmailInput.slice(atIndex, memEmailInputLength); //@xxx
        let inputAfterAtLength = memEmailInputLength - atIndex;  // 輸入的字串 @後有幾個字元(含@)
        
        emailSuggestionsData = null;
        let memEmailInputSub = memEmailInput.slice(0, - inputAfterAtLength);
        let index = 0;
        for(let i=0; i<domainData.length; i++){
            let dataBeforeAt = domainData[i].domain.slice(0, inputAfterAtLength);
            if (memEmailInputAfterAt === dataBeforeAt && index<3){
                let emailSuggestionsStr = memEmailInputSub + domainData[i].domain;
                emailSuggestionsData += '<option value="' + emailSuggestionsStr + '"></option>';
                index++;
            }
            //如果已輸入完整email網域，不再顯示提示
            if(memEmailInputAfterAt === domainData[i].domain){
                emailSuggestionsData = '';
            }
        }
        emailSuggestions.innerHTML = emailSuggestionsData;
    }
}
    
//------------------------Email自動填入提示------End-----------------------



let cityData = [];
let zipcodeElement = document.querySelector('.zipcode');

window.addEventListener('load', function(){
	loadJson();
	
	
	
});


async function loadJson(){
        try {
        const res = await fetch('/data/CityCountyData.json')
        if(!res.ok) console.log('fail to load CityCountyData.json');
        
        const data = await res.json();
        cityData = data;
        
        // 載入city選單
        loadCitySelect();
        
        // 設定上次選的city
        const savedCity = '';
        if (savedCity) {
            citySelect.value = savedCity;
            // 手動載入dist
            if (savedCity !== '--請選擇--') {
                loadDistSelect();
                // 設定上次選的dist
                const savedDist = '';
                if (savedDist) {
                    distSelect.value = savedDist;
                }
            }
        }
        
        // 如果送出表單驗證失敗，後端儲存已填過的欄位放到前端，用JS去抓郵遞區號->選對應的縣市鄉鎮
		if(zipcodeElement.value != ''){
			loadCityCountyByZipcode();
		}
		
        // 一次只能綁定一個事件 
        // input觸發包含: 鍵盤輸入、滑鼠複製貼上、清除欄位、語音輸入、自動填入(瀏覽器自動帶入)、手機選字
        if (zipcodeElement) {
            zipcodeElement.addEventListener('input', function(){
                loadCityCountyByZipcode();
            });
        } else {
            console.log('找不到 .zipcode 元素');
        }
        
        } catch (error) {
        console.error('載入 JSON 發生錯誤:', error);
        }
}


let citySelect = document.querySelector('.city');

// city縣市 載入JSON檔做成下拉選單
function loadCitySelect(){
    let jsonCityData = `<option value="" class="city-option" id="city-option">請選擇</option>`;
    for(let i=0; i<cityData.length; i++){
        let cityName = cityData[i].CityName;
        // 改用字串串接，避免模板字串問題
        jsonCityData += '<option value="' + cityName + '" class="city-option" data-city-index="' + i + '">' + cityName + '</option>';
    }

    citySelect.innerHTML = jsonCityData;

    // 只要縣市欄位變動 就會重新載入區域選單
    // change: 手動點選新的選項、鍵盤選擇(上下鍵+enter)
    citySelect.addEventListener('change', function(){    
        if(citySelect.value){
			loadDistSelect();
		}
    })
}


let distSelect = document.querySelector('.dist');
let citySelectedIndex = null;
// dist區域 載入JSON檔做成下拉選單
function loadDistSelect(){
    // 取得city欄位資料在JSON檔中的索引值
    citySelectedIndex =  citySelect.options[citySelect.selectedIndex].dataset.cityIndex;

    
    let jsonDistData = `<option value="" class="dist-option" id="dist-option">請選擇</option>`;
    let targetCity = cityData[citySelectedIndex];
    for(let i=0; i<targetCity.AreaList.length; i++){
        let areaName = targetCity.AreaList[i].AreaName;
        let zipCode = targetCity.AreaList[i].ZipCode;
        // 改用字串串接，避免模板字串問題
        jsonDistData += '<option value="' + areaName + '" class="dist-option" id="dist-option" data-zipcode="' + zipCode + '">' + areaName + '</option>';
    }
    distSelect.innerHTML = jsonDistData;
    
    distSelect.addEventListener('change', function(){
    	if(distSelect.value){ //************ */
			loadSeletedZipcode();
		}
    })
}



function loadSeletedZipcode(){
    zipcodeSelected = distSelect.options[distSelect.selectedIndex].dataset.zipcode;
    zipcodeElement.value = zipcodeSelected;
}



function loadCityCountyByZipcode(){
    let zipcodeInput =  zipcodeElement.value;
    let cityIndex = null;
    let distIndex = null;
    for(let i=0; i<cityData.length; i++){
        for(let j=0; j<cityData[i].AreaList.length; j++){
            let zipcodeCompared = cityData[i].AreaList[j].ZipCode;
            if(zipcodeInput === zipcodeCompared){
                cityIndex = i;
                distIndex = j;
                break;
            }
        }
        if(cityIndex != null) break;
    }

    let listAddr = document.querySelector('.list-addr');
    if(cityIndex !== null && distIndex !== null){
        // 如果有errorMsg 刪掉
        let error = listAddr.querySelector('.js-error-zipcode');
        if (error) {
            error.remove();
        }

        let zipcode = cityData[cityIndex].AreaList[distIndex].ZipCode;
        let city = cityData[cityIndex].CityName;
        let dist = cityData[cityIndex].AreaList[distIndex].AreaName;

        loadCitySelect();
        // 如果select裡有這個option 會自動在option加上selected
        citySelect.value = city;

        loadDistSelect();
        distSelect.value = dist;
		
		let errorCityElement = document.querySelector('.error-city');
		let errorDistElement = document.querySelector('.error-dist');
		if(errorCityElement) errorCityElement.style.display = 'none';//****隱藏後端的錯誤訊息 */	
		if(errorDistElement) errorDistElement.style.display = 'none';//****隱藏後端的錯誤訊息 */
        
    } else {
	//  郵遞區號輸入3碼以上，才出現驗證錯誤訊息
        let error = listAddr.querySelector('.js-error-zipcode');
        if(zipcodeInput.length>=3){
            // 如果有errorMsg 刪掉
            if (error) error.remove();
            
            // 再加新的errorMsg上去
            let errorMsg = `<span class="js-error-zipcode" style="color:red">※查無對應縣市鄉鎮，請輸入數字3碼</span>`;
            listAddr.insertAdjacentHTML('beforeend' ,errorMsg);
        } else if(zipcodeInput.length === 0){
			// 輸入空值時，如果有errorMsg 刪掉
            if (error) error.remove();
		}
    }
}
