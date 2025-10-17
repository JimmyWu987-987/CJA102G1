document.addEventListener("DOMContentLoaded", () => {
	// === 元素取得 ===
	const loading = document.getElementById("loading");
	const main = document.querySelector(".container-main");
	const textEl = document.getElementById("textContent");
	const bar = document.getElementById("progressBar");
	const seed = document.getElementById("seed");
	const germ = document.getElementById("germ");
	const bloom = document.getElementById("bloom");
	const btn = document.getElementById("plantBtn");

	// === Loading 文字循環 ===
	const loadingTexts = [
	  "正在接入折價券伺服器",
	  "正在同步會員資料",
	  "正在整理抽獎池",
	  "正在分析今日運氣",
	  "正在為您準備驚喜"
	];
	let idx = 0;
	setInterval(() => {
	  idx = (idx + 1) % loadingTexts.length;
	  textEl.textContent = loadingTexts[idx];
	}, 800);

	// === 進度條動畫 ===
	let progress = 0;
	const loadingInterval = setInterval(() => {
	  progress += 1;
	  bar.style.width = progress + "%";

	  if (progress >= 100) {
	    clearInterval(loadingInterval);

	    // Loading 淡出 + 移除
	    setTimeout(() => {
	      loading.classList.add("fade-out");
	      loading.addEventListener(
	        "transitionend",
	        () => loading.remove(),
	        { once: true }
	      );
	      // 顯示主畫面
	      setTimeout(() => main.classList.add("show"), 400);
	    }, 200);
	  }
	}, 20); // 每 20ms 加 1%，約 2 秒跑滿
	
	// === 花圖對照表 ===
	const flowerMap = {
	  "轉盤折200": { img: "apple.png"},
	  "轉盤折100": { img: "orange.png"},
	  "沒中獎": { img: "wilt.png"},
	  "已抽過": { img: "lemon.png"},
	};
	// === 重置到種子狀態 ===
	  function resetToSeed() {
	    seed.style.display = "block";
	    seed.style.top = "120px";
	    germ.style.display = "none";
	    germ.style.opacity = "0";
	    bloom.style.display = "none";
	    bloom.style.opacity = "0";
	  }
	  resetToSeed();
  

 
  // === 播種動畫觸發 ===
  btn.addEventListener('click', async () => {
    btn.disabled = true;
    seed.style.top = "160px"; // 掉入土裡

    // 1 秒後 → 發芽
    setTimeout(() => {
      seed.style.display = "none";
      germ.style.display = "block";
      germ.style.opacity = "1";
    }, 1000);

    // 2.5 秒後 → 開花
    setTimeout(async () => {
      germ.style.display = "none";
      bloom.style.display = "block";
      bloom.style.opacity = "1";
      try {
        const res = await fetch('/mem/spin/coupons', { method: 'POST' });
		const data = await res.json(); 
		const text = data.result || data.couponName || data.message || "沒中獎";
		//  從對照表選出設定
		const key = Object.keys(flowerMap).find(k => text.includes(k)) || "default";
		const flowerCfg = flowerMap[key];
		// 自動設定圖片與顏色
		bloom.src = window.BASE_IMG_PATH + flowerCfg.img;
		bloom.style.filter = `drop-shadow(0 0 10px ${flowerCfg.color})`;
		bloom.classList.add(flowerCfg.animation);

        Swal.fire({
          toast: true,
          position: 'bottom-end',
          icon: text.includes('沒中獎') ? 'info' : 'success',
          title: text.includes('沒中獎') ? '再接再厲！' : '🎉 恭喜中獎！',
          text: text,
          showConfirmButton: false,
          timer: 1500,
          background: '#fefcfc',
          color: '#2c3e50',
          timerProgressBar: true
        });

        // 若沒中獎 → 土壤震動 + 花掉落 + 種子重新長出
        if (text.includes('沒中獎')) {
          const garden = document.querySelector('.garden');
          garden.classList.add("shaking");
          setTimeout(() => garden.classList.remove("shaking"), 400);

          bloom.classList.add("falling");
          setTimeout(() => {
            bloom.classList.remove("falling");
            bloom.style.display = "none";
            seed.style.display = "block";
            seed.classList.add("rising");
            setTimeout(() => {
              seed.classList.remove("rising");
              seed.style.top = "120px";
            }, 1000);
          }, 1200);
        }

      } catch {
        Swal.fire({
          icon: 'error',
          title: '系統忙碌中',
          text: '請稍後再試一次！',
          confirmButtonText: '了解'
        });

        bloom.classList.add("falling");
        setTimeout(() => {
          bloom.classList.remove("falling");
          resetToSeed();
        }, 1200);
      }

      btn.disabled = false;
    }, 2500);
  });
});
