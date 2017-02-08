var re_uuid, player;
var firstView = true;// 第一次点击播放增加积分
$(document).ready(function() {

	re_uuid = $('#res_uuid').val();
	var fileId = $('#fileId').val();

	// 视频播放
	var video_width = $('#video_container').outerWidth();
	var video_height = 544;
	var video_option = {
		'auto_play' : '0',
		"file_id" : fileId,
		'app_id' : '1251442335',
		'width' : video_width,
		'height' : video_height,
		'disable_full_screen' : 1,
		'stretch_full' : 1,
		'WMode' : 'opaque',
		'remember' : 0
	};
	
	var page_reamin_uuid=$('#page_reamin_uuid').val();
	//页面记录时间对象
	var page_obj=creatPageReamin(page_reamin_uuid,5);
	//增加积分对象
	var jifen_obj=creatResourseJifen(re_uuid);
	player = new qcVideo.Player('video_container', video_option, {
		'playStatus' : function(status) {
			//console.log(status);
			if (status == 'ready') {
				// ready，什么都不做
			} else if (status == 'seeking') {
				// 搜索时，什么都不做
			} else if (status == 'playing') {
				//页面监测
				page_obj.controller(function(){
					return player.isPlaying();
				});
				//第一次点击播放增加积分
				jifen_obj.request();
			} else if (status == 'suspended' || status == 'playEnd') {
				// 什么都不做
			}
		}
	});

	// 分享
	$('.share-btn').on('click', function() {
		$('#share_process').modal('show');
	});


});
