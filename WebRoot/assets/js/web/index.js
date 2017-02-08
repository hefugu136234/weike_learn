$(document).ready(function(){
  var data_error = $('#data_error').val();
  if (!!data_error) {
    alert(data_error);
  }

  activeNav('nav_index');

  // 板块图标
  $('#index_plate').find('.title-tag').each(function(){
    var $that = $(this);
    var $icon = $that.find('.icon');
    itemAddIcons($that, $icon);
  });
});
