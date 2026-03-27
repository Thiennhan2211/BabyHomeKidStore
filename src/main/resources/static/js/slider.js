document.addEventListener("DOMContentLoaded", function () {

    const slides = document.querySelector(".slides");
    const images = document.querySelectorAll(".slide-img");

    let index = 0;

    if (!slides || images.length === 0) {
        console.log("Slider không tìm thấy phần tử");
        return;
    }

    console.log("Slider chạy, số ảnh:", images.length);

    setInterval(function () {

        index++;

        if (index >= images.length) {
            index = 0;
        }

        slides.style.transform = "translateX(-" + (index * 100) + "%)";

    }, 3000);

});