document.addEventListener('DOMContentLoaded', function() {
  // Lấy các phần tử hiệu ứng chuyển trang
  const pageTransition = document.querySelector('.page-transition');
  const transitionLogo = document.querySelector('.transition-logo');
  const transitionBackground = document.querySelector('.transition-background');
  const transitionContent = document.querySelector('.transition-content');
  const brandOverlay = document.querySelector('.brand-overlay');
  const brandLogo = document.querySelector('.brand-name');

  // Lấy tất cả các liên kết có hiệu ứng chuyển trang
  const transitionLinks = document.querySelectorAll('.page-transition-link');

  // Hàm xử lý hiệu ứng chuyển trang
  function handlePageTransition(e) {
    e.preventDefault();

    const targetUrl = this.getAttribute('data-href') || this.getAttribute('href');

    if (!targetUrl) return;

    // Hiển thị container hiệu ứng chuyển trang
    pageTransition.style.visibility = 'visible';

    // Sao chép nội dung và style từ brand-name sang transition-logo
    transitionLogo.textContent = brandLogo.textContent;
    transitionLogo.style.fontSize = window.getComputedStyle(brandLogo).fontSize;
    transitionLogo.style.fontWeight = window.getComputedStyle(brandLogo).fontWeight;
    transitionLogo.style.letterSpacing = window.getComputedStyle(brandLogo).letterSpacing;
    transitionLogo.style.fontFamily = window.getComputedStyle(brandLogo).fontFamily;

    // Lấy vị trí của brand-name gốc
    const logoRect = brandLogo.getBoundingClientRect();

    // Thiết lập vị trí ban đầu cho transition-logo giống với brand-name
    transitionLogo.style.color = 'white';
    transitionLogo.style.top = '50%';
    transitionLogo.style.left = '50%';
    transitionLogo.style.transform = 'translate(-50%, -50%)';
    transitionLogo.style.opacity = '1';
    transitionLogo.style.visibility = 'visible';

    // Ẩn brand-name gốc
    brandOverlay.style.opacity = '0';
    brandOverlay.style.visibility = 'hidden';

    // Fetch nội dung trang mới
    fetch(targetUrl)
    .then(response => response.text())
    .then(html => {
      // Tạo một DOM parser để lấy nội dung của trang mới
      const parser = new DOMParser();
      const doc = parser.parseFromString(html, 'text/html');

      // Lấy phần nội dung chính của trang mới
      const newContent = doc.querySelector('.main-content');

      if (newContent) {
        // Xóa nội dung cũ
        transitionContent.innerHTML = '';

        // Thêm style để hiển thị nội dung mới đúng cách
        transitionContent.style.overflow = 'hidden';

        // Clone và thêm nội dung mới vào transition-content
        const clonedContent = newContent.cloneNode(true);

        // Đảm bảo nội dung mới được hiển thị đúng cách
        clonedContent.style.position = 'relative';
        clonedContent.style.width = '100%';
        clonedContent.style.height = '100%';

        transitionContent.appendChild(clonedContent);

        // Đảm bảo các scripts trong nội dung mới được thực thi
        Array.from(clonedContent.querySelectorAll('script')).forEach(oldScript => {
          const newScript = document.createElement('script');
          Array.from(oldScript.attributes).forEach(attr =>
              newScript.setAttribute(attr.name, attr.value));
          newScript.textContent = oldScript.textContent;
          oldScript.parentNode.replaceChild(newScript, oldScript);
        });
      } else {
        console.error('Không tìm thấy phần tử .main-content trong trang mới');
      }

      // Tạo timeline GSAP cho hiệu ứng
      const tl = gsap.timeline({
        onComplete: function() {
          // Chuyển hướng sau khi hoàn thành hiệu ứng
          window.location.href = targetUrl;
        }
      });

      // 1. Logo phóng to 1.5 lần và chuyển màu đen trong 1.5 giây
      tl.to(transitionLogo, {
        scale: 1.5,
        // color: 'black',
        duration: 1.5,
        ease: 'power2.inOut'
      });

      // 2. Background trắng chạy từ dưới lên trong 2 giây
      tl.to(transitionBackground, {
        bottom: '0%',
        duration: 2,
        ease: 'power2.inOut'
      }, '-=0.5'); // Bắt đầu sớm hơn 0.5s

      tl.to(transitionLogo, {
        color: 'black',
        duration: 0.5,
        ease: 'power2.inOut',
        zIndex: 10003
      }, '-=1.2'); // 1.5 - 0.5 + (2 * 0.45) - 1.5 = -0.95

      // 3. Logo di chuyển lên trên và thu nhỏ trong 2 giây
      tl.to(transitionLogo, {
        top: '10%',
        scale: 0.5,
        duration: 2,
        ease: 'power2.inOut'
      }, '-=0.5'); // Bắt đầu sớm hơn 0.5s

      // 4. Nội dung trang mới chạy từ dưới lên trong 3 giây
      tl.to(transitionContent, {
        bottom: '0%',
        duration: 2,
        ease: 'power2.inOut'
      }, '-=0.5'); // Bắt đầu sớm hơn 1s
    })
    .catch(error => {
      console.error('Lỗi khi tải trang mới:', error);
      // Nếu có lỗi, vẫn chuyển hướng đến trang mới
      window.location.href = targetUrl;
    });
  }

  // Áp dụng sự kiện click cho tất cả các liên kết chuyển trang
  transitionLinks.forEach(link => {
    link.addEventListener('click', handlePageTransition);
  });

  // Sự kiện này sẽ được kích hoạt khi trang mới được tải
  window.addEventListener('pageshow', function(event) {
    // Hiển thị lại brand-name
    if (brandOverlay) {
      brandOverlay.style.opacity = '1';
      brandOverlay.style.visibility = 'visible';
    }

    // Ẩn các phần tử hiệu ứng chuyển trang
    pageTransition.style.visibility = 'hidden';
    transitionLogo.style.visibility = 'hidden';
    transitionLogo.style.opacity = '0';
    transitionBackground.style.bottom = '-100%';
    transitionContent.style.bottom = '-100%';

    // Xóa nội dung đã fetch để tránh xung đột
    transitionContent.innerHTML = '';
  });
});