
var activity_dropload;
$(function(){
		activity_dropload = dataDropload($('#activity_list_div'),
				$('#activity_list_ul'),
				'/api/webchat/works/activity/list', buildActivity,
				buildActivityListUl, 'up');
});

// 必须实现方法活动时间
function buildActivity(data) {
    var list = data.items;
    if (isItemList(list)) {
    	activity_dropload.listStartTime = list[list.length - 1].dateTime;
    }
    return list;
}


