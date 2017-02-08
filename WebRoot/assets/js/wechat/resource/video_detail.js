//首次播放增加积分操作
//新js原 video_view.js
var resource_uuid, player, resource_bloody;
$(function() {
	// 定义全局变量
	resource_uuid = $('#resource_uuid').val();
	resource_bloody = $('#resource_bloody').val();
	var resource_type = $('#resource_type').val();
	var resource_pre=$('#resource_pre');
	var bloodyTips = bloodyAlert(resource_bloody);
	var id_video_container=$('#id_video_container');
	//观看权限
	var res_auth_view=$('#res_auth_view').val();
	var res_auth_view_val=$('#res_auth_view_val').val();
	var res_auth_view_login=$('#res_auth_view_login').val();//需要登录
	if (resource_type == 'VIDEO') {
		var fileId = $('#resource_fileId').val();
		// 绘制视频信息
		var option = {
			'auto_play' : '0',
			'file_id' : fileId,
			'app_id' : '1251442335',
			'width' : 640,
			'height' : 480,
			'disable_full_screen' : 0,
			'stretch_full' : 1,
			'WMode' : 'opaque',
			'remember' : 0
		};

		var page_reamin_uuid = $('#page_reamin_uuid').val();
		// 页面记录时间对象
		var page_obj = creatPageReamin(page_reamin_uuid, 5);
		// 增加积分对象
		var jifen_obj = creatResourseJifen(resource_uuid);

		player = new qcVideo.Player('id_video_container', option, {
			'playStatus' : function(status) {
				// console.log(status);
				if (status == 'ready') {
					// ready，什么都不做
				} else if (status == 'seeking') {
					// 搜索时，什么都不做
				} else if (status == 'playing') {
					// 页面监测
					page_obj.controller(function() {
						return player.isPlaying();
					});
					// 第一次点击播放增加积分
					jifen_obj.request();

					
				} else if (status == 'suspended' || status == 'playEnd') {
					// 什么都不做
				} else if (status == 'stop') {
					// 试看结束
				}
			}
		});
		if(resource_bloody=='1'||res_auth_view=='0'){
			id_video_container.addClass('hide');
			resource_pre.removeClass('hide');
		}
	}

	$('#vr_play_btn').click(function(){
		if(resource_type == 'VIDEO'&&res_auth_view==='0'){
			if(res_auth_view_login=='1'){
				//登录
				var url='/api/webchat/resource/first/view/'+resource_uuid;
				var message='未登录用户，无权限观看视频，需要登陆吗？';
				loginAlertCommon(url,message);
			}else{
				//权限控制
				AlertClassTip(res_auth_view_val);
			}
			return ;
		}
		if(resource_type == 'VIDEO'&&resource_bloody=='1'){
			bloodyTips.alertCtrl(function(type){
				id_video_container.removeClass('hide');
				resource_pre.addClass('hide');
				if (type == 1) {
					player.play(0);
				}
			});
			return ;
		} 
		if(resource_type == 'VR'){
			if(resource_bloody=='1'){
				bloodyTips.alertCtrl(
						function (type){
							if (type == 1 || type == 3){
								window.location.href = '/api/webchat/resource/view/detail/' + resource_uuid;
							}
						}
					);
			}else{
				window.location.href = '/api/webchat/resource/view/detail/' + resource_uuid;
			}
			return ;
		}
	});

});
