/**
 * Select Image
 * Button Quantity
 * Delete File
 * Go Top
 * Variant Picker
 * Change Value
 * Sidebar Mobile
 * Check Active
 * Stagger Wrap
 * Modal Second
 * Header Sticky
 * Auto Popup
 * Total Price Variant
 * Scroll Grid Product
 * Handle Progress
 * Handle Footer
 * Infinite Slide
 * Add Wishlist
 * Handle Sidebar Filter
 * Estimate Shipping
 * Coupon Copy
 * Animation Custom
 * Parallaxie
 * Update Compare Empty
 * Delete Wishlist
 * Click Active
 * Handle Mobile Menu
 * Color Swatch Product
 * Tabs
 * Text Rotate
 * Custom Dropdown
 * Range Size
 * Bottom Sticky
 * Write Review
 * Video
 * Show Password
 * Change Image Dashboard
 * Open Mmenu
 * Preloader
 */

(function ($) {
  "use strict";

  /* Select Image
  -------------------------------------------------------------------------*/
  var dropdownSelect = function () {
    // $(".tf-dropdown-select").selectpicker();
    if ($(".tf-dropdown-select").length > 0) {
      const selectIMG = $(".tf-dropdown-select");

      selectIMG.find("option").each((idx, elem) => {
        const selectOption = $(elem);
        const imgURL = selectOption.attr("data-thumbnail");
        if (imgURL) {
          selectOption.attr("data-content",
              `<img src="${imgURL}" alt="Country" /> ${selectOption.text()}`);
        }
      });
      selectIMG.selectpicker();
    }
  };

  /* Button Quantity
  -------------------------------------------------------------------------*/
  var btnQuantity = function () {
    $(".minus-btn").on("click", function (e) {
      e.preventDefault();
      var $this = $(this);
      var $input = $this.closest("div").find("input");
      var value = parseInt($input.val(), 10);

      if (value > 1) {
        value = value - 1;
      }
      $input.val(value);
    });

    $(".plus-btn").on("click", function (e) {
      e.preventDefault();
      var $this = $(this);
      var $input = $this.closest("div").find("input");
      var value = parseInt($input.val(), 10);

      if (value > -1) {
        value = value + 1;
      }
      $input.val(value);
    });
  };

  /* Delete File
  -------------------------------------------------------------------------*/
  var deleteFile = function (e) {
    // --- helpers ---
    function parseCurrency(text) {
      return Math.max(0, parseInt((text || "").replace(/[^\d]/g, ""), 10) || 0);
    }

    // $el can be a jQuery set (input or span) or empty
    function parseQuantity($el) {
      $el = $el || $(); // ensure jQuery
      // prefer input value if any
      var $input = $el.filter("input").add($el.find("input")).first();
      if ($input.length) {
        return parseInt($input.val(), 10) || 0;
      }
      // otherwise take first element's text
      var txt = ($el.filter(":not(input)").first().text() || "");
      return parseInt(txt.replace(/\D/g, ""), 10) || 0;
    }

    // --- update functions ---
    function updatePriceEach() {
      $(".each-prd").each(function () {
        var $prd = $(this);
        var qty = parseQuantity($prd.find(".quantity-product, .number"));
        var price = parseCurrency($prd.find(".each-price").text());
        var subtotal = price * qty;
        $prd.find(".each-subtotal-price").text(
            subtotal.toLocaleString("vi-VN") + " ₫");
      });
    }

    function updateTotalPriceEach() {
      var total = 0, totalItems = 0;
      $(".each-list-prd .each-prd").each(function () {
        var $prd = $(this);
        var qty = parseQuantity($prd.find(".quantity-product, .number"));
        var price = parseCurrency($prd.find(".each-price").text());
        total += price * qty;
        totalItems += qty;
      });
      $(".each-total-price").text(total.toLocaleString("vi-VN") + " ₫");
      $(".prd-count").text(totalItems); // tổng số lượng item
      // optional: if you still want line-count, write to .prd-lines-count (if exists)
      $(".prd-lines-count").text($(".each-list-prd .each-prd").length);
    }

    function updateTotalPrice() {
      var total = 0, totalItems = 0;
      $(".list-file-delete .tf-mini-cart-item").each(function () {
        var $prd = $(this);
        var qty = parseQuantity($prd.find(".quantity-product, .number"));
        var price = parseCurrency($prd.find(".tf-mini-card-price").text());
        total += price * qty;
        totalItems += qty;
      });
      $(".tf-totals-total-value").text(total.toLocaleString("vi-VN") + " ₫");
      $(".prd-count").text(totalItems); // also keep total items in mini cart

      // --- Update progress ---
      var target = 10000000; // ví dụ: miễn phí ship khi đạt 10 triệu
      var percent = Math.min((total / target) * 100, 100); // max 100%
      $(".tf-progress-bar .value")
      .attr("data-progress", percent.toFixed(0)) // cập nhật attribute
      .css("width", percent + "%");             // cập nhật style
    }

    // updateCount: compute BOTH line count and total items
    function updateCount() {
      var totalItems = 0;
      $(".list-file-delete .file-delete").each(function () {
        totalItems += parseQuantity($(this).find(".quantity-product, .number"));
      });
      $(".prd-count").text(totalItems);
      if (totalItems === 0) {
        $(".count").hide();
      } else {
        $(".count").show().text(totalItems);
      }
    }

    // rest of helper functions left as before
    function checkListEmpty() {
      $(".wrap-empty_text").each(function () {
        var $listEmpty = $(this);
        var $textEmpty = $listEmpty.find(".box-text_empty");
        var $otherChildren = $listEmpty.find(".list-empty").children().not(
            ".box-text_empty");
        var $boxEmpty = $listEmpty.find(".box-empty_clear");
        if ($otherChildren.length > 0) {
          $textEmpty.hide();
        } else {
          $textEmpty.show();
          $boxEmpty.hide();
        }
      });
    }

    function ortherDel() {
      $(".container .orther-del").remove();
    }

    // --- debounce helper ---
    function debounce(fn, delay) {
      var t;
      return function () {
        var args = arguments, ctx = this;
        clearTimeout(t);
        t = setTimeout(function () {
          fn.apply(ctx, args);
        }, delay || 100);
      };
    }

    var updateAllDebounced = debounce(function () {
      updatePriceEach();
      updateTotalPriceEach();
      updateTotalPrice();
      updateCount();
    }, 120);

    // --- delegated event handlers (robust) ---
    // quantity input change (typing)
    $(document).on("input",
        ".list-file-delete .quantity-product, .each-list-prd .quantity-product",
        function () {
          updateAllDebounced();
        });

    // plus / minus (use closest container to find the input)
    $(document).on("click", ".plus-quantity, .minus-quantity", function (e) {
      var $btn = $(this);
      // find nearest product row (either .file-delete or .each-prd)
      var $row = $btn.closest(".file-delete, .each-prd");
      var $input = $row.find(".quantity-product").first();
      var current = parseInt($input.val(), 10) || 0;
      if ($btn.hasClass("plus-quantity")) {
        $input.val(current + 1);
      } else if ($btn.hasClass("minus-quantity") && current > 1) {
        $input.val(current - 1);
      }
      updateAllDebounced();
    });

    // remove item (delegated)
    $(document).on("click", ".list-file-delete .remove, .each-list-prd .remove", function (e) {
      e.preventDefault();

      const $item = $(this).closest(".file-delete, .each-prd");
      const itemId = $item.data("id"); // lấy ra cartItemId

      $item.remove();
      updateAllDebounced();
      checkListEmpty();
      ortherDel();

      if (!itemId) {
        console.error("Không tìm thấy ID của item");
        return;
      }

      // Lấy CSRF token
      const token = $('meta[name="_csrf"]').attr('content');
      const header = $('meta[name="_csrf_header"]').attr('content');

      fetch(`/api/cart/delete/${itemId}`, {
        method: "DELETE",
        headers: {
          'Content-Type': 'application/json',
          [header]: token
        }
      })
      .then(response => {
        if (!response.ok) {
          console.error("Xoá item thất bại, status:", response.status);
        }
      })
      .catch(err => {
        console.error("Lỗi khi gọi API xoá item:", err);
      });
    });

    // clear all
    $(document).on("click", ".clear-file-delete", function (e) {
      e.preventDefault();
      $(this).closest(".list-file-delete").find(".file-delete").remove();
      updateAllDebounced();
      checkListEmpty();
    });

    // init run
    checkListEmpty();
    updatePriceEach();
    updateTotalPriceEach();
    updateTotalPrice();
    updateCount();
  }; // end deleteFile

  /* Go Top
  -------------------------------------------------------------------------*/
  var goTop = function () {
    var $goTop = $("#goTop");
    var $borderProgress = $(".border-progress");

    $(window).on("scroll", function () {
      var scrollTop = $(window).scrollTop();
      var docHeight = $(document).height() - $(window).height();
      var scrollPercent = (scrollTop / docHeight) * 100;
      var progressAngle = (scrollPercent / 100) * 360;

      $borderProgress.css("--progress-angle", progressAngle + "deg");

      if (scrollTop > 100) {
        $goTop.addClass("show");
      } else {
        $goTop.removeClass("show");
      }
    });

    $goTop.on("click", function () {
      $("html, body").animate({scrollTop: 0}, 0);
    });
  };

  /* Variant Picker
  -------------------------------------------------------------------------*/
  var variantPicker = function () {
    $(".variant-picker-item").each(function () {
      var $picker = $(this);

      $picker.find(".color-btn").on("click", function (e) {
        var value2 = $(this).data("color");

        // Chỉ tìm trong chính variant picker này
        $picker.find(".value-currentColor").text(value2);
        $picker.find(".color-btn").removeClass("active");
        $(this).addClass("active");
      });

      $picker.find(".size-btn").on("click", function (e) {
        var value = $(this).data("size");

        // Chỉ tìm trong chính variant picker này
        $picker.find(".value-currentSize").text(value);
        $picker.find(".size-btn").removeClass("active");
        $(this).addClass("active");
      });
    });
  };

  /* Change Value
  -------------------------------------------------------------------------*/
  var changeValue = function () {
    if ($(".tf-dropdown-sort").length > 0) {
      $(".select-item").on("click", function (event) {
        $(this).closest(".tf-dropdown-sort").find(".text-sort-value").text(
            $(this).find(".text-value-item").text());

        $(this).closest(".dropdown-menu").find(
            ".select-item.active").removeClass("active");

        $(this).addClass("active");

        var color = $(this).data("value-color");
        $(this).closest(".tf-dropdown-sort").find(".btn-select").find(
            ".current-color").css("background", color);
      });
    }
  };

  /* Sidebar Mobile
  -------------------------------------------------------------------------*/
  var sidebarMobile = function () {
    if ($(".sidebar-content-wrap").length > 0) {
      var sidebar = $(".sidebar-content-wrap").html();
      $(".sidebar-mobile-append").append(sidebar);
    }
  };

  /* Check Active
  -------------------------------------------------------------------------*/
  var checkClick = function () {
    $(".flat-check-list").on("click", ".check-item", function () {
      $(this).closest(".flat-check-list").find(".check-item").removeClass(
          "active");
      $(this).addClass("active");
    });
  };

  /* Stagger Wrap
  -------------------------------------------------------------------------*/
  var staggerWrap = function () {
    if ($(".stagger-wrap").length) {
      var count = $(".stagger-item").length;
      for (var i = 1, time = 0.2; i <= count; i++) {
        $(".stagger-item:nth-child(" + i + ")")
        .css("transition-delay", time * i + "s")
        .addClass("stagger-finished");
      }
    }
  };

  /* Modal Second
  -------------------------------------------------------------------------*/
  var clickModalSecond = function () {
    $(".show-size-guide").on("click", function () {
      $("#size-guide").modal("show");
    });
    $(".show-shopping-cart").on("click", function () {
      $("#shoppingCart").modal("show");
    });
    $(".btn-icon-action.wishlist").on("click", function () {
      $("#wishlist").modal("show");
    });

    // ===== ADD TO CART =====
    $(".btn-add-to-cart").on("click", function (e) {
      e.preventDefault();

      var $form = $(this).closest('form');
      var productId, sizeId, colorId, quantity;

      if ($form.length > 0 && $form.find('select[name="sizeId"]').length > 0) {
        // Lấy từ form (có select)
        productId = parseInt($form.find('input[name="productId"]').val(), 10);
        sizeId = parseInt($form.find('select[name="sizeId"]').val(), 10);
        colorId = parseInt($form.find('select[name="colorId"]').val(), 10);
        quantity = parseInt($form.find('.quantity-product').val(), 10);
      } else {
        // Lấy từ variant picker
        productId = parseInt($('input[name="productId"]').val(), 10);
        sizeId = parseInt($('.variant-size .size-btn.active').data('size-id'),
            10);
        colorId = parseInt(
            $('.variant-color .color-btn.active').data('color-id'), 10);
        quantity = parseInt($('.quantity-product').first().val(), 10);
      }

      // Lấy CSRF token
      var token = $("meta[name='_csrf']").attr("content");
      var header = $("meta[name='_csrf_header']").attr("content");

      fetch('/api/cart/add', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          [header]: token
        },
        body: JSON.stringify({
          productId: productId,
          sizeId: sizeId,
          colorId: colorId,
          quantity: quantity
        })
      })
      .then(response => {
        if (response.ok) {
          $(".tf-add-cart-success").addClass("active");
        }
      })
      .catch(error => {
        console.error('Lỗi:', error);
      });
    });

    $(".tf-add-cart-success .tf-add-cart-close").on("click", function () {
      $(".tf-add-cart-success").removeClass("active");
    });

    $(".btn-add-note, .btn-estimate-shipping, .btn-add-gift").on("click",
        function () {
          var classList = {
            "btn-add-note": ".add-note",
            "btn-estimate-shipping": ".estimate-shipping",
            "btn-add-gift": ".add-gift",
          };

          $.each(classList, function (btnClass, targetClass) {
            if ($(event.currentTarget).hasClass(btnClass)) {
              $(targetClass).addClass("open");
            }
          });
        });

    $(".tf-mini-cart-tool-close").on("click", function () {
      $(".tf-mini-cart-tool-openable").removeClass("open");
    });
  };

  /* Header Sticky
-------------------------------------------------------------------------*/
  var headerSticky = function () {
    let lastScrollTop = 0;
    let delta = 5;
    let navbarHeight = $(".header-fix").outerHeight();
    let didScroll = false;

    $(window).scroll(function () {
      didScroll = true;
    });

    setInterval(function () {
      if (didScroll) {
        let st = $(window).scrollTop();
        navbarHeight = $(".header-fix").outerHeight();

        if (st > navbarHeight) {
          if (st > lastScrollTop + delta) {
            $(".header-fix").css("top", `-${navbarHeight}px`);
            $(".sticky-top").css("top", "15px");
          } else if (st < lastScrollTop - delta) {
            $(".header-fix").css("top", "0");
            $(".header-fix").addClass("header-sticky");
            $(".sticky-top").css("top", `${15 + navbarHeight}px`);
          }
        } else {
          $(".header-fix").css("top", "unset");
          $(".header-fix").removeClass("header-sticky");
          $(".sticky-top").css("top", "15px");
        }
        lastScrollTop = st;
        didScroll = false;
      }
    }, 250);
  };

  /* headerFixed
-------------------------------------------------------------------------*/
  const headerFixed = () => {
    const $header = $(".header-fixed");
    if ($header.length === 0) {
      return;
    }

    let isFixed = false;
    const scrollThreshold = 200;

    const handleScroll = () => {
      const shouldBeFixed = $(window).scrollTop() >= scrollThreshold;
      if (shouldBeFixed !== isFixed) {
        $header.toggleClass("is-fixed", shouldBeFixed);
        isFixed = shouldBeFixed;
      }
    };

    $(window).on("scroll", handleScroll);
    handleScroll();
  };

  /* Auto Popup
  -------------------------------------------------------------------------*/
  var autoPopup = function () {
    if ($(".auto-popup").length > 0) {
      let showPopup = sessionStorage.getItem("showPopup");
      if (!JSON.parse(showPopup)) {
        setTimeout(function () {
          $(".auto-popup").modal("show");
        }, 2000);
      }
    }
    $(".btn-hide-popup").on("click", function () {
      sessionStorage.setItem("showPopup", true);
    });
  };

  /* Total Price Variant
  -------------------------------------------------------------------------*/
  var totalPriceVariant = function () {
    $(".tf-product-info-list,.tf-cart-item").each(function () {
      var productItem = $(this);
      var basePrice =
          parseFloat(productItem.find(".price-on-sale").data("base-price")) ||
          parseFloat(productItem.find(".price-on-sale").text().replace("$",
              "").replace(/,/g, ""));
      var quantityInput = productItem.find(".quantity-product");
      var personSale = parseFloat(
          productItem.find(".number-sale").data("person-sale") || 5);
      var compareAtPrice = basePrice * (1 + personSale / 100);

      productItem.find(".compare-at-price").text(
          `$${compareAtPrice.toLocaleString("en-US",
              {minimumFractionDigits: 2})}`);
      productItem.find(".color-btn, .size-btn").on("click", function () {
        quantityInput.val(1);
      });

      productItem.find(".btn-increase").on("click", function () {
        var currentQuantity = parseInt(quantityInput.val(), 10);
        quantityInput.val(currentQuantity + 1);
        updateTotalPrice(null, productItem);
      });

      productItem.find(".btn-decrease").on("click", function () {
        var currentQuantity = parseInt(quantityInput.val(), 10);
        if (currentQuantity > 1) {
          quantityInput.val(currentQuantity - 1);
          updateTotalPrice(null, productItem);
        }
      });

      function updateTotalPrice(price, scope) {
        var currentPrice = price || parseFloat(
            scope.find(".price-on-sale").text().replace("$", "").replace(/,/g,
                ""));
        var quantity = parseInt(scope.find(".quantity-product").val(), 10);
        var totalPrice = currentPrice * quantity;

        scope.find(".price-add").text(`$${totalPrice.toLocaleString("en-US",
            {minimumFractionDigits: 2})}`);
      }
    });
  };

  /* Scroll Grid Product
  -------------------------------------------------------------------------*/
  var scrollGridProduct = function () {
    var scrollContainer = $(".wrapper-gallery-scroll");
    var activescrollBtn = null;
    var offsetTolerance = 20;

    function isHorizontalMode() {
      return window.innerWidth <= 767;
    }

    function getTargetScroll(target, isHorizontal) {
      if (isHorizontal) {
        return target.offset().left - scrollContainer.offset().left
            + scrollContainer.scrollLeft();
      } else {
        return target.offset().top - scrollContainer.offset().top
            + scrollContainer.scrollTop();
      }
    }

    $(".btn-scroll-target").on("click", function () {
      var scroll = $(this).data("scroll");
      var target = $(".item-scroll-target[data-scroll='" + scroll + "']");

      if (target.length > 0) {
        var isHorizontal = isHorizontalMode();
        var targetScroll = getTargetScroll(target, isHorizontal);

        if (isHorizontal) {
          scrollContainer.animate({scrollLeft: targetScroll}, 600);
        } else {
          $("html, body").animate({scrollTop: targetScroll}, 100);
        }

        $(".btn-scroll-target").removeClass("active");
        $(this).addClass("active");
        activescrollBtn = $(this);
      }
    });

    $(window).on("scroll", function () {
      var isHorizontal = isHorizontalMode();
      $(".item-scroll-target").each(function () {
        var target = $(this);
        var targetScroll = getTargetScroll(target, isHorizontal);

        if (isHorizontal) {
          if ($(window).scrollLeft() >= targetScroll - offsetTolerance && $(
              window).scrollLeft() <= targetScroll + target.outerWidth()) {
            $(".btn-scroll-target").removeClass("active");
            $(".btn-scroll-target[data-scroll='" + target.data("scroll")
                + "']").addClass("active");
          }
        } else {
          if ($(window).scrollTop() >= targetScroll - offsetTolerance && $(
              window).scrollTop() <= targetScroll + target.outerHeight()) {
            $(".btn-scroll-target").removeClass("active");
            $(".btn-scroll-target[data-scroll='" + target.data("scroll")
                + "']").addClass("active");
          }
        }
      });
    });
  };

  /* Handle Progress
  -------------------------------------------------------------------------*/
  var handleProgress = function () {
    if ($(".progress-cart").length > 0) {
      var progressValue = $(".progress-cart .value").data("progress");
      setTimeout(function () {
        $(".progress-cart .value").css("width", progressValue + "%");
      }, 800);
    }

    function handleProgressBar(showEvent, hideEvent, target) {
      $(target).on(hideEvent, function () {
        $(".tf-progress-bar .value").css("width", "0%");
      });

      $(target).on(showEvent, function () {
        setTimeout(function () {
          var progressValue = $(".tf-progress-bar .value").data("progress");
          $(".tf-progress-bar .value").css("width", progressValue + "%");
        }, 600);
      });
    }

    if ($(".popup-shopping-cart").length > 0) {
      handleProgressBar("show.bs.offcanvas", "hide.bs.offcanvas",
          ".popup-shopping-cart");
    }

    if ($(".popup-shopping-cart").length > 0) {
      handleProgressBar("show.bs.modal", "hide.bs.modal",
          ".popup-shopping-cart");
    }
  };

  /* Handle Footer
  -------------------------------------------------------------------------*/
  var handleFooter = function () {
    var footerAccordion = function () {
      var args = {duration: 250};
      $(".footer-heading-mobile").on("click", function () {
        var $parent = $(this).parent(".footer-col-block");
        var $content = $(this).next();

        $parent.toggleClass("open");

        if (!$parent.hasClass("open")) {
          $content.slideUp(args);
        } else {
          $content.slideDown(args);
        }
      });
    };

    function handleAccordion() {
      if (window.matchMedia("only screen and (max-width: 575px)").matches) {
        if (!$(".footer-heading-mobile").data("accordion-initialized")) {
          footerAccordion();
          $(".footer-heading-mobile").data("accordion-initialized", true);
        }
      } else {
        $(".footer-heading-mobile")
        .off("click")
        .removeData("accordion-initialized")
        .each(function () {
          $(this).parent(".footer-col-block").removeClass(
              "open").end().next().removeAttr("style");
        });
      }
    }

    handleAccordion();
    $(window).on("resize", handleAccordion);
  };

  /* Infinite Slide
  -------------------------------------------------------------------------*/
  var infiniteSlide = function () {
    if ($(".infiniteSlide").length > 0) {
      $(".infiniteSlide").each(function () {
        var $this = $(this);
        var style = $this.data("style") || "left";
        var clone = $this.data("clone") || 2;
        var speed = $this.data("speed") || 50;
        $this.infiniteslide({
          speed: speed,
          direction: style,
          clone: clone,
        });
      });
    }
  };

  /* Add Wishlist
  -------------------------------------------------------------------------*/
  var addWishList = function () {
    $(".btn-add-wishlist, .card-product .wishlist").on("click", function () {
      let $this = $(this);
      let icon = $this.find(".icon");
      let tooltip = $this.find(".tooltip");

      $this.toggleClass("addwishlist");

      if ($this.hasClass("addwishlist")) {
        icon.removeClass("icon-heart").addClass("icon-trash");
        tooltip.text("Remove Wishlist");
      } else {
        icon.removeClass("icon-trash").addClass("icon-heart");
        tooltip.text("Add to Wishlist");
      }
    });
    $(".btn-add-wishlist2").on("click", function () {
      let $this = $(this);
      let icon = $this.find(".icon");
      let text = $this.find(".text");

      $this.toggleClass("addwishlist");

      if ($this.hasClass("addwishlist")) {
        icon.removeClass("icon-heart").addClass("icon-trash");
        text.text("Remove List");
      } else {
        icon.removeClass("icon-trash").addClass("icon-heart");
        text.text("Add to List");
      }
    });
  };

  /* Handle Sidebar Filter
  -------------------------------------------------------------------------*/
  var handleSidebarFilter = function () {
    $("#filterShop,.sidebar-btn").on("click", function () {
      if ($(window).width() <= 1200) {
        $(".sidebar-filter,.overlay-filter").addClass("show");
      }
    });
    $(".close-filter,.overlay-filter").on("click", function () {
      $(".sidebar-filter,.overlay-filter").removeClass("show");
    });
  };
  /* Estimate Shipping
  -------------------------------------------------------------------------*/
  var estimateShipping = function () {
    if ($(".estimate-shipping").length) {
      const $countrySelect = $("#shipping-country-form");
      const $provinceSelect = $("#shipping-province-form");
      const $zipcodeInput = $("#zipcode");
      const $zipcodeMessage = $("#zipcode-message");
      const $zipcodeSuccess = $("#zipcode-success");
      const $shippingForm = $("#shipping-form");

      function updateProvinces() {
        const selectedCountry = $countrySelect.val();
        const $selectedOption = $countrySelect.find("option:selected");
        const provincesData = $selectedOption.attr("data-provinces");

        const provinces = JSON.parse(provincesData);
        $provinceSelect.empty();

        if (provinces.length === 0) {
          $provinceSelect.append($("<option>").text("------"));
        } else {
          provinces.forEach(function (province) {
            $provinceSelect.append(
                $("<option>").val(province[0]).text(province[1]));
          });
        }
      }

      $countrySelect.on("change", updateProvinces);

      function validateZipcode(zipcode, country) {
        let regex;

        switch (country) {
          case "Australia":
          case "Austria":
          case "Belgium":
          case "Denmark":
            regex = /^\d{4}$/;
            break;
          case "Canada":
            regex = /^[A-Za-z]\d[A-Za-z][ -]?\d[A-Za-z]\d$/;
            break;
          case "Czech Republic":
          case "Finland":
          case "France":
          case "Germany":
          case "Mexico":
          case "South Korea":
          case "Spain":
          case "Italy":
            regex = /^\d{5}$/;
            break;
          case "United States":
            regex = /^\d{5}(-\d{4})?$/;
            break;
          case "United Kingdom":
            regex = /^[A-Za-z]{1,2}\d[A-Za-z\d]? \d[A-Za-z]{2}$/;
            break;
          case "India":
          case "Vietnam":
            regex = /^\d{6}$/;
            break;
          case "Japan":
            regex = /^\d{3}-\d{4}$/;
            break;
          default:
            return true;
        }

        return regex.test(zipcode);
      }

      $shippingForm.on("submit", function (event) {
        const zipcode = $zipcodeInput.val().trim();
        const country = $countrySelect.val();

        if (!validateZipcode(zipcode, country)) {
          $zipcodeMessage.show();
          $zipcodeSuccess.hide();
          event.preventDefault();
        } else {
          $zipcodeMessage.hide();
          $zipcodeSuccess.show();
          event.preventDefault();
        }
      });

      $(window).on("load", updateProvinces);
    }
  };

  /* Coupon Copy
  -------------------------------------------------------------------------*/
  var textCopy = function () {
    $(".coupon-copy-wrap,.btn-coppy-text").on("click", function () {
      const couponCode = $(this).closest(".discount-bot,.wrap-code").find(
          ".coupon-code,.coppyText").text().trim();

      if (navigator.clipboard) {
        navigator.clipboard
        .writeText(couponCode)
        .then(function () {
          alert("Copied! " + couponCode);
        })
        .catch(function (err) {
          alert("Unable to copy: " + err);
        });
      } else {
        const tempInput = $("<input>");
        $("body").append(tempInput);
        tempInput.val(couponCode).select();
        document.execCommand("copy");
        tempInput.remove();
        alert("Copied! " + couponCode);
      }
    });
  };

  /* Parallaxie
  -------------------------------------------------------------------------*/
  var parallaxie = function () {
    var $window = $(window);

    if ($(".parallaxie").length) {
      function initParallax() {
        if ($(".parallaxie").length && $window.width() > 991) {
          $(".parallaxie").parallaxie({
            speed: 0.55,
            offset: 0,
          });
        }
      }

      initParallax();

      $window.on("resize", function () {
        if ($window.width() > 991) {
          initParallax();
        }
      });
    }
  };

  /* Update Compare Empty
  -------------------------------------------------------------------------*/
  var tableCompareRemove = function () {
    $(".remove").on("click", function () {
      let $clickedCol = $(this).closest(".compare-col");
      let colIndex = $clickedCol.index();
      let $rows = $(".compare-row");
      let visibleCols = $(".compare-row:first .compare-col:visible").length;

      if (visibleCols > 4) {
        $rows.each(function () {
          $(this).find(".compare-col").eq(colIndex).fadeOut(300);
        });
      } else {
        $rows.each(function () {
          let $cols = $(this).find(".compare-col");
          let $colToMove = $cols.eq(colIndex);

          $colToMove.children().fadeOut(300, function () {
            let $parentRow = $(this).closest(".compare-row");
            $colToMove.appendTo($parentRow);
          });
        });
      }
    });
  };

  /* Delete Wishlist
  ----------------------------------------------------------------------------*/
  var deleteWishList = function () {
    function checkEmpty() {
      var $wishlistInner = $(".wrapper-wishlist");
      var $product = $(".wrapper-wishlist .card-product");
      var productCount = $(".wrapper-wishlist .card-product").length;

      if (productCount <= 12) {
        $(".wrapper-wishlist .wd-full").hide();
        $product.css("display", "flex");
      } else {
        $(".wrapper-wishlist .wd-full").show();
        $product.slice(0, 12).css("display", "flex");
        $product.slice(12).hide();
      }

      if (productCount === 0) {
        $wishlistInner.append(`
          <div class="tf-wishlist-empty text-center">
            <p class="text-notice">NO PRODUCTS WERE ADDED TO THE WISHLIST.</p>
            <a href="index.html" class="tf-btn animate-btn btn-fill btn-back-shop">BACK TO SHOPPING</a>
          </div>
        `);
      } else {
        $wishlistInner.find(".tf-compare-empty").remove();
      }
    }

    $(".wrapper-wishlist .card-product .remove").on("click", function (e) {
      e.preventDefault();
      var $this = $(this);
      $this.closest(".card-product").remove();
      checkEmpty();
    });

    checkEmpty();
  };

  /* Click Active
  -------------------------------------------------------------------------*/
  var clickActive = function () {
    function isAllowed($container) {
      return !$container.hasClass("active-1600") || window.innerWidth < 1600;
    }

    let previousWidth = window.innerWidth;

    if (window.innerWidth < 1600) {
      $(".main-action-active.active-1600").each(function () {
        $(this).find(".btn-active, .active-item").removeClass("active");
      });
    }
    $(window).on("resize", function () {
      const currentWidth = window.innerWidth;

      const crossedBreakpoint = (previousWidth < 1600 && currentWidth >= 1600)
          || (previousWidth >= 1600 && currentWidth < 1600);

      if (crossedBreakpoint) {
        $(".main-action-active").each(function () {
          $(this).find(".btn-active, .active-item").removeClass("active");
        });

        if (previousWidth < 1600 && currentWidth >= 1600) {
          $(".main-action-active.active-1600").each(function () {
            const $container = $(this);
            const $btn = $container.find(".btn-active");
            const $item = $container.find(".active-item");

            $btn.addClass("active");
            $item.addClass("active");
          });
        }
      }

      previousWidth = currentWidth;
    });

    $(".btn-active").on("click", function (event) {
      const $container = $(this).closest(".main-action-active");

      if (!isAllowed($container)) {
        return;
      }

      event.stopPropagation();

      const $activeItem = $container.find(".active-item");
      const isResponsive = $container.hasClass("active-1600")
          && window.innerWidth < 1600;

      if (isResponsive) {
        $(".main-action-active").each(function () {
          if (this !== $container[0]) {
            $(this).find(".btn-active, .active-item").removeClass("active");
          }
        });
      } else {
        $(".main-action-active").each(function () {
          const $other = $(this);
          if (this !== $container[0] && (!$other.hasClass("active-1600")
              || window.innerWidth < 1600)) {
            $other.find(".btn-active, .active-item").removeClass("active");
          }
        });
      }

      $(this).toggleClass("active");
      $activeItem.toggleClass("active");
    });

    $(document).on("click", function (event) {
      const isMobile = window.innerWidth < 1600;

      $(".main-action-active").each(function () {
        const $container = $(this);
        const is1600 = $container.hasClass("active-1600");

        if ((is1600 && isMobile) || !is1600) {
          if (!$(event.target).closest($container).length) {
            $container.find(".btn-active, .active-item").removeClass("active");
          }
        }
      });
    });

    $(".choose-option-item").on("click", function () {
      const $container = $(this).closest(".main-action-active");
      if (!isAllowed($container)) {
        return;
      }

      $(this).closest(".choose-option-list").find(".select-option").removeClass(
          "select-option");
      $(this).addClass("select-option");
    });
  };

  /* Handle Mobile Menu
  -------------------------------------------------------------------------*/
  var handleMobileMenu = function () {
    const $desktopMenu = $(".box-nav-menu:not(.not-append)").clone();
    $desktopMenu.find(".list-ver, .list-hor,.mn-none").remove();

    const $mobileMenu = $('<ul class="nav-ul-mb"></ul>');

    $desktopMenu.find("> li.menu-item").each(function (i, menuItem) {
      const $item = $(menuItem);
      const text = $item.find(
          "> a.item-link").clone().children().remove().end().text().trim();
      const submenu = $item.find("> .sub-menu");
      const id = "dropdown-menu-" + i;
      if (text.toLowerCase() === "home") {
        const $li = $(`
              <li class="nav-mb-item">
                  <a href="#${id}" class="collapsed mb-menu-link" data-bs-toggle="collapse" aria-expanded="false" aria-controls="${id}">
                      <span>${text}</span>
                      <span class="icon icon-caret-down"></span>
                  </a>
                  <div id="${id}" class="collapse">
                      <ul class="sub-nav-menu"></ul>
                  </div>
              </li>
          `);
        $(".modalDemo .demo-name").each(function () {
          const $demoName = $(this);
          const link = $demoName.attr("href") || "#";
          const title = $demoName.text().trim();
          const isActive = $demoName.hasClass("active");
          if (title) {
            const activeClass = isActive ? "active" : "";
            $li.find(".sub-nav-menu").append(
                `<li><a href="${link}" class="sub-nav-link ${activeClass}">${title}</a></li>`);
          }
        });
        $mobileMenu.append($li);
        return;
      }

      if (submenu.length > 0) {
        const $li = $(`
                <li class="nav-mb-item">
                    <a href="#${id}" class="collapsed mb-menu-link" data-bs-toggle="collapse" aria-expanded="false" aria-controls="${id}">
                        <span>${text}</span>
                        <span class="icon icon-caret-down"></span>
                    </a>
                    <div id="${id}" class="collapse"></div>
                </li>
            `);

        const $subNav = $('<ul class="sub-nav-menu"></ul>');

        submenu.find(".mega-menu-item").each(function (j) {
          const heading = $(this).find(".menu-heading").text().trim();
          const subId = `${id}-group-${j}`;
          const $group = $(`
                    <li>
                        <a href="#${subId}" class="collapsed sub-nav-link" data-bs-toggle="collapse" aria-expanded="false" aria-controls="${subId}">
                            <span>${heading}</span>
                            <span class="icon icon-caret-down"></span>
                        </a>
                        <div id="${subId}" class="collapse">
                            <ul class="sub-nav-menu sub-menu-level-2"></ul>
                        </div>
                    </li>
                `);

          $(this)
          .find(".sub-menu_list a")
          .each(function () {
            const $link = $(this);
            const linkHref = $link.attr("href") || "#";
            const title = $link.text().trim();
            const isActive = $link.hasClass("active");

            if (title !== "") {
              const activeClass = isActive ? "active" : "";
              $group
              .find(".sub-menu-level-2")
              .append(
                  `<li><a href="${linkHref}" class="sub-nav-link ${activeClass}">${title}</a></li>`);
            }
          });

          $subNav.append($group);
        });

        if ($subNav.children().length === 0) {
          submenu.find("a").each(function () {
            const link = $(this).attr("href") || "#";
            const title = $(this).text().trim();
            if (title !== "") {
              $subNav.append(
                  `<li><a href="${link}" class="sub-nav-link">${title}</a></li>`);
            }
          });
        }
        $li.find(`#${id}`).append($subNav);
        $mobileMenu.append($li);
      } else {
        $mobileMenu.append(
            `<li class="nav-mb-item"><a href="${$item.find("a").attr(
                "href")}" class="mb-menu-link"><span>${text}</span></a></li>`
        );
      }
    });

    $("#wrapper-menu-navigation").empty().append($mobileMenu.html());
  };

  /* Color Swatch Product
-------------------------------------------------------------------------*/
  var swatchColor = function () {
    if ($(".card-product, .banner-card_product").length > 0) {
      $(".color-swatch").on("click mouseover", function () {
        var $swatch = $(this);

        // lấy link ảnh từ data attribute
        var mainImgUrl = $swatch.data("main");
        var hoverImgUrl = $swatch.data("hover") || mainImgUrl;

        var $card = $swatch.closest(".card-product, .banner-card_product");
        var $imgProduct = $card.find(".img-product");
        var $hoverImg = $card.find(".img-hover");

        // 👉 Cập nhật cả ảnh chính & hover cùng lúc
        if (mainImgUrl) {
          $imgProduct.attr({
            "src": mainImgUrl,
            "data-src": mainImgUrl
          });
        }

        if (hoverImgUrl) {
          $hoverImg.attr({
            "src": hoverImgUrl,
            "data-src": hoverImgUrl
          });
        }

        // Cập nhật label nếu có
        var $colorLabel = $swatch.find(".color-label");
        if ($colorLabel.length > 0) {
          $card.find(".quickadd-variant-color .variant-value")
          .text($colorLabel.text().trim());
        }

        // Active swatch
        $card.find(".color-swatch.active").removeClass("active");
        $swatch.addClass("active");
      });
    }
  };

  /* Tabs
  -------------------------------------------------------------------------*/
  var tabs = function () {
    $(".widget-tabs").each(function () {
      $(this)
      .find(".widget-menu-tab")
      .children(".item-title")
      .on("click", function () {
        var liActive = $(this).index();
        var contentActive = $(this)
        .siblings()
        .removeClass("active")
        .parents(".widget-tabs")
        .find(".widget-content-tab")
        .children()
        .eq(liActive);
        contentActive.addClass("active").fadeIn("slow");
        contentActive.siblings().removeClass("active");
        $(this).addClass("active").parents(".widget-tabs").find(
            ".widget-content-tab").children().eq(liActive);
      });
    });
  };

  /* Text Rotate
  -------------------------------------------------------------------------*/
  var textRotate = function () {
    if ($(".wg-curve-text").length > 0) {
      $(".text-rotate").each(function () {
        const $textRotate = $(this);
        const text = $textRotate.attr("data-text") || "";
        const chars = text.split("");
        const degree = 360 / chars.length;
        $textRotate.find(".text").each(function () {
          const $circularText = $(this);
          $circularText.empty();
          chars.forEach((char, i) => {
            const $span = $("<span></span>")
            .text(char)
            .css({
              transform: `rotate(${i * degree}deg)`,
            });
            $circularText.append($span);
          });
        });
      });
    }
  };

  /* Custom Dropdown
  -------------------------------------------------------------------------*/
  var customDropdown = function () {
    function updateDropdownClass() {
      const $dropdown = $(".dropdown-custom");

      if ($(window).width() <= 991) {
        $dropdown.addClass("dropup").removeClass("dropstart");
      } else {
        $dropdown.addClass("dropstart").removeClass("dropup");
      }
    }

    updateDropdownClass();
    $(window).resize(updateDropdownClass);
  };

  /* Range Size
  -------------------------------------------------------------------------*/
  var rangeSize = function () {
    $(".widget-size").each(function () {
      var $rangeInput = $(this).find(".range-input input");
      var $progress = $(this).find(".progress-size");
      var $maxPrice = $(this).find(".max-size");

      $rangeInput.on("input", function () {
        var maxValue = parseInt($rangeInput.val(), 10);
        var percentMax = (maxValue / $rangeInput.attr("max")) * 100;
        $progress.css("width", percentMax + "%");

        $maxPrice.html(maxValue);
      });
    });
  };

  /* Bottom Sticky
  --------------------------------------------------------------------------------------*/
  var scrollBottomSticky = function () {
    if ($("footer").length > 0) {
      $(window).on("scroll", function () {
        var scrollPosition = $(this).scrollTop();
        var myElement = $(".tf-sticky-btn-atc");
        var footerOffset = $("footer").offset().top;
        var windowHeight = $(window).height();

        if (scrollPosition >= 500 && scrollPosition + windowHeight
            < footerOffset) {
          myElement.addClass("show");
        } else {
          myElement.removeClass("show");
        }
      });
    }
  };

  /* Write Review
  ------------------------------------------------------------------------------------- */
  var writeReview = function () {
    if ($(".write-cancel-review-wrap").length > 0) {
      $(".btn-comment-review").click(function () {
        $(this).closest(".write-cancel-review-wrap").toggleClass(
            "write-review");
      });
    }
  };

  /* Video
  ------------------------------------------------------------------------------------- */
  var videoWrap = function () {
    if ($("div").hasClass("video-wrap")) {
      $(".popup-youtube").magnificPopup({
        type: "iframe",
      });
    }
  };

  /* Show Password
  -------------------------------------------------------------------------*/
  var showPassword = function () {
    $(".toggle-pass").on("click", function () {
      const wrapper = $(this).closest(".password-wrapper");
      const input = wrapper.find(".password-field");
      const icon = $(this);

      if (input.attr("type") === "password") {
        input.attr("type", "text");
        icon.removeClass("icon-show-password").addClass("icon-view");
      } else {
        input.attr("type", "password");
        icon.removeClass("icon-view").addClass("icon-show-password");
      }
    });
  };

  /* Change Image Dashboard
  -------------------------------------------------------------------------*/

  // ===== Hàm chung để preview ảnh =====
  function previewImage(file, targetImgSelector) {
    if (!file) {
      return;
    }

    const reader = new FileReader();
    reader.onload = function (e) {
      $(targetImgSelector).attr("src", e.target.result);
    };
    reader.readAsDataURL(file);
  }

// ===== Hàm chỉ preview (dùng trong form ProductImageListDto) =====
  // ===== Hàm preview ảnh theo index =====
  function initImagePreview() {
    // Gắn sự kiện theo kiểu delegation
    $(document).on("change", ".preview-input", function (e) {
      const file = e.target.files[0];
      if (file) {
        const reader = new FileReader();
        reader.onload = function (evt) {
          // lấy index từ name, ví dụ: images[2].image
          const match = $(e.target).attr("name").match(/images\[(\d+)\]/);
          const idx = match ? match[1] : "?";

          // log thử cho chắc
          console.log("Preview image index:", idx);

          // tìm đúng <img> cùng "khối" cha
          $(e.target)
          .closest(".product-image-preview")
          .find(".preview-img")
          .attr("src", evt.target.result)
          .attr("data-index", idx); // optional: gắn index vào ảnh
        };
        reader.readAsDataURL(file);
      }
    });
  }

// ===== Hàm preview + upload (dùng chỗ khác, vd: avatar user) =====
  var changeImageDash = function () {
    $(".changeImgDash").on("click", function () {
      $(".fileInputDash").click();
    });

    $(".fileInputDash").on("change", function (e) {
      const file = e.target.files[0];
      if (file) {
        // Preview
        previewImage(file, ".imgDash");

        // Upload
        const formData = new FormData();
        formData.append("file", file);

        const token = $("meta[name='_csrf']").attr("content");
        const header = $("meta[name='_csrf_header']").attr("content");

        fetch("/user/upload-image", {
          method: "POST",
          headers: {
            [header]: token
          },
          body: formData
        })
        .catch(err => console.error(err));
      }
    });
  };

  /* No Action Link
  -------------------------------------------------------------------------*/
  const preventDefault = () => {
    $('a[href="#"]').on("click", function (e) {
      e.preventDefault();
    });
  };
  /* No Action Link
  -------------------------------------------------------------------------*/
  var notifyForm = () => {
    $("#btnLogin").on("click", function (e) {
      e.preventDefault();

      const form = $(this).closest("form");
      let firstEmptyInput = null;
      let isValid = true;

      form.find("input").each(function () {
        if ($(this).val().trim() === "") {
          firstEmptyInput = $(this);
          isValid = false;
          return false;
        }
      });

      if (isValid) {
        form.submit();
      } else {
        alert("Please fill in all required fields!");
        firstEmptyInput.focus();
      }
    });
  };

  /* Select Category
  -------------------------------------------------------------------------*/
  var customSelect = function () {
    $("select#product_cat").each(function () {
      var $this = $(this),
          selectOptions = $(this).children("option").length;
      $this.addClass("hide-select");
      $this.after('<div class="tf-select-custom"></div>');
      var $customSelect = $this.next("div.tf-select-custom");
      $customSelect.text($this.children("option").eq(0).text());
      var $optionlist = $(
          '<ul class="select-options" /><div class="header-select-option"><span>Select Categories</span><span class="close-option"><i class="icon-close"></i></div>'
      ).insertAfter($customSelect);
      for (var i = 0; i < selectOptions; i++) {
        $("<li />", {
          text: $this.children("option").eq(i).text(),
          rel: $this.children("option").eq(i).val(),
        }).appendTo($optionlist);
      }
      var $optionlistItems = $optionlist.children("li");
      $customSelect.click(function (e) {
        e.stopPropagation();
        $("div.tf-select-custom.active")
        .not(this)
        .each(function () {
          $(this).removeClass("active").next("ul.select-options").hide();
        });
        $(this).toggleClass("active").next("ul.select-options").slideToggle();
      });
      $optionlistItems.click(function (e) {
        e.stopPropagation();
        $customSelect.text($(this).text()).removeClass("active");
        $this.val($(this).attr("rel"));
        $optionlist.hide();
      });
      $(document).click(function () {
        $customSelect.removeClass("active");
        $optionlist.hide();
      });
      $(".close-option").click(function () {
        $customSelect.removeClass("active");
        $optionlist.hide();
      });
    });
  };
  /* Hover Pin
-------------------------------------------------------------------------*/
  var hoverPin = function () {
    $(".tf-lookbook-hover").each(function () {
      const $container = $(this);

      $container.find(".bundle-pin-item").on("mouseover", function () {
        const $hoverWrap = $container.find(".bundle-hover-wrap");
        $hoverWrap.addClass("has-hover");

        const $el = $container.find("." + this.id).show();
        $hoverWrap.find(".bundle-hover-item").not($el).addClass("no-hover");
      });

      $container.find(".bundle-pin-item").on("mouseleave", function () {
        const $hoverWrap = $container.find(".bundle-hover-wrap");
        $hoverWrap.removeClass("has-hover");
        $hoverWrap.find(".bundle-hover-item").removeClass("no-hover");
      });
    });
  };
  /* Preloader
  -------------------------------------------------------------------------*/
  var preloader = function () {
    $("#preload").fadeOut("slow", function () {
      var $this = $(this);
      setTimeout(function () {
        $this.remove();
      }, 300);
    });
  };
  /* RTL
------------------------------------------------------------------------------------- */
  var RTL = function () {
    var isRTL = $("body").hasClass("rtl") || localStorage.getItem("dir")
        === "rtl";

    if (isRTL) {
      $("html").attr("dir", "rtl");
      $("body").addClass("rtl");
      $("#toggle-rtl").text("ltr");

      $(".tf-btn,.tf-btn-link").find(".icon").removeClass(
          "icon-arrow-right").addClass("icon-arrow-left");
      $(".nav-shop_link").find(".icon2").removeClass(
          "icon-caret-right").addClass("icon-caret-left");
      $(".pagination-item .icon").each(function () {
        const $icon = $(this);
        if ($icon.hasClass("icon-caret-right")) {
          $icon.removeClass("icon-caret-right").addClass("icon-caret-left");
        } else if ($icon.hasClass("icon-caret-left")) {
          $icon.removeClass("icon-caret-left").addClass("icon-caret-right");
        }
      });
      localStorage.setItem("dir", "rtl");
    } else {
      $("html").attr("dir", "ltr");
      $("body").removeClass("rtl");
      $("#toggle-rtl").text("rtl");
      localStorage.setItem("dir", "ltr");
    }
    $("#toggle-rtl").on("click", function () {
      var currentDir = $("html").attr("dir");
      if (currentDir === "rtl") {
        localStorage.setItem("dir", "ltr");
      } else {
        localStorage.setItem("dir", "rtl");
      }
      location.reload();
    });
  };

  // delete search history
  function deleteHistory() {
    $(document).on('submit', '.delete-search-form', function (e) {
      e.preventDefault();

      const form = $(this);
      const url = '/search/delete';

      // Lấy CSRF token
      const token = $('meta[name="_csrf"]').attr('content');
      const header = $('meta[name="_csrf_header"]').attr('content');

      fetch(url, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
          [header]: token
        }
      })
      .then(response => {
        if (response.ok) {
          // Tìm và xóa thẻ chứa history
          const historyItem = form.closest('.view-history-wrap');
          historyItem.fadeOut(300, function () {
            $(this).remove();
          });
        }
      })
      .catch(error => {
        console.error('Error:', error);
      });
    });
  }

  // Dom Ready
  $(function () {
    headerSticky();
    headerFixed();
    dropdownSelect();
    btnQuantity();
    deleteFile();
    deleteWishList();
    goTop();
    variantPicker();
    changeValue();
    sidebarMobile();
    checkClick();
    staggerWrap();
    clickModalSecond();
    autoPopup();
    totalPriceVariant();
    scrollGridProduct();
    handleProgress();
    handleFooter();
    infiniteSlide();
    addWishList();
    handleSidebarFilter();
    estimateShipping();
    textCopy();
    parallaxie();
    tableCompareRemove();
    clickActive();
    handleMobileMenu();
    swatchColor();
    tabs();
    textRotate();
    customDropdown();
    rangeSize();
    scrollBottomSticky();
    writeReview();
    videoWrap();
    showPassword();
    changeImageDash();
    initImagePreview();
    preventDefault();
    notifyForm();
    customSelect();
    hoverPin();
    RTL();
    preloader();
    deleteHistory();
  });
})(jQuery);
