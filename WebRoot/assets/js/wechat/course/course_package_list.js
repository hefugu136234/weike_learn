;
$(function() {
	course_package_dropload = dataDropload($('#course_package_div'),
			$('#course_package_ul'), '/api/webchat/course/package/list/data',
			buildDataTime, buildListUl, 'up');
});
var course_package_dropload;

function buildDataTime(data) {
	var list = data.items;
	if (!!list && list.length > 0) {
		course_package_dropload.listStartTime = list[list.length - 1].dateTime;
	}
	return list;
}

function buildListUl(element, itemList) {
	console.log(itemList);
	var li_content = '';
	console.log(itemList);
	$.each(itemList, function(index, item) {
		var li_a = '/api/webchat/course/package/detail/' + item.uuid;
		var li_name = item.name;
		var li_cover = item.cover;
		var learnCount = item.learnCount;
		var praiseCount = item.praiseCount;
		var commentCount = item.commentCount;
		var learnSchedule = item.learnSchedule;
		var studyStatus =item.studyStatus;
		var li = '<div class="item"><a href="' + li_a + '">'
				+ '<div class="img">' + '<img src="' + li_cover
				+ '" alt="课程包封面">';
		// 判断是否有进度
		if (studyStatus > 0) {
			//学习课程之后才展示
			var process_li = '';
			if (learnSchedule < 100) {
				// 进度
				process_li = '<div class="package-process">'
								+ '<div class="process-circle"><p class="process">'
								+'<span class="num">'+learnSchedule+'</span>%</p>'
								+'<p>进度</p></div></div>';
			} else {
				// 完成
				process_li='<div class="package-done">'
							+'<div class="info"><div class="icon icon-checked"></div><p>已完成</p>'
							+'</div>';
			}
			li+=process_li;
		}
		//img div end
		li=li+'</div>';
		li+='<div class="desc">'
				+'<h5 class="tt">'+li_name+'</h5>'
				+'<div class="package-list-status">'
					+'<span class="box"><i class="icon icon-edit"></i>'+learnCount+'</span>'
					+'<span class="box text-center"><i class="icon icon-like"></i>'+praiseCount+'</span>'
					+'<span class="box text-right"><i class="icon icon-message"></i>'+commentCount+'</span>'
				+'</div>'
			+'</div>'
			+'</a></div>';
		li_content += li;
	});
	element.append(li_content);
}
