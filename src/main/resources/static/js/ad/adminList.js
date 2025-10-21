// 下拉切換
document.getElementById('adType').addEventListener('change', e=>{
  location.href = e.target.value;
});

// 圖片放大
const outSide = document.getElementById('outSide');
const zoomImg = document.getElementById('zoomImg');
document.addEventListener('click', e=>{
  const img = e.target.closest('.adImg');
  if (!img) return;
  outSide.style.display = 'block';
  zoomImg.src = img.src;
});
document.getElementById('closeButton').addEventListener('click', ()=>{
  outSide.style.display = 'none';
  zoomImg.src = '';
});

//商品審核畫面顯示
document.addEventListener('click', function (e) {
	const btn = e.target.closest('.showProAdReview');
	if (!btn) return;
  e.preventDefault();
  document.getElementById('proAdRevView').style.display = 'block';
});

//關閉商品審核畫面顯示
document.addEventListener('click', (e) => {
  const btn = e.target.closest('.closeRev');
  if (!btn) return;
  location.href = '/admin/proAd/list';
});

//人物文字顯示
const quotes = [
    "Java是簡單的。",
    "最喜歡看認真的你。",
    "記得喝水!",
    "快到發薪日了，加油!",
    "你今天也要很正面哦!",
    "我愛我的工作。",
  ];

  const personImg = document.getElementById("personImg");
  const bubbleText = document.getElementById("bubbleText");

  personImg.addEventListener("click", () => {
    const randomIndex = Math.floor(Math.random() * quotes.length);
    bubbleText.textContent = quotes[randomIndex];
  });
  
  
  //審核完畢特效
  window.addEventListener("load", function() {
		const successMsg = document.body.dataset.success;
		if (successMsg != null) {
			console.log(successMsg);
			if (successMsg  === "審核完成") {
				Swal.fire({
					icon : 'success',
					title : successMsg,
					text : "工作辛苦了!",
					showConfirmButton : false,
					customClass : {
						icon : 'custom-icon',
						popup : 'custom-swal',
						title : 'custom-swal-title',
						htmlContainer : 'custom-swal-content'
					}
				});

			}
			if (successMsg  === "修改完成") {
				Swal.fire({
					icon : 'success',
					title : successMsg,
					text : "工作辛苦了!",
					showConfirmButton : false,
					customClass : {
						icon : 'custom-icon',
						popup : 'custom-swal',
						title : 'custom-swal-title',
						htmlContainer : 'custom-swal-content'
					}
				});

			}

		}
	});
  
  
  