document.addEventListener('DOMContentLoaded', function () {
  // Lấy các phần tử hiệu ứng chuyển trang
  const pageTransition = document.querySelector('.page-transition-before');
  const transitionLogo = document.querySelector('.transition-logo');
  const transitionBackground = document.querySelector('.transition-background');
  const brandOverlay = document.querySelector('.brand-overlay');
  const brandLogo = document.querySelector('.brand-name');
  const header = document.querySelector('.header');

  // Lấy tất cả các liên kết có hiệu ứng chuyển trang
  const transitionLinks = document.querySelectorAll('.page-transition-link');

  // Hàm xử lý hiệu ứng chuyển trang
  function handlePageTransition(e) {
    e.preventDefault();

    const targetUrl = this.getAttribute('data-href') || this.getAttribute(
        'href');

    if (!targetUrl) {
      return;
    }

    // Hiển thị container hiệu ứng chuyển trang
    pageTransition.style.visibility = 'visible';

    // Sao chép nội dung và style từ brand-name sang transition-logo
    transitionLogo.textContent = brandLogo.textContent;
    transitionLogo.style.fontSize = window.getComputedStyle(brandLogo).fontSize;
    transitionLogo.style.fontWeight = window.getComputedStyle(
        brandLogo).fontWeight;
    transitionLogo.style.letterSpacing = window.getComputedStyle(
        brandLogo).letterSpacing;
    transitionLogo.style.fontFamily = window.getComputedStyle(
        brandLogo).fontFamily;

    // Thiết lập vị trí ban đầu cho transition-logo giống với brand-name
    transitionLogo.style.color = 'white';
    transitionLogo.style.top = '50%';
    transitionLogo.style.left = '50%';
    transitionLogo.style.transform = 'translate(-50%, -50%)';

    const isMobile = window.innerWidth < 768;

    if (isMobile) {
      // Trên điện thoại: không hiển thị transition-logo và không ẩn brand-overlay
      transitionLogo.style.opacity = '0';
      transitionLogo.style.visibility = 'hidden';
      // Giữ nguyên brandOverlay
    } else {
      // Trên desktop: sẽ hiển thị ngay như hiện tại
      transitionLogo.style.opacity = '1';
      transitionLogo.style.visibility = 'visible';
      // Ẩn brand-name gốc
      brandOverlay.style.opacity = '0';
      brandOverlay.style.visibility = 'hidden';
    }

    // Tạo timeline GSAP cho hiệu ứng
    const tl = gsap.timeline({
      onComplete: function () {
        // Chuyển hướng sau khi hoàn thành hiệu ứng
        window.location.href = targetUrl;
      }
    });

    // 1. Logo phóng to 1.3 lần và chuyển màu đen trong 1 giây
    // Logo animation khác nhau cho mobile và desktop
    if (!isMobile) {
      // Trên desktop: phóng to như hiện tại
      tl.to(transitionLogo, {
        scale: 1.3,
        duration: 1,
        ease: 'power2.inOut'
      });
    }

    // 2. Background trắng chạy từ dưới lên trong 1.5 giây
    tl.to(transitionBackground, {
      opacity: 1,
      visibility: 'visible',
      bottom: '0%',
      duration: 1.5,
      ease: 'power2.inOut'
    }, '-=0.3'); // Bắt đầu sớm hơn 0.3s

    tl.to(header, {
      opacity: 0,
      duration: 0.2,
      ease: 'power2.out'
    }, '-=0.4');

    if (isMobile) {
      tl.to(transitionLogo, {
        opacity: 1,
        visibility: 'visible',
      }, '-=0.8');
      tl.to(header, {
        display: 'none',
        // ease: 'power2.out'
      });
    }

    tl.to(transitionLogo, {
      color: 'black',
      duration: 0.5,
      ease: 'power2.inOut',
      zIndex: 10003
    }, '-=0.9'); // 1 - 0.5 + (2 * 0.45) - 1.5 = -0.95

    tl.to(header, {
      transform: 'translateY(95%)'
    });

    // 3. Logo di chuyển lên trên và thu nhỏ trong 1 giây
    tl.to(transitionLogo, {
      top: '6%',
      scale: 0.25,
      duration: 1,
      ease: 'power2.inOut'
    }, '-=0.3'); // Bắt đầu sớm hơn 0.3s

    tl.to(transitionLogo, {
      opacity: 0,
      duration: 0.2,
      ease: 'power2.inOut'
    }, '-=0.5');

    tl.to(header, {
      display: 'flex',
      opacity: 1,
      transform: 'translateY(0)',
      visibility: 'visible',
      y: 0,
      duration: 0.3,
      ease: 'power2.out'
    }, '-=0.3');
  }

  // Áp dụng sự kiện click cho tất cả các liên kết chuyển trang
  transitionLinks.forEach(link => {
    link.addEventListener('click', handlePageTransition);
  });
});
