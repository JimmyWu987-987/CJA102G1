// 等整個頁面載入完畢再開始執行
document.addEventListener("DOMContentLoaded", () => {
  const seed = document.getElementById('seed');
  const germ = document.getElementById('germ');
  const bloom = document.getElementById('bloom');
  const btn = document.getElementById('plantBtn');

  // 初始狀態重設函式
    function resetToSeed() {
      seed.style.display = "block";
      seed.style.top = "120px";
      germ.style.display = "none";
      germ.style.opacity = "0";
      bloom.style.display = "none";
      bloom.style.opacity = "0";
    }
	// 確保是種子狀態
	 resetToSeed();
	 
  btn.addEventListener('click', async () => {
    btn.disabled = true;
    seed.style.top = "160px"; // 掉入土裡

    // 1秒後種子藏起來 → 發芽
    setTimeout(() => {
      seed.style.display = "none";
      germ.style.display = "block";
      germ.style.opacity = "1";
    }, 1000);

    // 2.5秒後長花
    setTimeout(() => {
      germ.style.display = "none";
      bloom.style.display = "block";
      bloom.style.opacity = "1";
    }, 2500);

    // 4秒後顯示抽籤結果
    setTimeout(async () => {
      try {
        const res = await fetch('/mem/spin/coupons', { method: 'POST' });
        const text = await res.text();

		Swal.fire({
			toast: true,              // 啟用 toast 模式（右上角小提示）
			  position: 'bottom-end',      // 出現位置（可改 top-start、bottom-end）
			  icon: text.includes('沒中獎') ? 'info' : 'success',
			  title: text.includes('沒中獎') ? '再接再厲！' : '🎉 恭喜中獎！',
			  text: text,               // 顯示內容文字
			  showConfirmButton: false, // 不顯示「確定」按鈕
			  timer: 1000,              // 自動關閉時間
			  background: '#fefcfc',
			  color: '#2c3e50',
			  timerProgressBar: true    // 加上時間進度條（可有可無）
		     });
			 if (text.includes('沒中獎')) {
				const garden = document.querySelector('.garden');
				garden.classList.add("shaking");
				setTimeout(() => garden.classList.remove("shaking"), 400);
				bloom.classList.add("falling");//lottery.css  .falling
				
				setTimeout(() => {
					bloom.classList.remove("falling");
					bloom.style.display = "none";
					   
					 seed.style.display = "block";
					 seed.classList.add("rising");
					 setTimeout(() => {
					     seed.classList.remove("rising");
					     seed.style.top = "120px";
					   }, 1000);
					 }, 1200);}
      } catch {
		// 錯誤處理
		  Swal.fire({
		    icon: 'error',
		    title: '系統忙碌中',
		    text: '請稍後再試一次！',
		    confirmButtonText: '了解',
		    customClass: {
		      popup: 'custom-swal',
		      title: 'custom-swal-title'
		    }
		  });
		  resetToSeed(); 
	  }
      btn.disabled = false;
    }, 4000);
  });
  });