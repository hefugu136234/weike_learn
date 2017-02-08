$(document).ready(function() {
	// activeNav('nav_index');

	// 点击分享
	$('.share-btn').on('click', function() {
		$('#share_process').modal('show');
	});

	$('img.lazyload-img').lazyload({
		placeholder : '/assets/img/grey.gif',
		effect : 'fadeIn',
		container : $('.lazyload-container')
	});

	lightbox.option({
		'resizeDuration' : 200,
		'wrapAround' : true
	});

	var page_reamin_uuid = $('#page_reamin_uuid').val();
	// 页面记录时间对象
	var page_obj = creatPageReamin(page_reamin_uuid, 5);
	// 页面监测
	page_obj.controller(function() {
		return true;
	});
});
