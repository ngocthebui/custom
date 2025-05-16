// Video section
const videoContainer = document.querySelector(".video-container");
const promoVideo = document.getElementById("promo-video");
const videoButton = document.querySelector(".video-btn");
const shopNowBtn = document.querySelector(".shop-now-btn");

let isMouseOverContainer = false;
let isMouseOverButton = false;
let isVideoStop = false;
let isMobile = window.innerWidth <= 768;

// Đảm bảo video luôn luôn tắt tiếng
promoVideo.muted = true;

// Cài đặt thuộc tính video cho thiết bị di động
if (isMobile) {
  // Cho phép phát inline (quan trọng cho iOS)
  promoVideo.setAttribute('playsinline', '');
  // Preload metadata để giúp hiển thị
  promoVideo.setAttribute('preload', 'metadata');

  // Tự động phát video khi trang đã tải xong
  window.addEventListener('load', () => {
    // Thử phát video
    promoVideo.play().then(() => {
      console.log("Video đang tự động phát");
      videoButton.innerHTML = "⏸";
      videoButton.classList.remove("play-icon");
      videoButton.classList.add("pause-icon");
    }).catch(error => {
      console.log("Không thể tự động phát video:", error);
      // Hiển thị nút play rõ ràng hơn
      videoButton.style.opacity = "1";
    });
  });
}

// Sự kiện mouse cho desktop
if (!isMobile) {
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
}

// Xử lý click vào nút play/pause
videoButton.addEventListener("click", () => {
  if (promoVideo.paused) {
    isVideoStop = false;

    // Nếu video chưa bắt đầu hoặc ở giây cuối cùng, quay về đầu
    if (promoVideo.currentTime === 0 || promoVideo.currentTime
        >= promoVideo.duration - 0.1) {
      promoVideo.currentTime = 0;
    }

    // THAY ĐỔI: Không bật âm thanh khi click nút play
    // Đảm bảo video luôn tắt tiếng
    promoVideo.muted = true;

    promoVideo.play().catch(e => {
      console.log("Không thể phát video:", e);
    });

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

// Thêm listener để đảm bảo video luôn luôn tắt tiếng, ngay cả khi người dùng
// cố gắng bật tiếng bằng các điều khiển mặc định của trình duyệt
promoVideo.addEventListener('volumechange', () => {
  if (!promoVideo.muted) {
    promoVideo.muted = true;
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
    slideInterval = setInterval(nextSlide, 3000);
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
