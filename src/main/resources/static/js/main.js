const filter_field = document.getElementById("filter-value");
let selected_value = "";
const row = document.getElementById("row");
const removeBtn = function(elem){
  if(elem.tagName == "button"){
    row.removeChild(elem);
  }else{
    row.removeChild(elem.parentNode);
  }
}
let addFilter = (e) => {
  let elem = document.createElement("div");
  elem.classList.add("col-auto","mb-3");
  elem.innerHTML =
    '<button type="button" class="btn btn-dark filter-btn" onclick="removeBtn(this)">' +
    e +
    '<i class="fa-solid fa-xmark"></i></button>';
  row.appendChild(elem);
}

document.getElementById("filter-btn").onclick = (e) => {
  if (filter_field.value != null && filter_field.value.trim() != "" && selected_value != "") {
    addFilter(selected_value + ": " + filter_field.value.trim() + " ");
  }
}
[...document.getElementById("selected-filter").children].forEach((e) => {
  e.addEventListener("click", (ev) => {
    selected_value = ev.target.textContent;
    if(filter_field.value != null && filter_field.value.trim() != ""){
      addFilter(selected_value + ": " + filter_field.value.trim() + " ");
    }
  });
});
