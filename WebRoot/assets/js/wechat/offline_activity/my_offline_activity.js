$(function() {
	var $un_check_div = $('#un_check_div');
	var $un_check_ul = $('#un_check_ul');
	var $checked_div = $('#checked_div');
	var $checked_ul = $('#checked_ul');
	// 必须实现方法活动时间
	var uncheckList = function(data) {
		var list = data.items;
		if (isItemList(list)) {
			uncheck_dropload.listStartTime = list[list.length - 1].dateTime;
		}
		return list;
	};

	var checkedList = function(data) {
		var list = data.items;
		if (isItemList(list)) {
			checked_dropload.listStartTime = list[list.length - 1].dateTime;
		}
		return list;
	};

	var buildUl = function($ul, list) {
		var li_item = '';
		$.each(list, function(index, item) {
			var uuid = item.uuid;
			var name = item.name;
			var cover = item.wxcover;
			var bookNum = item.bookNum;
			var personNum = item.personNum;
			name = dealSpanStr(name, 34);
			var bookTimeShow = item.bookTimeShow;
			var url = '/api/webchat/offline/activity/detail/' + uuid;
			var li = '<a href="' + url + '" class="box">'
					+ '<div class="img"><img src="' + cover + '"></div>'
					+ '<div class="info"><h5 class="tt">' + name + '</h5>'
					+ '<div class="desc">' + '<p>人数：<span class="num">'
					+ bookNum + '</span>/' + personNum + '</p>' + '<p>报名时间：'
					+ bookTimeShow + '</p>' + '</div>' + '</div>'
					+ '<div class="icon icon-arr-r"></div></a>';
			li_item += li;
		});
		$ul.append(li_item);
	};

	 uncheck_dropload = dataDropload($un_check_div, $un_check_ul,
			'/api/webchat/offline/activity/my/book/list?status=0', uncheckList,
			buildUl, 'up');

	 checked_dropload = dataDropload($checked_div, $checked_ul,
			'/api/webchat/offline/activity/my/book/list?status=1', checkedList,
			buildUl, 'up');
});