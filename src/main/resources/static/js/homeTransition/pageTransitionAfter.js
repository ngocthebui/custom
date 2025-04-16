document.addEventListener('DOMContentLoaded', function () {
  // Lấy các phần tử hiệu ứng chuyển trang
  const brandLogo = document.querySelector('.brand-name');
  const header = document.querySelector('.header');
  const pageTransitionAfter = document.querySelector('.page-transition-after');

  // Khởi tạo timeline GSAP
  const tl = gsap.timeline();

  const isMobile = window.innerWidth < 768;

  if (!isMobile) {
    // Trên desktop: phóng to như hiện tại
    tl.to(brandLogo, {
      scale: 1.3,
    });
  }

  // Thiết lập vị trí ban đầu của brandLogo trước khi bắt đầu animation
  gsap.set(brandLogo, {
    position: 'fixed',  // Sử dụng position fixed để dễ điều khiển vị trí
    top: '50%',        // Bắt đầu từ giữa màn hình theo chiều dọc
    left: '50%',       // Bắt đầu từ giữa màn hình theo chiều ngang
    xPercent: -50,     // Dịch chuyển để căn giữa logo
    yPercent: -50,     // Dịch chuyển để căn giữa logo
    opacity: 1,        // Đảm bảo logo hiển thị
    zIndex: 11111      // Đảm bảo hiển thị trên các phần tử khác
  });

  // Ẩn header ban đầu
  tl.to(header, {
    opacity: 0,
    display: 'none',
    duration: 0.1
  });

  // 3. Logo di chuyển lên trên và thu nhỏ trong 1 giây
  tl.to(brandLogo, {
    top: '0%',         // Di chuyển lên vị trí top 6%
    yPercent: 0,       // Bỏ căn giữa theo chiều dọc
    scale: 0.25,       // Thu nhỏ
    duration: 1,       // Thời gian 1 giây
    ease: 'power2.inOut'
  });

  tl.to(brandLogo, {
    opacity: 0,
    duration: 0.3,
    ease: 'power2.inOut'
  }, '-=0.3');

  // Hiển thị header sau khi logo đã di chuyển
  tl.to(header, {
    display: 'flex',
    opacity: 1,
    visibility: 'visible',
    y: 0,
    duration: 0.5,
    ease: 'power2.out'
  }, '-=0.3');

  // Ẩn phần tử .page-transition-after sau khi animation hoàn tất
  tl.to(pageTransitionAfter, {
    opacity: 0,
    visibility: 'hidden',
    duration: 0.3
  });
});
