document.addEventListener("DOMContentLoaded", function () {
  const colorList = document.getElementById("color-list");
  const addBtn = document.getElementById("btn-add-color");

  function reindexColors() {
    const items = colorList.querySelectorAll(".color-item");
    items.forEach((item, index) => {
      item.querySelectorAll("input").forEach(input => {
        if (input.type === "text" && !input.classList.contains("color-code")) {
          input.name = `colors[${index}].name`;
        } else if (input.type === "color") {
          input.name = `colors[${index}].code`;
        } else if (input.classList.contains("color-code")) {
          input.removeAttribute("name"); // chỉ hiển thị hex, không gửi về server
        }
      });
    });
  }

  addBtn.addEventListener("click", function () {
    const newItem = document.createElement("div");
    newItem.classList.add("color-item", "tf-grid-layout", "sm-col-3", "my-1");

    newItem.innerHTML = `
      <fieldset>
        <input type="text" placeholder="Color name (Eg: Red, Black)" />
      </fieldset>
      <fieldset>
        <div class="tf-grid-layout sm-col-2">
          <input class="color-code" placeholder="#rrggbb" type="text">
          <span class="color-circle">
            <input type="color" />
          </span>
        </div>
      </fieldset>
      <button class="btn-remove btn-blue tf-btn animate-btn w-maxcontent fw-bold" type="button">Delete</button>
    `;

    colorList.appendChild(newItem);

    const picker = newItem.querySelector("input[type=color]");
    const codeInput = newItem.querySelector(".color-code");

    // khi chọn màu -> update text
    picker.addEventListener("input", function () {
      codeInput.value = picker.value;
    });

    // khi gõ hex -> update picker (nếu hợp lệ)
    codeInput.addEventListener("input", function () {
      const val = codeInput.value.trim();
      if (/^#([0-9A-Fa-f]{6})$/.test(val)) {
        picker.value = val;
      }
    });

    reindexColors();
  });

  colorList.addEventListener("click", function (e) {
    if (e.target.classList.contains("btn-remove")) {
      e.target.closest(".color-item").remove();
      reindexColors();
    }
  });

  reindexColors();
});

// Khi chọn màu -> cập nhật text
function syncToText(colorInput, index) {
  const textInput = document.getElementById("colorCode-" + index);
  if (textInput) {
    textInput.value = colorInput.value;
  }
}

// Khi gõ vào text -> cập nhật color input
function syncToColor(textInput, index) {
  const colorInput = document.getElementById("colorPicker-" + index);
  if (colorInput) {
    const val = textInput.value.trim();
    // validate hex: phải dạng #rrggbb
    if (/^#([0-9A-Fa-f]{6})$/.test(val)) {
      colorInput.value = val;
    }
  }
}

document.addEventListener("DOMContentLoaded", function () {
  const sizeList = document.getElementById("size-list");
  const addSizeBtn = document.getElementById("btn-add-size");

  function reindexSizes() {
    const items = sizeList.querySelectorAll(".size-item");
    items.forEach((item, index) => {
      const input = item.querySelector("input[type='text']");
      if (input) {
        input.name = `sizes[${index}].name`;
      }
    });
  }

  addSizeBtn.addEventListener("click", function () {
    const newItem = document.createElement("div");
    newItem.classList.add("size-item", "tf-grid-layout", "sm-col-2", "my-1");
    newItem.innerHTML = `
        <fieldset>
          <input type="text" name="" placeholder="Size name (Eg: S, M, L)" />
        </fieldset>
        <button class="btn-remove btn-blue tf-btn animate-btn w-maxcontent fw-bold" type="button">Delete</button>
      `;
    sizeList.appendChild(newItem);
    reindexSizes();
  });

  sizeList.addEventListener("click", function (e) {
    if (e.target.classList.contains("btn-remove")) {
      e.target.closest(".size-item").remove();
      reindexSizes();
    }
  });

  // Khởi tạo index ban đầu
  reindexSizes();
});

document.addEventListener("DOMContentLoaded", function () {
  const categoryList = document.getElementById("category-list");
  const addCategoryBtn = document.getElementById("btn-add-category");
  const categoryTemplate = document.getElementById("category-template");

  // Hàm tạo 1 item category mới
  function createCategoryItem() {
    const index = categoryList.querySelectorAll(".category-item").length;

    // Tạo div chứa item
    const div = document.createElement("div");
    div.classList.add("category-item", "size-item", "tf-grid-layout", "sm-col-2", "my-1");

    div.innerHTML = `
      <fieldset>
        <label for="categoryIdList${index}">Category</label>
        <div class="tf-select">
          <select class="w-100" id="categoryIdList${index}" name="categoryIdList[${index}]" required>
            ${categoryTemplate.innerHTML}
          </select>
        </div>
      </fieldset>
      <div class="d-flex align-items-end">
        <button class="btn-remove btn-blue tf-btn animate-btn w-maxcontent fw-bold" type="button">
          Delete
        </button>
      </div>
    `;

    // Gán sự kiện xóa
    div.querySelector(".btn-remove").addEventListener("click", function () {
      div.remove();
      reIndexCategories();
    });

    return div;
  }

  // Hàm re-index lại name + id sau khi xóa
  function reIndexCategories() {
    const items = categoryList.querySelectorAll(".category-item");
    items.forEach((item, i) => {
      const select = item.querySelector("select");
      const label = item.querySelector("label");

      select.name = `categoryIdList[${i}]`;
      select.id = `categoryIdList${i}`;
      label.setAttribute("for", `categoryIdList${i}`);
    });
  }

  // Thêm mới
  addCategoryBtn.addEventListener("click", function () {
    const newItem = createCategoryItem();
    categoryList.appendChild(newItem);
    reIndexCategories();
  });

  // Xóa cho các item đã render sẵn từ server
  categoryList.querySelectorAll(".category-item .btn-remove").forEach((btn) => {
    btn.addEventListener("click", function () {
      btn.closest(".category-item").remove();
      reIndexCategories();
    });
  });
});

function updateSalePrice() {
  const price = parseFloat(document.getElementById("price").value) || 0;
  const percent = parseFloat(document.getElementById("salePercentage").value)
      || 0;
  const salePrice = price - (price * percent / 100);

  document.getElementById("salePrice").value = salePrice > 0 ? salePrice : "";
}
