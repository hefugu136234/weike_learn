$(function() {
	$('.package-list-control').each(function() {
		var $that = $(this);
		var $package_tab = $that.find('.package-list-tab');
		var $package_list = $that.find('.package-show-list');

		$that.on('click', '.package-list-tab', function() {
			if ($package_tab.hasClass('open')) {
				$package_list.removeClass('open');
				$package_tab.removeClass('open');
			} else {
				$package_tab.addClass('open');
				$package_list.addClass('open');
			}
		});
	});
	
	var course_studyStatus=parseInt($('#course_studyStatus').val());


	
	var itemExamineUrlFun = function(uuid, course_uuid) {
		location.href = '/api/webchat/course/'+course_uuid+'/chapter/examin/'+uuid;
	};

	var itemExamineFun = function(logined, disableClick, course_uuid, uuid) {
		if (logined == 0) {
			var course_url = '/api/webchat/course/package/detail/' + course_uuid;
			loginAlert(course_url);
		} else {
			if(course_studyStatus==0){
				AlertClassTip("请先点击学习本课程");
				return ;
			}
			if (disableClick == 0) {
				AlertClassTip("请先通过前一章节的考试");
			} else {
				itemExamineUrlFun(uuid, course_uuid);
			}
		}
	};

	var itemResourceFun = function(disableClick, url) {
		if (disableClick == 0) {
			// 不能点击
			AlertClassTip("请先通过前一章节的考试");
		} else if (disableClick == 1) {
			location.href = url;
		}
	};

	var itemBoxClickFun = function(s_item, logined, disableClick, course_uuid) {
		var $s_item = $(s_item);
		var type = $s_item.data('type');
		var uuid = $s_item.data('uuid');
		$s_item.click(function(e) {
			e.preventDefault();
			var url = '/api/webchat/course/learn/' + uuid;
			if (type == 'resource') {
				itemResourceFun(disableClick, url);
			} else if (type == 'examine') {
				itemExamineFun(logined, disableClick, course_uuid,uuid);
			}
		});
	};

	var itemBoxFun = function(item, course_uuid) {
		var $item = $(item);
		var logined = $item.data("logined");
		var disableClick = $item.data("disableclick");
		var boxes = $item.find('div.box.done');
		if (isItemList(boxes)) {
			boxes.each(function(index, s_item) {
				itemBoxClickFun(s_item, logined, disableClick, course_uuid);
			});
		}
	};

	var list_div = $('div.list-with-img');
	if (isItemList(list_div)) {
		var course_uuid = $('#course_uuid').val();
		//var course_url = '/api/webchat/course/package/detail/' + course_uuid;
		list_div.each(function(index, item) {
			itemBoxFun(item, course_uuid);
		});
	}

	// 点击学习
	$('#course_study').click(function(e) {
		e.preventDefault();
		var $this = $(this);
		var uuid = $this.data('uuid');
		$.post('/api/webchat/course/study/action', {
			uuid : uuid
		}, function(data) {
			if (isSuccess(data)) {
				$this.hide();
				var score = data.message;
				$('#user-info').find('span.num').html(score);
				$('#user-info').removeClass('hide');
			} else {
				if (data.message == 'not login') {
					var url = '/api/webchat/course/package/detail/' + uuid;
					loginAlert(url);
				} else {
					AlertClassTip(data.message);
				}
			}
		});
	});

});
