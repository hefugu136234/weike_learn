$(document).ready(function(){
  activeNav('nav_activity');

  // 直播倒计时
  $('.countdown').each(function () {
    var $that = $(this);
    var intDiff = parseInt($that.data('times'));

    countDownTimer($that, intDiff);
  });
  
  var uuid=$('#broadcast_uuid').val();
	$('#plat_redirect').click(function(){
		$.getJSON('/f/web/live/click/view',{'uuid':uuid},
				function(data){
			if (isSuccess(data)) {
				location.href=data.message;
			}else{
				alert(data.message);
			}
		});
	});
});
