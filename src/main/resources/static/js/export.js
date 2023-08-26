const { jsPDF } = window.jspdf;
const bootstrap = window.bootstrap;
const modal = document.getElementById("myModal");
const myModal = bootstrap.Modal.getOrCreateInstance(modal);
let img_electricity = new Image();
let img_water = new Image();
img_electricity.src = '/img/electricity.jpg';
img_water.src = '/img/water.jpg';
let X_E = [140,155,164,173,136,136,136,145,127,54,66,79,66,40,63,90
,98,98,98,98,98,98
,118,118,118,118,118,118
,140,140,140,140,140,140
,178,178,178,178,178,178
,178,178,178
,140,140,140
,140,140,140,140,140];
let Y_E = [11,11,11,11,18,28,33,41,53,20,29,29,36,47,47,47
,95,100,105,110,115,120
,95,100,105,110,115,120
,95,100,105,110,115,120
,95,100,105,110,115,120
,132,135,138
,132,135,138
,151,155,159,164,168];
Y_E = Y_E.map(y => y + 4);
let X_W = [140,151,162,173,140,140,140,144,131,
56,71,84,54,45,69,95,
99,99,99,99,99,99,99,99,
120,120,120,120,120,120,120,120,
143,143,143,143,143,143,143,143,
180,180,180,180,180,180,180,180,
180,180,
143,143,143,143,143,143
];
let Y_W = [13,13,13,13,20,30,35,43,55,
22,32,32,37,49,49,49,
94,98,103,107,112,120,124,128,
94,98,103,107,112,120,124,128,
94,98,103,107,112,120,124,128,
94,98,103,107,112,120,124,128,
136,140,
136,140,145,150,155,161
];
Y_W = Y_W.map(y => y + 4);
let exportPdf;
let exportAll;
let X = X_W;
let Y = Y_W;
let img = img_water;
function changeType(type){
  if(type == "WATER"){
    X = X_W;
    Y = Y_W;
    img = img_water;
  }else{
    X = X_E;
    Y = Y_E;
    img = img_electricity;
  }
}
window.onload = (e) => {
  function exportPDF(info,name) {
    let doc = new jsPDF();
    doc.addImage(img, 'jpg', 0, 0, 210, 297);
    doc.setFontSize(8);
    for (let i = 0; i < X.length; ++i) {
      doc.text(info[i], X[i], Y[i]);
    }
    doc.text("-",X[10]+10,Y[10])
    doc.save(`${name}.pdf`);
  }
  exportPdf = exportPDF;
  exportAll = function(){
    const data = JSON.parse(sessionStorage.getItem("duplicatas"));
    let doc = new jsPDF();

    for(let j = 0;j < data.length;++j){
      doc.addPage();
      doc.addImage(img, "jpg", 0, 0, 210, 297);
      doc.setFontSize(8);
      for (let i = 0; i < X.length; ++i) {
        doc.text(data[j].data[i], X[i], Y[i]);
      }
      doc.text("-", X[10] + 10, Y[10]);
    }

    doc.deletePage(1);
    doc.save("invoice.pdf");
  }
  let info1 = ["LOC","SECT","TRN","ORD","NOM","ADRESSE","ADRESSE2",
  "POLICE","NCOMPT","FACTUREN","MOIS","ANNE","USAGE",
  "IDX1","IDX2","CONS"
  ,"CONSTR1","CONSTR2","CONSTR3","CONSTR4","CONSTR5","CONSTR6"
  ,"PRIX1","PRIX2","PRIX3","PRIX4","PRIX5","PRIX6"
  ,"MNT1","MNT2","MNT3","MNT4","MNT5","MNT6"
  ,"MNTTVA1","MNTTVA2","MNTTVA3","MNTTVA4","MNTTVA5","MNTTVA6"
  ,"TVA_RDF","TVA_ENT","TVA_LOC"
  ,"RDF","TAXE_ENT","TAXE_LOC"
  ,"MNT_HT","TVA","TPPAN","TIMBRE","NET_APAYER"
  ];
  let info = ["LOC","SECT","TRN","ORD","NOM","ADRESSE","ADRESSE2",
  "POLICE","NCOMPT","FACTUREN","MOIS","ANNE","USAGE","IDX1","IDX2","CONS",
  "CONSTR1","CONSTR2","CONSTR3","CONSTR4","CONSTR5",
  "CONSTRA1","CONSTRA2","CONSTRA3",
  "PRIX1","PRIX2","PRIX3","PRIX4","PRIX5",
  "PRIXA1","PRIXA2","PRIXA3",
  "MNTHT1","MNTHT2","MNTHT3","MNTHT4","MNTHT5",
  "MNTHTA1","MNTHTA2","MNTHTA3",
  "MNTTVA1","MNTTVA2","MNTTVA3","MNTTVA4","MNTTVA5",
  "MNTTVAA1","MNTTVAA2","MNTTVAA3",
  "tva-rdf_eau","tva_rdf_ass","red_fix_eau","red_fix_ass",
  "mont-ht","tva","timbre","NET"
  ];
  $("form").on("submit", function (e) {
    e.preventDefault();
    const url = $(this).attr("action");
    const data = $(this).serialize();
    $.ajax({
      type: "POST",
      url: url,
      data: data,
      success: function (data) {
        sessionStorage.setItem("duplicatas",JSON.stringify(data));
        changeType(data[0].type);
        if(data.length == 1){
          exportPDF(data[0].data,data[0].data[11] + "/" + data[0].data[10]);
        }else{
          const parent = document.getElementById("invoice-container");
          parent.innerHTML = "";
          for(let i = 0;i < data.length;++i){
            let div = document.createElement("div");
            div.className = "card";
            div.style.width = "9rem";
            div.style.margin = "1%";
            div.innerHTML =
              '<img src="/img/electricity-bill.png" class="card-img-top" alt="...">' +
              '<div class="card-body">' +
              `  <h5 class="card-title">${data[i].title}</h5>` +
              `  <a class="btn btn-primary btn-sm" onclick="exportPdf(${data[i].data},${data[i].title})">Download</a>` +
              "</div>";
            parent.appendChild(div);
          }
          myModal.show();
        }
      },
      error: function (data) {
        const err = $("#error");
        err.text(data.responseText);
        err.show();
      },
    });
  });
}