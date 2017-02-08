var live_dropload;
$(function() {
	addActiveClass('jczb');
	// 实例化直播列表
	live_dropload = dataDropload($('#live_recorded_div'),
			$('#live_recorded_ul'), '/api/webchat/broadcast/has/record',
			buildLive, buildLiveUl, 'up');
});

function buildLive(data) {
	var list = data.itemList;
	if (!!list && list.length > 0) {
		live_dropload.listStartTime = list[list.length - 1].dateTime;
	}
	return list;
}

function buildLiveUl(emlemt, itemList) {
	var li_content = '';
	$.each(itemList, function(index, item) {
		var li_a = '/api/webchat/broadcast/first/page/' + item.uuid;
		var li_name = item.name;
		var li_cover = item.cover;
		var li_book = item.bookNum;
		var li_desc = item.desc;
		var li_endDate = item.endTime;
		li_endDate = li_endDate.substring(0, 10);
		var li = '<a href="' + li_a + '" class="box">'
				+ '<div class="img"><img src="' + li_cover + '"></div>'
				+ '<div class="info">' + '<h5 class="tt">' + li_name + '</h5>'
				+ '<p><span class="icon icon-users"></span>' + li_book
				+ '人<span class="deadline">' + li_endDate
				+ ' 结束</span></p></div>'
				+ '<div class="icon icon-arr-r"></div></a>';
		li_content += li;
	});
	emlemt.append(li_content);
}
