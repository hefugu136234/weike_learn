$(function() {
	/**
	 * 分页操作
	 */
	var package_count = $('#all_package_count').val();
	package_count = parseInt(package_count);
	var pageObject = pageObj(package_count, 10, 1);
	pageControllerInit(pageObject, pagecontainer, getRequest);
});
var pagination_ul, package_ul;

function pagecontainer() {
	if (!pagination_ul) {
		pagination_ul = $('#all_package_list');
	}
	return pagination_ul;
}

function getRequest(obj) {
	if (!package_ul) {
		package_ul = $('#package_ul');
	}
	// 请求数据
	$.getJSON('/f/web/course/package/list/data', {
		'size' : obj.batchSize,
		'currentPage' : obj.currentPage
	}, function(data) {
		if (isSuccess(data)) {
			var itemList = data.items;
			obj.total = data.itemTotalSize;
			buildUl(package_ul, itemList);
			pageControllerInit(obj, pagecontainer, getRequest);
		}
	});
}

function buildUl(ul, itemList) {
	ul.empty();
	var li_item = '';
	if (isItemList(itemList)) {
		$.each(itemList, function(index, item) {
			var item_div = buildUlItem(item);
			li_item = li_item + item_div;
		});
	}
	ul.append(li_item);
}

function buildUlItem(item) {
	var url = '/f/web/course/package/detail/' + item.uuid;
	var cover = item.cover;
	var name = item.name;
	name = cutStrForNum(name, 40);
	if (!cover) {
		cover = '/assets/img/web/placeholder.jpg';
	}
	var li = '<li class="col-xs-12 col-sm-6 col-md-3 item">'
			+ '<div class="thumbnail resource-item">' + '<a href="' + url
			+ '" class="img">' + '<img src="' + cover + '"alt="" />';
	var learnSchedule = item.learnSchedule;
	var studyStatus =item.studyStatus;
	if (studyStatus > 0) {
		var schedule_div = '';
		if (learnSchedule < 100) {
			schedule_div = '<div class="package-process">'
					+ '<div class="process-circle">' + '<p class="process">'
					+ '<span class="num">' + learnSchedule + '</span>%'
					+ '</p>' + '<p>进度</p>' + '</div>' + '</div>';
		} else {
			schedule_div = '<div class="package-done"><div class="info"><div class="icon icon-checked"></div><p>已完成</p></div></div>'
		}
		li += schedule_div;
	}
	li += '</a>';
	li += '<div class="caption desc">' + '<h3 class="tt">' + '<a href="' + url
			+ '">' + name + '</a>' + '</h3>'
			+ '<div class="package-list-status clearfix">'
			+ '<span class="box"><i class="icon icon-edit"></i>'
			+ item.learnCount + '</span>'
			+ '<span class="box text-center"><i class="icon icon-like"></i>'
			+ item.praiseCount + '</span>'
			+ '<span class="box text-right"><i class="icon icon-message"></i>'
			+ item.commentCount + '</span>' + '</div>' + '</div>';
	li += '</div></li>';
	return li;

}