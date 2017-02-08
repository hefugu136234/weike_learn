var player, res_uuid, division, player_timer, play_total_button, play_pre_button, play_next_button, pdf_list_ul, chapters_list_ul, current_page_span, video_tips, video_tips_tt, video_tips_info, video_tips_btn;
var li_active_eq = 0;// 当前pdf li 和章节 li 选中的序号
var firstView = true;// 第一次点击播放增加积分
var click_play_first = true;// 第一次点击播放标志

/**
 * 1.视频的播放、暂定有一个总控制，控制播放暂停按钮 计时器的开启和关闭 2.视频秒数变化有一个总控制，控制pdf及相关变化
 * 3.pdf有一个总控制，控制pdf及相关变化
 *
 * 注：1. 视频时间变化时，pdf显示期间，先关闭计时器 2.pdf控制视频时，同理先关闭计时器，控制视频时间，控制pdf变化
 *
 * 3.计时器的开关交给视频状态变化来完成
 *
 * 传入参数，true是开，false是关
 */
//新js原 three_view.js
$(document).ready(function() {
	// activeNav('nav_index');
	var fileid = $('#three_fileid').val();
	var three_division = $('#three_division').text();
	res_uuid = $('#resource_uuid').val();
	play_total_button = $('#play_total_button');
	play_pre_button = $('#play_pre_button');
	play_next_button = $('#play_next_button');
	pdf_list_ul = $('#pdf_list_ul');
	chapters_list_ul = $('#chapters_list_ul');
	current_page_span = $('#current_page_span');
	video_tips_tt = $('#video_tips_tt');
	video_tips = $('#video_tips');
	video_tips_info = $('#video_tips_info');
	video_tips_btn = $('#video_tips_btn');
	division = JSON.parse(three_division);

	// 视频相关
	var userAgent = navigator.userAgent;
	var Android = /(Android)/i.test(userAgent);
	var IOS = /(iPhone|iPad|iPod|iOS)/i.test(userAgent);
	var video_class = $('.video-container');
	var video_ppt = $('.video-ppt-container');
	video_container = $('#video_container');
	var video_width = video_container.outerWidth();
	var video_height = video_container.outerHeight();
	if (!Android && !IOS){
		video_height = video_width * 3/4;
		video_class.outerHeight(video_height);
		video_ppt.outerHeight(video_height);
	}
	var video_option = {
		'auto_play' : '0',
		'file_id' : fileid,
		'app_id' : '1251442335',
		'width' : video_width,
		'height' : video_height,
		'disable_full_screen' : 1,
		'stretch_full' : 1,
		'WMode' : 'opaque',
		'remember' : 0
	};
	
	var page_reamin_uuid=$('#page_reamin_uuid').val();
	//页面记录时间对象
	var page_obj=creatPageReamin(page_reamin_uuid,5);
	//增加积分对象
	var jifen_obj=creatResourseJifen(res_uuid);
	
	player = new qcVideo.Player('video_container', video_option, {
		/**
		 * 初始化的状态为seeking，ready,
		 * 点击播放时先seeking，再palying，这样就把启动程序启动2遍，player_loop发生变化，后一次能取消，前一次取消不了
		 * 在微信上播放时，可能只有seeking，没有playing； ready:
		 * “播放器已准备就绪”,seeking:”搜索”,suspended:”暂停”, playing:”播放中” ,
		 * playEnd:”播放结束” , stop: “试看结束触发”
		 */
		'playStatus' : function(status) {
			console.log(status);
			if (status == 'ready') {
				// ready，什么都不做
				// click_play_first = false;// 自动播放
			} else if (status == 'seeking') {
				// 搜索时，什么都不做
			} else if (status == 'playing') {
				// if (!click_play_first) {
				// // 如果是自动播放
				// click_play_first = true;
				// return;
				// }
				playCtrlVideo(true);
				// 首次播放，增加积分
				//页面监测
				page_obj.controller(function(){
					return player.isPlaying();
				});
				//第一次点击播放增加积分
				jifen_obj.request();;
			} else if (status == 'suspended' || status == 'playEnd') {
				playCtrlVideo(false);
			}
		}
	});

	// 视频信息弹出
	video_tips_btn.on('click', function() {
		var $that = $(this);

		if ($that.hasClass('tips')) {
			if ($that.hasClass('active')) {
				$that.removeClass('active');
				$('#video_tips').stop().fadeOut();
			} else {
				$that.addClass('active');
				$('#video_tips').stop().fadeIn();
			}
		}
	});


	// 章节选择按钮
	$('#page_select_btn').click(function() {
		var $that = $(this);
		var $select_menu = $('#ppt_select_list');

		if ($select_menu.hasClass('active')) {
			$that.removeClass('active');
			$select_menu.animate({
				bottom : '-96px'
			}, 500);
			$select_menu.removeClass('active');
		} else {
			$that.addClass('active');
			$select_menu.animate({
				bottom : '48px'
			}, 500);
			$select_menu.addClass('active');
		}
	});

	// 总控制按钮
	play_total_button.click(function(e) {
		e.preventDefault();
		$this = $(this);
		if ($this.hasClass('icon-play2')) {
			// 当前为停止状态，1，暂停 2.播放完毕
			if (player.isPlayEnd()) {
				// 播放完毕
				player.play(0);
			} else if (player.isSuspended()) {
				// 暂停
				player.resume();// 恢复播放
			} else {
				// 首次播放
				player.play(0);
			}
		} else if ($this.hasClass('icon-pause')) {
			// 当前为播放状态
			player.pause();
		}
	});

	// 默认选中了第一页
	/**
	 * 向左翻页 减
	 */
	play_pre_button.click(function(e) {
		e.preventDefault();
		if (li_active_eq == 0) {
			return false;
		}
		var shall_eq = li_active_eq - 1;
		pdfCtrl(shall_eq);
	});

	/**
	 * 向右翻页 加
	 */
	play_next_button.click(function(e) {
		e.preventDefault();
		if (li_active_eq == division.length - 1) {
			// 点击无效
			return false;
		}
		var shall_eq = li_active_eq + 1;
		pdfCtrl(shall_eq);
	});

	chapters_list_ul.find('li').each(function(index, item) {
		$(item).click(function() {
			pdfCtrl(index);
		});
	});

	videoTipsCtrl(li_active_eq);


});

function playCtrlVideo(play) {
	if (play) {
		// 开
		// 先控制按钮
		if (play_total_button.hasClass('icon-play2')) {
			play_total_button.removeClass('icon-play2').addClass('icon-pause');
		}
		startTimer();
	} else {
		// 关
		if (play_total_button.hasClass('icon-pause')) {
			play_total_button.removeClass('icon-pause').addClass('icon-play2');
		}
		closeTimer();
	}
}

/**
 * 当计时器变化时，视频总控制
 */
function videoCtrl() {
	var count_index = judyDivisionOftime();
	pdfChangeAffect(count_index);
}

/**
 * 左右翻页时，pdf的总控制
 */
function pdfCtrl(shall_eq) {
	// 首先先暂停视频
	player.pause();
	// pdf变化
	pdfChangeAffect(shall_eq);
	// 恢复播放
	var time = division[li_active_eq].video;
	player.play(time);
}

/**
 * 对pdf变化的影响，一系列变化 shall_eq是将要变化li的序号
 */
function pdfChangeAffect(shall_eq) {
	// 先比对将要变化的序号和当前全局序号比对
	if (li_active_eq == shall_eq) {
		// 不产生变化
		return;
	}
	// 在pdf里面的2个item
	var pdf_ul_org = pdf_list_ul.find('li').eq(li_active_eq);
	var pdf_ul_cur = pdf_list_ul.find('li').eq(shall_eq);
	changeActivie(pdf_ul_org, pdf_ul_cur);
	// 在章节中的2个item
	var chapters_org = chapters_list_ul.find('li').eq(li_active_eq);
	var chapters_cur = chapters_list_ul.find('li').eq(shall_eq);
	changeActivie(chapters_org, chapters_cur);
	// 控制是否是最后一页或者首页
	if (shall_eq == 0) {
		// 第1页 play_pre_button
		play_pre_button.addClass('disabled');
	} else if (shall_eq == division.length - 1) {
		// 最后一页 play_next_button
		play_next_button.addClass('disabled');
	} else {
		if (play_pre_button.hasClass('disabled')) {
			play_pre_button.removeClass('disabled');
		}

		if (play_next_button.hasClass('disabled')) {
			play_next_button.removeClass('disabled');
		}
	}
	// 当前页面的控制
	current_page_span.html(shall_eq + 1);

	// 提示信息
	videoTipsCtrl(shall_eq);

	// 置换当前序号
	li_active_eq = shall_eq;
}

/**
 * 去掉原来的active,加上现在的active
 *
 */
function changeActivie(item_org, item_cur) {
	item_org.removeClass('active');
	item_cur.addClass('active');
}

/**
 * 当计数器秒数变化时，触发，遍历对应关系
 */
function judyDivisionOftime() {
	var video_current_time = player.getCurrentTime();
	var count_index = 0;
	$.each(division, function(index, item) {
		var time = item.video;
		if (video_current_time >= time) {
			count_index = index;
		} else {
			return false;
		}
	});
	return count_index;
}

/**
 * 开始1s循环计时， 一定保证player_timer，只能开启一个
 */
function startTimer() {
	if (!!player_timer) {
		// 如果已经开启，则不再开始
		return;
	}
	player_timer = setInterval(function() {
		// 调用视频控制
		videoCtrl();
	}, 1000);
}

/**
 * 关闭1s循环计时，一定关闭后将计数器滞空
 */
function closeTimer() {
	if (!!player_timer) {
		// 如果已经开启，关闭
		clearInterval(player_timer);
		// 关闭将
		player_timer = undefined;
	}
	// 否则已经关闭
}

// 视频提示信息
function videoTipsCtrl(index) {
	var title = division[index].title;
	var description = division[index].description;

	if (!!title || !!description) {
		video_tips_btn.addClass('tips');
	} else {
		video_tips_btn.removeClass('tips');
		video_tips_btn.removeClass('active');
		video_tips.stop().fadeOut();
	}

	if (!title) {
		title = '';
	}

	if (!description) {
		description = '';
	}

	video_tips_tt.html(title);
	video_tips_info.html(description);
}

