$(document).ready(function () {
  var province = $('#userProvince');
  var city = $('#userCity');
  var hospital = $('#userHospital');

  province.on('change', function (e) {
    e.preventDefault();
    var provinceId = $(this).val();
    var url = '/get_cities?provinceId=' + provinceId;
    $.get(url, function (data) {
      if (data.status == 'ok') {
        city.empty();
        $.each(data.cities, function (index, value) {
          city.append("<option value=" + value[1] + ">" + value[0] + "</option>");
        })
      } else {
        alert(data.err_msg);
      }
      getHospitals();
    });
  });

  city.on('change', function (e) {
    e.preventDefault();
    var cityId = $('#userCity').val();
    var url = '/get_hospitals?cityId=' + cityId;
    $.get(url, function (data) {
      if (data.status == 'ok') {
        hospital.empty();
        $.each(data.hospitals, function (index, value) {
          hospital.append("<option value=" + value[1] + ">" + value[0] + "</option>");
        })
      } else {
        alert(data.err_msg);
      }
    });
  });

  function getHospitals() {
    var cityId = $('#userCity').val();
    var url = '/get_hospitals?cityId=' + cityId;
    $.get(url, function (data) {
      if (data.status == 'ok') {
        hospital.empty();
        $.each(data.hospitals, function (index, value) {
          hospital.append("<option value=" + value[1] + ">" + value[0] + "</option>");
        })
      } else {
        alert(data.err_msg);
      }
    });
  }

});
