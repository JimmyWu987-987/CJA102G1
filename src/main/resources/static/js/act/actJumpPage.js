const iframe = document.getElementById('memRegistration');
iframe.addEventListener('load', function() {

	const href = iframe.contentWindow.location.href;
	// 成功時讓整頁跳轉
	if (href !== 'about:blank' && href.indexOf('/mem/reg/pay') !== -1) {
		window.top.location.href = href;
	}
	// 失敗時會回報名表單不會跳整頁
});/**
 * 
 */