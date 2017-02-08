$(function() {
	$('#three_play').click(function(e) {
		e.preventDefault();
		// 提示
		var res_page_uuid=$('#resource_uuid').val();
		var url='/api/webchat/resource/first/view/'+res_page_uuid;
		loginAlert(url);
	});

});

