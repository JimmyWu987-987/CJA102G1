 // 下拉切換
    document.getElementById('adType').addEventListener('change', e=>{
      location.href = e.target.value;
    });    
    
  // 	圖片控制
function doFirst() {
	document.querySelector('#theFile').onchange = fileChange
}
function fileChange(e) {
	let file = e.target.files[0]
	const reader = new FileReader()
	reader.readAsDataURL(file)
	reader.addEventListener('load', () => {
		// 建立 <img>
		let image = document.createElement('img')
		image.src = reader.result

		let box = document.querySelector('.box')
		// 移除舊圖片（保留 input）
		let oldImg = box.querySelector('img')
		if (oldImg) box.removeChild(oldImg)

		// 隱藏 "+"
		document.querySelector('.add').style.opacity = 0

		// 加入新圖片
		box.appendChild(image)
	})
}
window.addEventListener('load', doFirst)

//廣告費用：選單變更
const duration = document.getElementById("duration");
const show = document.getElementById("adFeeShow");
const hide = document.getElementById("adFee");

function updateFee() {
	let feeS = "$0", fee = 0;
	switch (duration.value) {
		case "0": feeS = "$1,000"; fee = 1000; break;
		case "1": feeS = "$2,000"; fee = 2000; break;
		case "2": feeS = "$3,000"; fee = 3000;
	}
	show.value = feeS; // 有 $
	hide.value = fee;  // 純數字
}
duration.addEventListener("change", updateFee); updateFee();


// 結束日期
const start = document.getElementById('actAdStart');
const end   = document.getElementById('actAdEnd');

function updateEnd() {

	  // 把 "YYYY-MM-DD" 轉成 Date（本地時區，避免時差問題）
	  const [y, m, d] = start.value.split('-').map(Number);
	  const dt = new Date(y, m - 1, d);

	  // 0=30天, 1=60天, 2=90天
	  let addDays = 30;
	  if (duration.value === '1') addDays = 60;
	  else if (duration.value === '2') addDays = 90;

	  dt.setDate(dt.getDate() + addDays);
	  end.valueAsDate = dt;
}

start.addEventListener("change", updateEnd);
duration.addEventListener("change", updateEnd);
updateEnd();


//閱讀規則
const checkbox = document.getElementById('checkbox');
const openRule  = document.getElementById('openRule');
const rulebody   = document.getElementById('rulebody');
const closeRule = document.getElementById('closeRule');
const submitBtn = document.getElementById('submit-btn');


openRule.addEventListener('click', (e) => {
  e.preventDefault();
  rulebody.style.display = 'block';

	fetch('/json/ad/fmemApplyAds.json')
    .then(res => res.json())
  	.then(data => {
    const box = document.getElementById('ruleContent');

    let listHtml = '';
    for (let i = 0; i < data.rules.length; i++) {
       listHtml += `<li style="margin-bottom:12px;">${data.rules[i]}</li>`;
    }
		
    let html = `
      <h4 style="text-align:center;margin:0; font-size:25px">${data.intro}</h4>
      <p style="text-align:center;margin:6px 0 10px">${data.title}</p>
			<ol>${listHtml}</ol>
      `;
    box.innerHTML = html;
  });
});

closeRule.addEventListener('click',(e)=> {
  e.preventDefault();
  rulebody.style.display = 'none';
	checkbox.disabled = false;
})

checkbox.addEventListener('change', () => {
  submitBtn.disabled = !checkbox.checked;  // 勾了才能提交
  if (checkbox.checked) {
	    submitBtn.style.backgroundColor = '#2d6cdf';  // 勾選時便藍色
	  } else {
	    submitBtn.style.backgroundColor = '#ccc';     
	  }
})