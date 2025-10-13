document.addEventListener('DOMContentLoaded', function () {
    const track = document.querySelector('.carousel-track');
    const items = document.querySelectorAll('.carousel-item');
    const prevBtn = document.querySelector('.carousel-btn.prev');
    const nextBtn = document.querySelector('.carousel-btn.next');
    const dots = document.querySelectorAll('.carousel-dots .dot');
    let index = 0;
    let interval = null;
	
	// 只有一張圖就隱藏按鈕和圓點
	if (items.length <= 1) {
	    prevBtn.style.display = 'none';
	    nextBtn.style.display = 'none';
	    if (dotsContainer) dotsContainer.style.display = 'none';
	    return; // 不需要初始化輪播
	}

    function updateCarousel() {
        track.style.transform = `translateX(-${index * 100}%)`;
        dots.forEach(dot => dot.classList.remove('active'));
        if (dots[index]) dots[index].classList.add('active');
    }

    prevBtn.addEventListener('click', () => {
        index = (index - 1 + items.length) % items.length;
        updateCarousel();
        resetInterval();
    });

    nextBtn.addEventListener('click', () => {
        index = (index + 1) % items.length;
        updateCarousel();
        resetInterval();
    });

    dots.forEach((dot, i) => {
        dot.addEventListener('click', () => {
            index = i;
            updateCarousel();
            resetInterval();
        });
    });

    function startAutoPlay() {
        interval = setInterval(() => {
            index = (index + 1) % items.length;
            updateCarousel();
        }, 5000);
    }

    function resetInterval() {
        clearInterval(interval);
        startAutoPlay();
    }

    updateCarousel();
    startAutoPlay();
});