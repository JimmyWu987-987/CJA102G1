document.addEventListener("DOMContentLoaded", () => {
   const cpnSourceSelect = document.querySelector("[name='cpnSource']");
   const discTypeSelect = document.querySelector("[name='discType']");
   const discValueInput = document.querySelector("[name='discValue']");
   const cpnNameInput = document.querySelector("[name='cpnName']");
   const hint = document.getElementById("discHint");
   const startDateInput = document.getElementById("startDate");

   // 限制開始日期不能早於今天
   const today = new Date().toISOString().split("T")[0];
   if (startDateInput) startDateInput.min = today;

   // 更新折扣提示與範圍
   function updateDiscValueRules() {
     if (discTypeSelect.value === "PERCENTAGE") {
       discValueInput.min = 1;
       discValueInput.max = 99;
       discValueInput.step = 1;
       if (hint) hint.textContent = "請輸入折扣比例（1~99，表示 1折~99折）";
     } else {
       discValueInput.min = 1;
       discValueInput.max = 99999;
       discValueInput.step = 1;
       if (hint) hint.textContent = "請輸入折抵金額（例如：100、200）";
     }
   }

   // 自動更新折價券名稱
   function updateCpnName() {
     const sourceText = cpnSourceSelect.options[cpnSourceSelect.selectedIndex]?.text.trim() || "";
     const discValue = discValueInput.value.trim();

     if (sourceText && discValue) {
       cpnNameInput.value = (discTypeSelect.value === "PERCENTAGE")
         ? `${sourceText}${discValue}折`
         : `${sourceText}折${discValue}`;
     } else {
       cpnNameInput.value = "";
     }
   }

   // 綁定事件
   discTypeSelect.addEventListener("change", () => {
     updateDiscValueRules();
     updateCpnName();
   });
   cpnSourceSelect.addEventListener("change", updateCpnName);
   discValueInput.addEventListener("input", updateCpnName);

   // 初始化
   updateDiscValueRules();
   updateCpnName();
 });