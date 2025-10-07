

// 水果破格-動畫
const apple = document.querySelector("#apple");
const lemon = document.querySelector("#lemon");

window.addEventListener('load', function(){
    let scrollTop = Math.round(window.scrollY); //四捨五入到整數  
    let windowH = window.innerHeight; 
    let conceptElement = document.querySelector('.concept');
    let conceptElementY = conceptElement.getBoundingClientRect().top + scrollTop;

    let shown = false;
    window.addEventListener('scroll', function(){
        scrollTop = Math.round(window.scrollY); 
        // 數據區塊在顯示區塊裡才顯示
        if(scrollTop >= conceptElementY - windowH){
            if(shown === false){
                displayConcept();
                shown = true;
            }
        } 
    })

    // 摸到水果會有動畫
    let fruitImgs = document.querySelectorAll('.fruit-pic img');
    fruitImgs.forEach((fruitImg) => {

        let isAnimating = false;
        fruitImg.closest('.concept').addEventListener('mouseover', function(){
            if(isAnimating) {
                return;
            } else {
                isAnimating = true;
                let fruitImgT = gsap.timeline({ 
                    defaults: { duration: 0.3, ease: "cubic-bezier(.36,.07,.19,.97)" },
                    onComplete:() => {
                        isAnimating = false;
                    }
                });
                fruitImgT
                    .to(fruitImg, {rotate: 10})
                    .to(fruitImg, {rotate: -10})
                    .to(fruitImg, {rotate: 10})
                    .to(fruitImg, {rotate: -10})
                    .to(fruitImg, {rotate: 0})
            }
        })
    })
});


function displayConcept(){
    const apple = document.querySelector("#apple");
    const lemon = document.querySelector("#lemon");
    let appleX = apple.getBoundingClientRect().x;
    let lemonX = lemon.getBoundingClientRect().x;
    let deltaX = lemonX - appleX;
    
    const appleT = gsap.timeline();
    const lemonT = gsap.timeline();
    appleT.to("#apple img", {x: deltaX, duration: 2, ease: "back.out(1.7)", rotate: 720, delay: .8})
            .to("#apple img",{x: 0, duration: 2.5, ease: "back.out(1.7)", rotate: 0});

    gsap.from("#orange img", {top: "-180%", duration: 3.5, ease: "bounce.out", rotate: 360, delay: .3});

    lemonT.to("#lemon img", {x: -deltaX, duration: 2, ease: "back.inOut(1.7)", rotate: 720, delay: .8})
            .to("#lemon img", {x: 0, duration: 2.5, ease: "back.inOut(1.7)", rotate: 0});
}


// 數據區塊-動畫
window.addEventListener('load', function(){
    let scrollTop = Math.round(window.scrollY); //四捨五入到整數  
    let documentH = document.documentElement.scrollHeight; 
    let windowH = window.innerHeight; 
    
    let statsElement = document.querySelector('.stats');
    let introElement = document.querySelector('.intro');
    let statsElementY = statsElement.getBoundingClientRect().top + scrollTop;
    let introElementY = introElement.getBoundingClientRect().top + scrollTop;

    window.addEventListener('resize', function(){
        windowH = window.innerHeight; 
        statsElementY = statsElement.getBoundingClientRect().top + scrollTop;
        introElementY = introElement.getBoundingClientRect().top + scrollTop;
    })

    let shown = false;
    // 如果視窗高度"涵蓋"顯示區塊 直接顯示數字
    if(windowH > statsElementY){
        displayStat();
        shown = true;
    }
    
    // 如果視窗高度"沒涵蓋"顯示區塊，但位於顯示區塊內 直接顯示數字
    if((scrollTop >= statsElementY - windowH) && (scrollTop < introElementY)){
        if(shown === false){
            displayStat();
            shown = true;
        }
    }

    window.addEventListener('scroll', function(){
        scrollTop = Math.round(window.scrollY); //四捨五入到整數
        // 數據區塊在顯示區塊裡才顯示
        if((scrollTop >= statsElementY - windowH) && (scrollTop < introElementY)){
            // console.log('在顯示區域裡');
            // 還沒顯示過才跑
            if(shown === false){
                displayStat();
                shown = true;
            }
        } else {
            // console.log('離開顯示區域');
            shown = false;
        }   
    })
})

function displayStat(){
    // GSAP 淡入效果
    gsap.to(".stat", { duration: 1, opacity: 1, y: 0, stagger: 0.2});

    // 數字動畫
    document.querySelectorAll(".stat-number").forEach(el => {
        let target = +el.getAttribute("data-target");
        gsap.fromTo(el, 
            { innerText: 0 }, 
            { innerText: target, duration: 1.3, snap: { innerText: 1 },
            ease: "power1.out", delay: 0.1 }
        );
    });
}


//banner車-動畫
window.addEventListener('load', driveCar);
window.addEventListener('resize', function(){
    // 視窗resize時，車維持在固定位子
    let bannerW = document.querySelector('.banner').clientWidth;
    gsap.to('#car', { duration: 0, x: -(bannerW*0.5)})
});

function driveCar(){
    let bannerW = document.querySelector('.banner').clientWidth;
    let carT = gsap.timeline();
    carT.to('#car', { duration: 2, x: -(bannerW*0.5), ease: 'easeOutSine' , delay: 0.5})
}