$("form").on("submit", function (e) {
  e.preventDefault();
  const url = $(this).attr("action");
  const formData = $(this).serialize();
  $.ajax({
    type: "POST",
    url: url,
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
