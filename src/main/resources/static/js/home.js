// Video section
const videoContainer = document.querySelector(".video-container");
const promoVideo = document.getElementById("promo-video");
const videoButton = document.querySelector(".video-btn");
const shopNowBtn = document.querySelector(".shop-now-btn");

let isMouseOverContainer = false;
let isMouseOverButton = false;
let isVideoStop = false;

videoContainer.addEventListener("mouseenter", () => {
  if (!isVideoStop) {
    isMouseOverContainer = true;
    promoVideo.play();
    videoButton.innerHTML = "⏸";
    videoButton.classList.remove("play-icon");
    videoButton.classList.add("pause-icon");
  }
});

videoContainer.addEventListener("mouseleave", () => {
  if (!isVideoStop) {
    isMouseOverContainer = false;

    setTimeout(() => {
      if (!isMouseOverContainer && !isMouseOverButton) {
        promoVideo.pause();
        videoButton.innerHTML = "▶";
        videoButton.classList.remove("pause-icon");
        videoButton.classList.add("play-icon");
      }
    }, 50);
  }
});

shopNowBtn.addEventListener("mouseenter", () => {
  isMouseOverButton = true;
});

shopNowBtn.addEventListener("mouseleave", () => {
  isMouseOverButton = false;
});

videoButton.addEventListener("mouseenter", () => {
  isMouseOverButton = true;
});

videoButton.addEventListener("mouseleave", () => {
  isMouseOverButton = false;
});

videoButton.addEventListener("click", () => {
  if (promoVideo.paused) {
    isVideoStop = false;
    promoVideo.play();
    videoButton.innerHTML = "⏸";
    videoButton.classList.remove("play-icon");
    videoButton.classList.add("pause-icon");
  } else {
    isVideoStop = true;
    promoVideo.pause();
    videoButton.innerHTML = "▶";
    videoButton.classList.remove("pause-icon");
    videoButton.classList.add("play-icon");
  }
});

// Slideshow section
const slides = document.querySelectorAll(".slide");
const indicators = document.querySelectorAll(".indicator");
const slideshowContainer = document.querySelector(".slideshow-container");

let currentSlide = 0;
let slideshowPaused = true;
let slideInterval;

function showSlide(n) {
  slides.forEach((slide) => slide.classList.remove("active"));
  indicators.forEach((indicator) => indicator.classList.remove("active"));

  currentSlide = (n + slides.length) % slides.length;

  slides[currentSlide].classList.add("active");
  indicators[currentSlide].classList.add("active");
}

function nextSlide() {
  showSlide(currentSlide + 1);
}

function startSlideshow() {
  if (slideshowPaused) {
    slideInterval = setInterval(nextSlide, 2000);
    slideshowPaused = false;
  }
}

function pauseSlideshow() {
  clearInterval(slideInterval);
  slideshowPaused = true;
}

indicators.forEach((indicator, index) => {
  indicator.addEventListener("click", () => {
    showSlide(index);
    pauseSlideshow();
  });
});

slideshowContainer.addEventListener("mouseenter", startSlideshow);
slideshowContainer.addEventListener("mouseleave", pauseSlideshow);

// Initialize with first slide
showSlide(0);

// Bắt đầu slideshow tự động trên thiết bị di động
if (window.innerWidth <= 768) {
  startSlideshow();
}
