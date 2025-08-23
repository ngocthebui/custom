document.addEventListener("DOMContentLoaded", function () {
  const colorList = document.getElementById("color-list");
  const addBtn = document.getElementById("btn-add-color");

  function reindexColors() {
    const items = colorList.querySelectorAll(".color-item");
    items.forEach((item, index) => {
      // input name sẽ thành colors[index].name, colors[index].code
      item.querySelectorAll("input").forEach(input => {
        if (input.type === "text") {
          input.name = `colors[${index}].name`;
        } else if (input.type === "color") {
          input.name = `colors[${index}].code`;
        }
      });
    });
  }

  addBtn.addEventListener("click", function () {
    const newItem = document.createElement("div");
    newItem.classList.add("color-item", "tf-grid-layout", "sm-col-3", "my-1");
    newItem.innerHTML = `
      <fieldset>
        <input type="text" name="" placeholder="Tên màu (VD: Đỏ, Đen)" />
      </fieldset>
      <fieldset>
        <span class="color-circle">
          <input type="color" name="" />
        </span>
      </fieldset>
      <button class="btn-remove btn-blue tf-btn animate-btn w-maxcontent fw-bold" type="button">Delete</button>
    `;
    colorList.appendChild(newItem);
    reindexColors();
  });

  colorList.addEventListener("click", function (e) {
    if (e.target.classList.contains("btn-remove")) {
      e.target.closest(".color-item").remove();
      reindexColors();
    }
  });

  // Khởi tạo index ban đầu
  reindexColors();
});

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
