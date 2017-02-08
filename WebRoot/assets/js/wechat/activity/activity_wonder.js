var activity_dropload;
$(function(){
	addActiveClass('jchd');
	var emptyArea = $('.empty-tips-area');
	if (!isItemList(emptyArea)) {
		// 初始化下拉
		activity_dropload = dataDropload($('#activity_list_div'),
				$('#activity_list_ul'),
				'/api/webchat/wonder/activity/data', buildActivity,
				buildActivityListUl, 'up');
	}
	var $offline_activity_div,$offline_activity_ul;
	$offline_activity_div=$('#offline_activity_div');
	$offline_activity_ul=$('#offline_activity_ul');
	
	var offlineUl=function($ul, list) {
		var li_item = '';
		$.each(list, function(index, item) {
			var li_a = '/api/webchat/offline/activity/detail/' + item.uuid;
			var li_title = item.name;
			var wxcover = item.wxcover;
			var li_mark = item.desc;
			var li = '<a href="' + li_a + '" class="box">'
					+ '<div class="img"><img src="' + wxcover + '"></div>'
					+ '<div class="info-tag">' + '<h5 class="tt">' + li_title
					+ '</h5>' + '<p>' + li_mark
					+ '</p><span class="icon icon-arr-r"></span></div></a>';
			li_item += li;
		});
		$ul.append(li_item);
	};
	
	// 必须实现方法活动时间
	var offlineList=function(data) {
	    var list = data.items;
	    if (isItemList(list)) {
	    	offline_dropload.listStartTime = list[list.length - 1].dateTime;
	    }
	    return list;
	};
	
	var offline_dropload = dataDropload($offline_activity_div,
			$offline_activity_ul,
			'/api/webchat/offline/activity/list', offlineList,
			offlineUl, 'up');
});

// 必须实现方法活动时间
function buildActivity(data) {
    var list = data.items;
    if (isItemList(list)) {
    	activity_dropload.listStartTime = list[list.length - 1].dateTime;
    }
    return list;
}

