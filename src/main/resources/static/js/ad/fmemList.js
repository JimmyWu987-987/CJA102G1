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
	
	

//欄位彈窗
const advPopup = document.getElementById('advPopup');
const closeAdv = document.getElementById('closeAdv');
const revUpd   = document.getElementById('revUpd');
const revRemark= document.getElementById('revRemark');
const launStat = document.getElementById('launStat');
const launUpd  = document.getElementById('launUpd');

document.addEventListener('click', e=>{
  const btn = e.target.closest('.detailBtn');
  if (!btn) return;
  revUpd.textContent    = btn.dataset.revupd   || '-';
  revRemark.textContent = btn.dataset.remark   || '-';
  launStat.textContent  = btn.dataset.launstat || '-';
  launUpd.textContent   = btn.dataset.launupd  || '-';
  advPopup.style.display = 'flex';
});

closeAdv.addEventListener('click', ()=>{ advPopup.style.display = 'none'; });
advPopup.addEventListener('click', e=>{ if (e.target === advPopup) advPopup.style.display = 'none'; });

