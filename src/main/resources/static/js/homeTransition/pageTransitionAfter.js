document.addEventListener("DOMContentLoaded", function () {
  // Lấy các phần tử hiệu ứng chuyển trang
  const transitionLogo = document.querySelector(".brand-name");
  const header = document.querySelector(".header");

  const isMobile = window.innerWidth < 768;

  if (!isMobile) {
    gsap.set(transitionLogo, {
      scale: 1.3
    });
  }
  
  // Tạo timeline GSAP cho hiệu ứng
  const tl = gsap.timeline();

  tl.to(transitionLogo, {
    top: "6%",
    scale: 0.25,
    duration: 1,
    ease: "power2.inOut",
  });
  
  tl.to(
    transitionLogo,
    {
      opacity: 0,
      duration: 0.2,
      ease: "power2.inOut",
    },
    "-=0.5"
  );

  tl.to(
    header,
    {
      display: "flex",
      opacity: 1,
      transform: "translateY(0)",
      visibility: "visible",
      y: 0,
      duration: 0.3,
      ease: "power2.out",
    },
    "-=0.3"
  );
});
