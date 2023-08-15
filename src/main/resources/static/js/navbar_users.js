const filter_field = document.getElementById("filter-value");
const filter_field2 = document.getElementById("filter-value2");
let selected_value = "";
let selected_columns = {};
const types = {'id':'number','username':'text','email':'text'};
const row = document.getElementById("row");

const table = new Tabulator("#table", {
  maxHeight: "calc(100% - 110px)",
  layout: "fitColumns",
});

const rangeCheck = document.getElementById("rangeSwitch");

const handler = {
  set(target, prop, nval) {
    if (isNaN(nval)) {
      table.addFilter(prop, ">=", nval[0]);
      table.addFilter(prop, "<=", nval[1]);
    } else {
      table.addFilter(prop, "=", nval);
    }
    target[prop] = nval;
  },
  deleteProperty(target,prop){
    const val = target[prop]
    if (isNaN(val)) {
      table.removeFilter(prop, ">=", val[0]);
      table.removeFilter(prop, "<=", val[1]);
    } else {
      table.removeFilter(prop, "=", val);
    }
    delete target[prop];
    return true;
  }
};

let proxy = new Proxy(selected_columns, handler);

document.getElementById("clear-all").onclick = (e) => {
  row.replaceChildren();
  //proxy = {};
  table.clearFilter();
}

rangeCheck.onclick = (e) => {
  filter_field2.setAttribute('value','');
  if(rangeCheck.value == '0'){
    rangeCheck.value = '1';
    filter_field2.setAttribute("type",(selected_value != '' ? types[selected_value] : 'text'));
  }else{
    rangeCheck.value = "0";
    filter_field2.setAttribute("type", "hidden");
  }
}

const removeBtn = function(elem){
  row.removeChild(elem.parentNode);
  delete proxy[elem.innerText.split(':')[0]];
}
let addFilter = () => {
  if (proxy[selected_value] != undefined) {
    return;
  }
  let value = selected_value + ": " + filter_field.value.trim() + " ";
  if(rangeCheck.value == '1'){
    value += "<=< " + filter_field2.value.trim() + ' ';
    if(types[selected_value] == 'number'){
      proxy[selected_value] = [
        Number.parseInt(filter_field.value.trim()),
        Number.parseInt(filter_field2.value.trim()),
      ];
    }else{
      proxy[selected_value] = [filter_field.value.trim(),filter_field2.value.trim()];
    }
  }else{
    if(types[selected_value] == 'number'){
      proxy[selected_value] = Number.parseInt(filter_field.value.trim());
    }else{
      proxy[selected_value] = filter_field.value.trim();
    }
  }
  const elem = document.createElement("div");
  elem.classList.add("col-auto","mb-3");
  elem.innerHTML =
    '<button type="button" class="btn btn-dark filter-btn" onclick="removeBtn(this)">' +
    value +
    '<i class="fa-solid fa-xmark"></i></button>';
  row.appendChild(elem);
}

document.getElementById("filter-btn").onclick = (e) => {
  if (filter_field.value != null && filter_field.value.trim() != "" && selected_value != '') {
    if (
      rangeCheck.value == "0" ||
      (rangeCheck.value == "1" &&
        filter_field2.value != null &&
        filter_field2.value.trim() != "")
    ) {
      addFilter();
    }
  }
}

[...document.getElementById("selected-filter").children].filter(e => e.tagName != "DIV").forEach((e) => {
  e.addEventListener("click", (ev) => {
    if(selected_value != ev.target.textContent){
      filter_field.setAttribute('value','');
    }
    selected_value = ev.target.textContent;
    filter_field.setAttribute('type',types[selected_value]);
    if(rangeCheck.value == '1'){
      filter_field2.setAttribute("type",types[selected_value]);
    }
    if(filter_field.value != null && filter_field.value.trim() != ""){
      if ( rangeCheck.value == '0' ||
        (rangeCheck.value == "1" &&
        filter_field2.value != null &&
        filter_field2.value.trim() != "")
      ){
        addFilter();
      }
    }
  });
});
