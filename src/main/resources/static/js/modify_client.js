const localite1 = document.getElementById("loc1");
const police1 = document.getElementById("pol1");
const parent1 = document.getElementById("list-info1");

class Point {
  constructor(x, y) {
    this.x = x;
    this.y = y;
  }
}

let list1 = [...parent1.children].map(ul => {
  return {
    x: Number(ul.children[0].innerHTML),
    y: Number(ul.children[1].innerHTML)
  };
});

function draw1() {
  parent1.innerHTML = "";
  list1.forEach((p) => {
    const elem = document.createElement("ul");
    elem.setAttribute("class", "list-group list-group-horizontal");
    elem.innerHTML =
      `<li class="list-group-item">${p.x}</li>` +
      `<li class="list-group-item">${p.y}</li>`;
    parent1.appendChild(elem);
  });
}
$("#btn-rem1").click(function() {
  if (isNaN(localite1.valueAsNumber) || isNaN(police1.valueAsNumber)) return;
  list1 = list1.filter(
    (e) => e.x != localite1.valueAsNumber || e.y != police1.valueAsNumber
  );
  draw1();
});

$("#btn-add1").click(function() {
  if (isNaN(localite1.valueAsNumber) || isNaN(police1.valueAsNumber)) return;
  if (
    list1.filter(
      (e) => e.x == localite1.valueAsNumber && e.y == police1.valueAsNumber
    ).length == 0
  ) {
    list1.push(new Point(localite1.valueAsNumber, police1.valueAsNumber));
    draw1();
  }
});

const localite2 = document.getElementById("loc2");
const police2 = document.getElementById("pol2");
const parent2 = document.getElementById("list-info2");

let list2 = [...parent2.children].map((ul) => {
  return {
    x: Number(ul.children[0].innerHTML),
    y: Number(ul.children[1].innerHTML),
  };
});

function draw2() {
  parent2.innerHTML = "";
  list2.forEach((p) => {
    const elem = document.createElement("ul");
    elem.setAttribute("class", "list-group list-group-horizontal");
    elem.innerHTML =
      `<li class="list-group-item">${p.x}</li>` +
      `<li class="list-group-item">${p.y}</li>`;
    parent2.appendChild(elem);
  });
}

$("#btn-rem2").click(function () {
  if (isNaN(localite2.valueAsNumber) || isNaN(police2.valueAsNumber)) return;
  list2 = list2.filter(
    (e) => e.x != localite2.valueAsNumber || e.y != police2.valueAsNumber
  );
  draw2();
});

$("#btn-add2").click(function () {
  if (isNaN(localite2.valueAsNumber) || isNaN(police2.valueAsNumber)) return;
  if (
    list2.filter(
      (e) => e.x == localite2.valueAsNumber && e.y == police2.valueAsNumber
    ).length == 0
  ) {
    list1.push(new Point(localite2.valueAsNumber, police2.valueAsNumber));
    draw2();
  }
});

$("form").on("submit", function (e) {
  e.preventDefault();
  const url = $(this).attr("action");
  let formData = $(this).serialize();
  console.log(formData);
  $.ajax({
    type: "POST",
    url: url + 
    "&l1=" + list1.map(p => p.x).join(",") +
    "&p1=" + list1.map(p => p.y).join(",") +
    "&l2=" + list2.map(p => p.x).join(",") +
    "&p2=" + list2.map(p => p.y).join(","),
    data: formData,
    success: function (data) {
      window.location.href = window.location.origin + "/" + data;
    },
    error: function (data) {
      const err = $("#error");
      err.text(data.responseText);
      err.show();
    },
  });
});
