var activity_uuid, activity_description, droploadList;
$(function() {
	activity_uuid = $('#activity_uuid').val();
	activity_description = $('#activity_description').val();
	// 请求是否已经报名
	$.getJSON('/api/webchat/activity/is/sign/up', {
		'uuid' : activity_uuid,
		'timestamp' : new Date().getTime()
	}, function(data) {
		if (isSuccess(data)) {
			if (data.message == 'not_sign') {
				// 活动报名的控制显示
				AlertClassTip(activity_description, {title: '活动简介',option:['关闭']}, function(flag) {
					if (flag == 1) {
						$.post('/api/webchat/activity/sign/up/add', {
							'uuid' : activity_uuid
						}, function(data) {
							if (isSuccess(data)) {
								// 报名成功不做提示
							} else {
								AlertClassTip(data.message);
							}
						});
					}
				});
			}
		}
	});

	//简介详情
	$('.activity-intro-tag').on('click', function(){
    AlertClassTip(activity_description, {title: '活动简介',option:['关闭']});
  });

	// 实例化下拉插件
	droploadList = dataDropload($('#activity_wrapper'), $('#activity_oups_list'),
			'/api/webchat/activity/oups/list/' + activity_uuid, buildDataList,
			commonDropUl,'up');

});

// 实名认证的需要
function isneedName() {
	$.getJSON('/api/webchat/activity/isneed/real/name', {
		'uuid' : activity_uuid,
		'dateTime' : new Date().getTime()
	}, function(data) {
		if (isSuccess(data)) {
			location.href = '/api/webchat/activity/opus/page/' + activity_uuid;
		} else {
			if(data.message=='not login'){
				//登录
				var url='/api/webchat/activity/total/page/'+activity_uuid;
				loginAlert(url);
			}else if(data.message=='need real name'){
				//实名
				AlertClassTip('您必须通过实名认证，才能提交作品', {title: '提交作品',option:['实名认证', '取消']}, function(flag) {
					if (flag == 1) {
						window.location.href = '/api/webchat/activity/real/name/page';
					}
				});
			}else{
				AlertClassTip(data.message);
			}
		}
	});
}

// 必须实现方法
function buildDataList(data) {
	var list = data.items;
	if (isItemList(list)) {
		var listStartTime = list[list.length - 1].updateTime;
		droploadList.listStartTime = listStartTime;
	}
	return list;
}
