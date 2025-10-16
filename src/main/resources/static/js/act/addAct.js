document.addEventListener("DOMContentLoaded", function () {
    const actStartInput = document.getElementById("actStart");
    const actEndInput = document.getElementById("actEnd");
    const actMainImgInput = document.getElementById("actMainImg");
    const actImgsInput = document.getElementById("actImgs");

    // 日期限制
    const today = new Date();
    const minStartDate = new Date(today.getTime() + 45 * 24 * 60 * 60 * 1000);
    actStartInput.min = minStartDate.toISOString().split("T")[0];

    actStartInput.addEventListener("change", () => {
        const startDate = new Date(actStartInput.value);
        actEndInput.min = startDate.toISOString().split("T")[0];
        if (actEndInput.value) {
            const endDate = new Date(actEndInput.value);
            if (endDate < startDate) actEndInput.value = "";
        }
    });

    // 主圖預覽
    const mainPreview = document.createElement("img");
    mainPreview.style.marginTop = "10px";
    mainPreview.style.width = "150px";
    mainPreview.style.height = "150px";
    mainPreview.style.objectFit = "cover";
    mainPreview.style.borderRadius = "8px";
    actMainImgInput.parentNode.appendChild(mainPreview);

    actMainImgInput.addEventListener("change", () => {
        const file = actMainImgInput.files[0];
        if (file && file.type.startsWith("image/")) {
            const reader = new FileReader();
            reader.onload = e => mainPreview.src = e.target.result;
            reader.readAsDataURL(file);
        } else {
            mainPreview.src = "";
        }
    });

    // 多圖預覽
    const multiPreviewContainer = document.createElement("div");
    multiPreviewContainer.className = "preview-container";
    actImgsInput.parentNode.appendChild(multiPreviewContainer);

    actImgsInput.addEventListener("change", () => {
        multiPreviewContainer.innerHTML = "";
        const files = Array.from(actImgsInput.files).slice(0, 5);
        files.forEach(file => {
            if (file.type.startsWith("image/")) {
                const reader = new FileReader();
                const img = document.createElement("img");
                reader.onload = e => img.src = e.target.result;
                reader.readAsDataURL(file);
                multiPreviewContainer.appendChild(img);
            }
        });
    });
});