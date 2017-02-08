$(function() {
	var uuid = $('#body_modal_uuid').val();
	var $offline_apply_div = $('#offline_apply_div');
	var $offline_apply_ul = $('#offline_apply_ul');
	// 必须实现方法活动时间
	var offlineApplyList = function(data) {
		var list = data.items;
		if (isItemList(list)) {
			offline_dropload.listStartTime = list[list.length - 1].dateTime;
		}
		return list;
	};

	var offlineUl = function($ul, list) {
		var li_item = '';
		$.each(list, function(index, item) {
			var name = item.name;
			var bookTime = item.bookTime;
			var checkStatus = item.checkStatus;
			var checkStatusVal = '';
			if (checkStatus == 0) {
				checkStatusVal = '未审核';
			} else if (checkStatus == 1) {
				checkStatusVal = '已通过';
			} else if (checkStatus == 2) {
				checkStatusVal = '未通过';
			}
			var li = '<li>' + '<div class="box name">' + name + '</div>'
					+ '<div class="box date">' + bookTime + '</div>'
					+ '<div class="box status">' + checkStatusVal + '</div>'
					+ '</li>';
			li_item += li;
		});
		$ul.append(li_item);
	};

	var offline_dropload = dataDropload($offline_apply_div,
			$offline_apply_ul, '/api/webchat/offline/book/list?uuid=' + uuid,
			offlineApplyList, offlineUl, '');
})