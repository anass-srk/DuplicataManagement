const { jsPDF } = window.jspdf;
const bootstrap = window.bootstrap;
const modal = document.getElementById("myModal");
const myModal = bootstrap.Modal.getOrCreateInstance(modal);
let img = new Image();
img.src = '/img/electricity.jpg';
let X = [140,155,164,173,136,136,136,145,127,54,66,79,66,40,63,90
,98,98,98,98,98,98
,118,118,118,118,118,118
,140,140,140,140,140,140
,178,178,178,178,178,178
,178,178,178
,140,140,140
,140,140,140,140,140];
let Y = [11,11,11,11,18,28,33,41,53,20,29,29,36,47,47,47
,95,100,105,110,115,120
,95,100,105,110,115,120
,95,100,105,110,115,120
,95,100,105,110,115,120
,132,135,138
,132,135,138
,151,155,159,164,168];
Y = Y.map(y => y + 4);
let exportPdf;
let exportAll;
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
  let info = ["LOC","SECT","TRN","ORD","NOM","ADRESSE","ADRESSE2",
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
  //exportPDF(info);
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