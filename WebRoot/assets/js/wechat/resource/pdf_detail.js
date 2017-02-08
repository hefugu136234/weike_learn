$(function(){
	$('img').lazyload({
		placeholder : "/assets/img/grey.gif",
		effect : "fadeIn",
		container : $("#page")
	});

	var imgsObj = $('img');
	var imgs = new Array();
	for (var i = 0; i < imgsObj.size(); i++) {
		imgs.push(imgsObj.eq(i).attr('data-original'));
	}

	$('img').click(function(event) {
		event.preventDefault();
		WeixinJSBridge.invoke('imagePreview', {
			'current' : $(this).attr('src'),
			'urls' : imgs
		});
	});
	
	var page_reamin_uuid = $('#page_reamin_uuid').val();
	// 页面记录时间对象
	var page_obj = creatPageReamin(page_reamin_uuid, 5);
	// 页面监测
	page_obj.controller(function() {
		return true;
	});
});
//新js原 pdf_view.js