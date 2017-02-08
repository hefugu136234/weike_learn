var verify_phone = /^(0|86|17951)?(13[0-9]|15[012356789]|17[0-9]|18[0-9]|14[57])[0-9]{8}$/; // 验证手机号码
var verify_http = new RegExp("^(http).*");
$(function() {
	// 限制字符串字数
	$('.limit-str-text').each(function() {
		var $that = $(this);
		var text = $that.html();
		var min_str = $that.data('minstr');
		var new_str = cutStrForNum(text, min_str);
		$that.html(new_str);
	});

	// // 弹框关闭
	$('.feed-back-modal').find('.btn.cancel,.btn.login, .btn.sub, .cover')
			.click(
					function() {
						$(this).parents('.feed-back-modal').fadeOut()
								.removeClass('active');
					});

	// 单个滚动banner初始化
	var banners = $('.default-swiper');
	if (isItemList(banners)) {
		banners.each(function(index, item) {
			var $item = $(item);
			var swiper_pagination = $item.find('.swiper-pagination');
			new Swiper($item, {
				pagination : swiper_pagination,
				paginationClickable : true
			});
		});
	}

	// 组滚动banner初始化
	var default_group_swiper = $('.default-group-swiper');
	if (isItemList(default_group_swiper)) {
		default_group_swiper.each(function(index, item) {
			var $item = $(item);
			var li_length = $item.find('li').length;
			if (li_length < 3) {
				return true;// continue
			}
			new Swiper($item, {
				slidesPerView : 'auto',
				paginationClickable : true,
				loop : true,
				autoplay : 4000,
				autoplayDisableOnInteraction : false
			});
		});
	}

	// 标签切换
	$('.tab-control').each(function() {
		var $tab_control = $(this);
		$tab_control.find('.tab-control-tag').on('click', '.btn', function() {
			var $that = $(this);
			var index = $that.index();
			var $tags = $tab_control.find('.tab-control-tag');
			var $content = $tab_control.find('.tab-content');
			$that.siblings('.btn').removeClass('active');
			$that.addClass('active');
			$content.eq(index).siblings('.tab-content').removeClass('active');
			$content.eq(index).addClass('active');
		});
	});

	// 注册登录弹出框
	// $('#reg_alert').find('a.login').click(function(e) {
	// e.preventDefault();
	// var url = $('#reg_alert').data('url');
	// resJudyType(1, url);
	// });
	//
	// $('#reg_alert').find('a.reg').click(function(e) {
	// e.preventDefault();
	// var url = $('#reg_alert').data('url');
	// resJudyType(2, url);
	// });

	// 收藏的公共方法
	$('#my_collection').click(function(event) {
		event.preventDefault();
		var $this = $(this);
		var star = $this.find('.icon');
		var text = $this.find('.info');
		var num = $this.find('.num');
		var uuid = $this.data('uuid');
		$.post('/api/webchat/collection/status', {
			'uuid' : uuid
		}, function(data) {
			if (isSuccess(data)) {
				if (data.reStatus) {
					star.attr('class', 'icon icon-star');
					text.html('已收藏');
				} else {
					star.attr('class', 'icon icon-star-line');
					text.html('收藏');
				}
				num.html(data.reCount);
			} else {
				if (data.message == 'not login') {
					var url = '/api/webchat/resource/first/view/' + uuid;
					// $('#reg_alert').data('url', url);
					// $('#reg_alert').fadeIn().addClass('active');
					loginAlert(url);
				} else {
					AlertClassTip(data.message);
				}
			}
		});
	});

	// 点赞的公共方法
	$('#my_zan').click(function(event) {
		event.preventDefault();
		var $this = $(this);
		var text = $this.find('.info');
		var num = $this.find('.num');
		var uuid = $this.data('uuid');
		$.post('/api/webchat/praise/status', {
			'uuid' : uuid
		}, function(data) {
			if (isSuccess(data)) {
				if (data.reStatus) {
					text.html('已赞');
					var score = data.integral;
					if (score > 0) {
						scoreTipObj().show(score);
					}
				} else {
					text.html('赞');
				}
				num.html(data.reCount);
			} else {
				if (data.message == 'not login') {
					var url = '/api/webchat/resource/first/view/' + uuid;
					// $('#reg_alert').data('url', url);
					// $('#reg_alert').fadeIn().addClass('active');
					loginAlert(url);
				} else {
					AlertClassTip(data.message);
				}
			}
		});
	});

	// 微信分享提示公共方法
	$('.share-from-wechat-btn').click(function(e) {
		e.preventDefault();
		shareWXTips();
	});

	// 分享的公共方法
	$('#my_share').click(function(e) {
		e.preventDefault();
		$('#share_from_wechat').fadeIn();
	});

	// 分享方式隐藏
	$('#share_from_wechat').click(function() {
		$(this).fadeOut();
	});

	// 微信图片预览
	var $wecaht_preview_imgs = $('img.wechat-preview-img');
	var preview_imgs_array = new Array();
	for (var i = 0; i < $wecaht_preview_imgs.length; i++) {
		preview_imgs_array.push($wecaht_preview_imgs.eq(i).attr('src'));
	}
	$('img.wechat-preview-img').each(function() {
		$(this).click(function(event) {
			event.preventDefault();
			WeixinJSBridge.invoke('imagePreview', {
				'current' : $(this).attr('src'),
				'urls' : preview_imgs_array
			});
		});
	});

});

// 创建公用微信分享提示
var shareWXTips = function(tips) {
	var wechatTips = new Object();
	wechatTips.tips = tips;
	wechatTips.wxTipsUi = function() {
		var html = '<div class="share-wechat-tips">' + '<p>请从微信中进行分享！</p>';
		if (typeof this.tips !== 'undefined') {
			html += '<div class="tips">' + this.tips + '</div>';
		}
		html += '</div>';
		return html;
	};
	wechatTips.createDom = function() {
		var dom = this.wxTipsUi();
		this.wxTipsDom = $(dom);
	};
	wechatTips.show = function() {
		this.createDom();
		this.bindClick();
		$('body').append(this.wxTipsDom);
		this.wxTipsDom.fadeIn();
	};
	wechatTips.destroy = function() {
		this.wxTipsDom.fadeOut(function() {
			this.wxTipsDom.remove();
		}.bind(this));
	};
	wechatTips.bindClick = function() {
		this.wxTipsDom.click(function() {
			this.destroy();
		}.bind(this));
	};
	wechatTips.show();
};

// var message='您需要<strong class="blue">注册/登录</strong>“知了云盒”账号才能观看该视频哦';
// 登录弹出框
var loginAlert = function(url) {
	var message = '您需要<strong class="blue">注册/登录</strong>“知了云盒”账号才能继续操作';
	loginAlertCommon(url,message);
};

var loginAlertCommon = function(url,message) {
	AlertClassTip(message, {
		option : [ '立即注册', '登录' ]
	}, function(flag) {
		if (flag == 1) {
			resJudyType(2, url);
		} else if (flag == 2) {
			resJudyType(1, url);
		}
	});
};

var realnameAlert = function(url) {
	var message = '您需要<strong class="blue">实名认证</strong>后才能观看该视频哦';
	AlertClassTip(message, {
		option : [ '立即认证', '取消' ]
	}, function(flag) {
		if (flag == 1) {
			resJudyType(3, url);
		}
	});
};

var nowatchAlert = function(url) {
	var message = 'TODO';
	AlertClassTip(message, {
		option : [ '申请密码', '取消' ]
	}, function(flag) {
		// if(flag==1){
		// resJudyType(3, url);
		// }
	});
};

// // 触发添加积分效果
// function addScoreTips(score) {
// var $score_ui = $('<div class="score-alert-tips active">'
// + '<h5 class="tt">积 分</h5>' + '<div class="num">+' + score
// + '</div>' + '</div>');
//
// // $score_ui.addClass('active');
// $('body').append($score_ui);
// setTimeout(function() {
// $score_ui.removeClass('active');
// $score_ui.remove();
// }, 3500);
// }

// 字符串限制字数
function cutStrForNum(str, len) {
	var str_length = 0;
	var str_cut = new String();
	var result = str;

	for (var i = 0; i < str.length; i++) {
		a = str.charAt(i);
		str_length++;
		if (escape(a).length > 4) {
			// 中文字符的长度经编码之后大于4
			str_length++;
		}
		str_cut = str_cut.concat(a);

		if (str_length >= len) {
			str_cut = str_cut.concat('...');
			result = str_cut.toString();
			break;
		}
	}

	return result;
}

// 判断数据返回状态
function isSuccess(data) {
	if (data.status == 'success') {
		return true
	}
	return false;
}

// 判断list数据
function isItemList(list) {
	if (!!list && list.length > 0) {
		return true
	}
	return false;
}

// 图片过滤
function coverFilter(cover) {
	var img_src = cover;
	if (!verify_http.test(cover)) {
		img_src = "http://cloud.lankr.cn/api/image/" + cover
				+ "?m/2/h/500/f/jpg";
	}
	return img_src;
}

// 5秒一次记录页面停留时间
function pageRemainInit(callUUidBack, duration) {
	var uuid = callUUidBack();
	if (!!uuid) {
		duration = duration * 1000;
		self.setInterval(function() {
			// 向服务器推送数据
			$.post('/api/webchat/record/page/remain', {
				'uuid' : uuid
			}, function(data) {
				// console.log(data);
			});
		}, duration);
	}
}

// 由秒得到天时分秒数据
var formatTime = function(longTime) {
	// 转化为 日+小时+分+秒
	var time = parseFloat(longTime);
	if (time != null && time != "") {
		if (time < 60) {
			var s = time;
			time = s + '秒';
		} else if (time > 60 && time < 3600) {
			var m = parseInt(time / 60);
			var s = parseInt(time % 60);
			time = m + "分钟" + s + "秒";
		} else if (time >= 3600 && time < 86400) {
			var h = parseInt(time / 3600);
			s
			var m = parseInt(time % 3600 / 60);
			var s = parseInt(time % 3600 % 60 % 60);
			time = h + "小时" + m + "分钟" + s + "秒";
		} else if (time >= 86400) {
			var d = parseInt(time / 86400);
			var h = parseInt(time % 86400 / 3600);
			var m = parseInt(time % 86400 % 3600 / 60)
			var s = parseInt(time % 86400 % 3600 % 60 % 60);
			time = d + '天' + h + "小时" + m + "分钟" + s + "秒";
		}
	}
	return time;
};

// 底部添加点击样式
function addActiveClass(id) {
	$('#' + id).addClass('active');
}

// 渲染资源列表
/**
 * 2016-06-24 ulCall 填充数据的dom ul的实体id list 资源数据 clear_data 是否要清空数据
 */
function drawResourceList($ul, list, clear_data) {
	if (!isItemList(list)) {
		return;
	}
	if (clear_data) {
		$ul.empty();
	}
	var ul_content = '';
	$.each(list, function(index, item) {
		var li = drawResourceItem(item, '');
		ul_content += li;
	});
	$ul.append(ul_content);
}

/**
 * 字符串是否含有html标签的检测
 *
 * @param htmlStr
 */
function checkHtml(htmlStr) {
	var reg = /<[^>]+>/g;
	return reg.test(htmlStr);
}
// js处理span的标签
function dealSpanStr(str, num) {
	if (checkHtml(str)) {
		// 需要截取的字符串
		var dealStr = filtrationStr(str);
		// 可能有多个关键字
		var keywords = new Array()
		var i = 0;
		while (i < str.length) {
			var firstStart = str.indexOf("<i class='result-mark'>", i);
			var firstEnd = str.indexOf("</i>", i);
			if (firstStart < 0 || firstEnd < 0) {
				break;
			}
			i = firstEnd + 1;
			var keyword = str.substring(firstStart, firstEnd);
			// 得到处理的关键字
			keyword = filtrationStr(keyword);
			if (keyword == '') {
				break;
			}
			if ($.inArray(keyword, keywords) < 0) {
				keywords.push(keyword);
			}
		}
		// for (var i = 0; i < str.length; i++) {
		// var firstStart = str.indexOf("<i class='result-mark'>", i);
		// var firstEnd = str.indexOf("</i>", i);
		// if(firstStart<0||firstEnd<0){
		// break;
		// }
		// if (firstEnd < firstStart) {
		// //去掉前一次的结尾
		// continue;
		// }
		// var keyword = str.substring(firstStart, firstEnd);
		// // 得到处理的关键字
		// keyword = filtrationStr(keyword);
		// if ($.inArray(keyword, keywords) < 0) {
		// keywords.push(keyword);
		// }
		// }
		// 截过的字符串
		dealStr = cutStrForNum(dealStr, num);
		if (isItemList(keywords)) {
			$.each(keywords, function(index, item) {
				var replaceStr = "<i class='result-mark'>" + item + "</i>";
				dealStr = dealStr.replaceAll(item, replaceStr);
			});
		}
		return dealStr;
	}
	return cutStrForNum(str, num);
}

String.prototype.replaceAll = function(s1, s2) {
	return this.replace(new RegExp(s1, "gm"), s2);
}

// 过滤字符串
function filtrationStr(str) {
	var dealStr = str.replaceAll("<i class='result-mark'>", "");
	dealStr = dealStr.replaceAll("</i>", "");
	return dealStr;
}

/**
 * 主要资源 video pdf 三分屏 新闻 绘制资源列表的每个item
 *
 * @param item
 */
function drawResourceItem(item, url) {
	var type = item.type;
	var uuid = item.uuid;
	var name = item.name;
	var cover = item.cover;
	var speakerName = item.speakerName;
	var hospitalName = item.hospitalName;
	var catgoryName = item.catgoryName;
	var viewCount = item.viewCount;
	var bloody = item.bloody;
	name = dealSpanStr(name, 34);
	if (url == '') {
		// 资源
		url = '/api/webchat/resource/first/view/' + uuid;
	} else {
		// 作品
		url = '/api/webchat/wx/works/detail/' + uuid;
	}
	var fileClass = '', filename = '';
	if (type == 'VIDEO') {
		fileClass = 'video-type';
		filename = '视频';
	} else if (type == 'PDF') {
		fileClass = 'pdf-type';
		filename = 'PDF';
	} else if (type == 'THREESCREEN') {
		fileClass = 'ppt-type';
		filename = '课件';
	} else if (type == 'NEWS') {
		// return drawResNewsItem(item);
		fileClass = 'file-type';
		filename = '资讯';
	} else if (type == 'VR') {
		fileClass += ' vr-type';
		filename = 'VR';
	}
	var item_li = '<a href="' + url + '" class="box">' + '<div class="img">'
			+ '<img src="' + cover + '">' + '<div class="img-tag tr '
			+ fileClass + '">' + filename + '</div>';
	if (bloody) {
		item_li += '<div class="bloody-icon"><img alt="" src="/assets/img/app/icon_bloody.png"></div>';
	}
	item_li += '<div class="img-tag br view-num">'
			+ '<span class="icon icon-eye"></span>' + viewCount
			+ '</div></div>' + '<div class="info">' + '<h5 class="tt">' + name
			+ '</h5>' + '<div class="desc">' + '<p><span>' + catgoryName
			+ '</span></p>' + '<p><span>' + speakerName + '</span> | <span>'
			+ hospitalName + '</span></p></div></div>'
			+ '<div class="icon icon-arr-r"></div></a>';
	return item_li;
}

/**
 * 构建资源新闻类型
 *
 * @param item
 * @returns {String}
 */
function drawResNewsItem(item) {
	var catgoryName = item.catgoryName;
	var uuid = item.uuid;
	var name = item.name;
	var dateTime = item.dateTime;
	var shareCount = item.shareCount;
	var cover = item.cover;
	var desc = item.desc;
	var url = '/api/webchat/resource/first/view/' + uuid;
	var li = '';
	if (catgoryName == '分享有礼') {
		// 分享有礼
		name = dealSpanStr(name, 20);
		desc = dealSpanStr(desc, 20);
		li = '<a href="' + url + '" class="box with-share">'
				+ '<div class="img"><img src="' + cover + '"></div>'
				+ '<div class="info">' + '<h5 class="tt">' + name + '</h5>'
				+ '<div class="desc">' + desc + '</div></div>'
				+ '<div class="share-item">'
				+ '<div class="icon icon-share"></div>' + '<div>分享 '
				+ shareCount + '</div></div>'
				+ '<div class="icon icon-arr-r"></div></a>';
	} else {
		// 一般新闻
		name = dealSpanStr(name, 20);
		desc = dealSpanStr(desc, 25);
		li = '<a href="' + url + '" class="box">' + '<div class="img">'
				+ '<img src="' + cover + '">'
				+ '<div class="img-tag tr file-type">资讯</div></div>'
				+ '<div class="info">' + '<h5 class="tt">' + name + '</h5>'
				+ '<div class="desc">' + '<p>' + desc + '</p>' + '<p>'
				+ dateTime + '</p></div></div>'
				+ '<div class="icon icon-arr-r"></div></a>';
	}
	return li;
}

/**
 * 获取当前时间(按固定格式)
 *
 * @returns {String}
 */
function getNowFormatDate() {
	// 是当前时间比现实时间多1s,便于取到最新数据
	var date = new Date();
	var seperator1 = "-";
	var seperator2 = ":";
	var month = date.getMonth() + 1;
	month = repairZero(month);
	var strDate = date.getDate();
	strDate = repairZero(strDate);
	var hours = date.getHours();
	hours = repairZero(hours);
	var minutes = date.getMinutes();
	minutes = repairZero(minutes);
	var seconds = date.getSeconds() + 1;
	seconds = repairZero(seconds);
	var currentdate = date.getFullYear() + seperator1 + month + seperator1
			+ strDate + " " + hours + seperator2 + minutes + seperator2
			+ seconds;
	return currentdate;
}

/**
 * 补0
 *
 * @param num
 * @returns
 */
function repairZero(num) {
	if (num < 10) {
		return num = "0" + num;
	}
	return num;
}

/**
 * buildListCallBack 生成对应的list buildUlCallBack 生成ul的callback； 每个调用都要实现的方法
 *
 * 在外部调用刷新的方法 dropload.opts.loadUpFn(dropload); 调用加载更多的方法
 * dropload.opts.loadDownFn(dropload); lock=='up' 向上锁
 */
function dataDropload($div, $ul, request_url, buildListCallBack,
		buildUlCallBack, lock) {
	var time = getNowFormatDate();
	var dropload = $div
			.dropload({
				scrollArea : window,
				domUp : {
					domClass : 'dropload-up',
					domRefresh : '<div class="dropload-refresh">↓下拉刷新</div>',
					domUpdate : '<div class="dropload-update">↑释放更新</div>',
					domLoad : '<div class="dropload-load"><span class="loading"></span>加载中...</div>'
				},
				domDown : {
					domClass : 'dropload-down',
					domRefresh : '<div class="dropload-refresh">↑上拉加载更多</div>',
					domLoad : '<div class="dropload-load"><span class="loading"></span>加载中...</div>',
					domNoData : '<div class="dropload-noData">暂无更多数据</div>'
				},
				loadUpFn : function(me) {
					// 向上刷新
					var startTime = getNowFormatDate();
					var dateTimeSmpt = new Date().getTime();
					$.getJSON(request_url, {
						'startTime' : startTime,
						'size' : 20,
						'action' : 'refresh',
						'dateTimeSmpt' : dateTimeSmpt
					}, function(data) {
						if (isSuccess(data)) {
							// 回调1
							var list = buildListCallBack(data);
							// var isLoadDown = windowHeightDrop();
							$ul.empty();
							if (isItemList(list)) {
								me.isFirst = false;
								// 组合li 回调2
								buildUlCallBack($ul, list);
							}
							// 每次数据加载完，必须重置
							me.resetload();
							// 解锁
							me.unlock();
							me.noData(false);
						} else {
							// 每次数据加载完，必须重置,无更多数据
							me.resetload();
						}
					});
				},
				loadDownFn : function(me) {
					// 往下滑动加载更多
					if (!me.listStartTime) {
						// 多数不用
						me.listStartTime = getNowFormatDate();
					}
					if (me.isFirst == undefined) {
						me.isFirst = true;
					}
					var dateTimeSmpt = new Date().getTime();
					$.getJSON(request_url, {
						'startTime' : me.listStartTime,
						'size' : 20,
						'action' : 'downMore',
						'isFirst' : me.isFirst,
						'dateTimeSmpt' : dateTimeSmpt
					}, function(data) {
						if (isSuccess(data)) {
							var list = buildListCallBack(data);
							if (isItemList(list)) {
								me.isFirst = false;
								// 组合li 回调2
								buildUlCallBack($ul, list);
							} else {
								if (!me.isFirst) {
									// 当不是首次加载到数据，隐藏无数据
									// me.$domDown.addClass('hide');
									me.$element.find('div.dropload-down')
											.addClass('hide');
								}
								// 锁定
								me.lock();
								// 无数据
								me.noData();
							}
							// 每次数据加载完，必须重置
							me.resetload();
						} else {
							if (!me.isFirst) {
								// 当不是首次加载到数据，隐藏无数据
								me.$domDown.addClass('hide');
							}
							// 锁定
							me.lock();
							// 无数据
							me.noData();
							me.resetload();
						}
					});
				},
				threshold : 40
			});
//	if (lock == 'up' || lock == 'down') {
//		dropload.lock(lock);// 锁住上方
//	}
	dropload.lock('up');
	if (lock == 'down') {
		dropload.lock(lock);// 锁住上方
	}
	return dropload;
}

function windowHeightDrop() {
	var documenthei = $(document).height();
	var windowhei = $(window).height();
	return documenthei > windowhei;
}

/**
 * 绘制资源的下拉公共方法
 *
 * @param $ul
 * @param list
 */
function commonDropUl($ul, list) {
	drawResourceList($ul, list, false);
}

/**
 * 活动列表的数据绘制
 *
 * @param $ul
 * @param list
 */
function buildActivityListUl($ul, list) {
	var li_item = '';
	$.each(list, function(index, item) {
		var li_a = '/api/webchat/activity/total/page/' + item.uuid;
		var li_title = item.name;
		var li_kv = item.kv;
		var li_mark = item.mark;
		var li = '<a href="' + li_a + '" class="box">'
				+ '<div class="img"><img src="' + li_kv + '"></div>'
				+ '<div class="info-tag">' + '<h5 class="tt">' + li_title
				+ '</h5>' + '<p>' + li_mark
				+ '</p><span class="icon icon-arr-r"></span></div></a>';
		li_item += li;
	});
	$ul.append(li_item);
}

// 登录弹出框处理的数据
function resJudyType(type, url) {
	// type=1 登录 type=2 注册 type=3 实名
	$.getJSON('/api/webchat/is/login/info', {
		'timestamp' : new Date().getTime()
	}, function(data) {
		if (isSuccess(data)) {
			if (data.message == 'HASLOGIN_HASREALNAME') {
				location.href = url;
			} else {
				if (type == 1) {
					recordPriorUrl(url);
					location.href = '/api/webchat/page/login';
				} else if (type == 2) {
					recordPriorUrl(url);
					location.href = '/api/webchat/register';
				} else if (type == 3) {
					recordPriorUrl(url);
					location.href = '/api/webchat/activity/real/name/page';
				}
			}
		}
	});
}

// 登录之前记忆之前的url
function recordPriorUrl(url) {
	$.post('/api/webchat/record/prior/url', {
		url : url
	}, function(data) {

	});
}

var customFormat = function(date, formatString) {
	var YYYY, YY, MMMM, MMM, MM, M, DDDD, DDD, DD, D, hhhh, hhh, hh, h, mm, m, ss, s, ampm, AMPM, dMod, th;
	var dateObject = date;
	YY = ((YYYY = dateObject.getFullYear()) + "").slice(-2);
	MM = (M = dateObject.getMonth() + 1) < 10 ? ('0' + M) : M;
	MMM = (MMMM = [ "January", "February", "March", "April", "May", "June",
			"July", "August", "September", "October", "November", "December" ][M - 1])
			.substring(0, 3);
	DD = (D = dateObject.getDate()) < 10 ? ('0' + D) : D;
	DDD = (DDDD = [ "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday",
			"Friday", "Saturday" ][dateObject.getDay()]).substring(0, 3);
	th = (D >= 10 && D <= 20) ? 'th' : ((dMod = D % 10) == 1) ? 'st'
			: (dMod == 2) ? 'nd' : (dMod == 3) ? 'rd' : 'th';
	formatString = formatString.replace("#YYYY#", YYYY).replace("#YY#", YY)
			.replace("#MMMM#", MMMM).replace("#MMM#", MMM).replace("#MM#", MM)
			.replace("#M#", M).replace("#DDDD#", DDDD).replace("#DDD#", DDD)
			.replace("#DD#", DD).replace("#D#", D).replace("#th#", th);

	h = (hhh = dateObject.getHours());
	if (h == 0)
		h = 24;
	if (h > 12)
		h -= 12;
	hh = h < 10 ? ('0' + h) : h;
	hhhh = hhh < 10 ? ('0' + hhh) : hhh;
	AMPM = (ampm = hhh < 12 ? 'am' : 'pm').toUpperCase();
	mm = (m = dateObject.getMinutes()) < 10 ? ('0' + m) : m;
	ss = (s = dateObject.getSeconds()) < 10 ? ('0' + s) : s;
	return formatString.replace("#hhhh#", hhhh).replace("#hhh#", hhh).replace(
			"#hh#", hh).replace("#h#", h).replace("#mm#", mm).replace("#m#", m)
			.replace("#ss#", ss).replace("#s#", s).replace("#ampm#", ampm)
			.replace("#AMPM#", AMPM);
}

/**
 * 2016-08-04 为了迎合课程包数据统计
 */
var creatPageReamin = function(uuid, duration) {
	var obj = new Object();
	obj.uuid = uuid;
	obj.first = true;
	obj.duration = duration * 1000;
	obj.action = function(callback) {
		setTimeout(function() {
			this.request(callback);
		}.bind(this), this.duration);
	};
	obj.request = function(callback) {
		var flag = callback();
		if (flag) {
			// 向服务器推送数据
			$.post('/api/webchat/record/page/remain', {
				'uuid' : this.uuid
			}, function(data) {
				if (isSuccess(data)) {
					this.action(callback);
				}
			}.bind(this));
		} else {
			this.action(callback);
		}
	};
	obj.controller = function(callback) {
		if (this.first) {
			console.log('执行');
			this.first = false;
			this.request(callback);
		}
	};
	return obj;
};

/**
 * 2016-08-04 资源增加积分数据
 */
var creatResourseJifen = function(uuid) {
	var obj = new Object();
	obj.uuid = uuid;
	obj.first = true;
	obj.request = function() {
		if (this.first) {
			console.log('加积分');
			$.post('/api/webchat/view/res/add/jifen', {
				'uuid' : this.uuid
			}, function(data) {
				// console.log(data);
				if (isSuccess(data)) {
					this.first = false;
				}
			}.bind(this));
		}
	};
	return obj;
};

// 增加积分滑动显示效果
var scoreTipObj = function(score) {
	var obj = new Object();
	obj.score = parseInt(score);
	obj.setScore = function(s) {
		this.score = parseInt(s);
	};
	obj.getScore = function() {
		if (score > 0) {
			return '+' + this.score;
		}
		return this.score;
	};
	obj.body = function() {
		return '<div class="score-alert-tips active"><h5 class="tt">积 分</h5><div class="num">'
				+ this.getScore() + '</div></div>';
	};
	obj.bodyCreatDom = function() {
		var dom = this.body();
		this.bodyDom = $(dom);
	};
	obj.disappear = function() {
		setTimeout(function() {
			this.bodyDom.removeClass('active');
			this.bodyDom.remove();
		}.bind(this), 3500);
	};
	obj.show = function(s) {
		if (s != undefined) {
			this.setScore(s);
		}
		this.bodyCreatDom();
		$(document.body).append(this.bodyDom);
		this.disappear();
	};
	return obj;
};

/**
 * 作用：使用多种弹出框 comfirm=确定(右) cancel(左) 参数： message(必须) 显示的消息 opt(不是必须)
 * 对象{title:'title',option:['comfirm','cancel']} opt 属性 title option(数组)
 * callback(不是必须) 回调 callback(flag) 回调的参数 flag=0点击其他关闭 flag=1 点击comfirm flag=2
 * 点击cancel 使用示例：opt的title,option(都是可选传参) 1.alert =AlertClassTip('message');
 * 2.alert+回调 AlertClassTip('message',function(flag){}); 3.alert+title
 * AlertClassTip('message',{title:'title'},function(flag){});
 * 4.alert+title+ComfirmValue+回调
 * AlertClassTip('message',{title:'title',option:['comfirm']},function(flag){});
 * 2.comfirm+title+ComfirmValue+CancelValue+回调=
 * AlertClassTip('messgae',{title:'title',option:['comfirm','cancel']},function(flag){console.log(flag)});
 * 其他参数选填
 */
var AlertClassTip = function(message, opt, callback) {
	var alert_v = 'alert', comfirm_v = 'comfirm';
	var AlertClass = new Object();
	AlertClass.comfirmValue = '确定';
	AlertClass.type = alert_v;
	AlertClass.setType = function(v) {
		this.type = v;
	};
	AlertClass.setComfirmValue = function(v) {
		this.comfirmValue = v;
	};
	AlertClass.cancelValue = '取消';
	AlertClass.setCancelValue = function(v) {
		this.cancelValue = v;
	};
	AlertClass.setTitle = function(v) {
		this.title = v;
	};
	AlertClass.message = message;
	AlertClass.opt = opt;
	AlertClass.setMessage = function(v) {
		this.message = v;
	};
	AlertClass.callback = callback;
	AlertClass.setCallback = function(v) {
		this.callback = v;
	};

	AlertClass.getType = function() {
		return this.type;
	};
	AlertClass.alertHtml = function() {
		// 创建弹出框的实体
		var alertHtml = '<div class="feed-back-modal"><div class="cover"></div><div class="alert">';
		if (this.title != undefined) {
			// title 一般不显示
			alertHtml += '<h5 class="tt">' + this.title + '</h5>';
		}
		// message
		alertHtml += '<div class="con">' + this.message + '</div>';
		// buts
		alertHtml += '<div class="btns">';
		if (this.getType() == alert_v) {
			alertHtml += '<a href="javascript:" class="btn sub comfirm">'
					+ this.comfirmValue + '</a>';
		} else if (this.getType() == comfirm_v) {
			alertHtml += '<a href="javascript:" class="btn cancel">'
					+ this.cancelValue + '</a>';
			alertHtml += '<a href="javascript:" class="btn sub comfirm">'
					+ this.comfirmValue + '</a>';
		}
		alertHtml += '</div>';
		alertHtml += '<div class="close-btn cancel"><span class="icon icon-close"></span></div></div></div>';
		return alertHtml;
	};
	AlertClass.executeCallBack = function(flag) {
		// flag=comfirm =1 cancel=2 其他=0
		var v_callback = this.callback;
		if (v_callback != undefined) {
			v_callback(flag);
		}
	};
	AlertClass.disappear = function() {
		this.alertHtmlDom.fadeOut(function() {
			this.alertHtmlDom.removeClass('active');
			this.alertHtmlDom.remove();
		}.bind(this));
	};
	AlertClass.alertBindClick = function() {
		this.alertHtmlDom.find('a.btn.sub.comfirm').click(function(e) {
			// 确定
			e.preventDefault();
			this.disappear();
			this.executeCallBack(1);
		}.bind(this));
		this.alertHtmlDom.find('a.btn.cancel').click(function(e) {
			// 确定
			e.preventDefault();
			this.disappear();
			this.executeCallBack(2);
		}.bind(this));
		this.alertHtmlDom.find('div.cover,div.close-btn.cancel').click(
				function(e) {
					// 确定
					e.preventDefault();
					this.disappear();
					this.executeCallBack(0);
				}.bind(this));
	};
	AlertClass.alertHtmlDomCreat = function() {
		var alertHtml = this.alertHtml();
		this.alertHtmlDom = $(alertHtml);
	};
	(function(alert_m) {
		// 判断类型
		var v = alert_m.opt, c = alert_m.callback;
		if (typeof v === 'undefined') {
			// 如果v undefined 则c不管
			alert_m.opt = undefined;
			alert_m.setType(alert_v);
			alert_m.callback = undefined;
		} else if (typeof v === 'function') {
			// v=function,则代表为回调函数
			alert_m.opt = undefined;
			alert_m.setType(alert_v);
			alert_m.setCallback(v);
		} else if (typeof v === 'object') {
			// v 为对象，代表配置
			alert_m.opt = v;
			var title = v.title;
			// comfirm
			if (typeof title === 'string') {
				// 定义title
				alert_m.setTitle(title);
			}
			var option = v.option;
			// 配置
			if (typeof option === 'object') {
				if (option.length == 0) {
					alert_m.setType(comfirm_v);
				} else if (option.length == 1) {
					alert_m.setType(alert_v);
					alert_m.setComfirmValue(option[0]);
				} else if (option.length == 2) {
					alert_m.setType(comfirm_v);
					alert_m.setComfirmValue(option[0]);
					alert_m.setCancelValue(option[1]);
				}
			} else if (typeof option === 'undefined') {
				alert_m.setType(comfirm_v);
			}
			// 判断回调
			if (typeof c === 'function') {
				alert_m.setCallback(c);
			}
		}
		// 创建
		alert_m.alertHtmlDomCreat();
		alert_m.alertBindClick();
		$(document.body).append(alert_m.alertHtmlDom);
		alert_m.alertHtmlDom.fadeIn(function() {
			alert_m.alertHtmlDom.addClass('active');
		}.bind(this));
	}(AlertClass));
};

// loading 展示
/**
 * 信息提交后的loading显示框 alertTips强制关闭后要显示的信息 tips loading的显示信息
 */
var LoadingClass = function(alertTips, tips) {
	var Loading = new Object();
	Loading.getTips = function() {
		return this.tips;
	};
	Loading.setTips = function(v) {
		this.tips = v;
	};
	Loading.loading = false;
	Loading.getLoading = function() {
		return this.loading;
	};
	Loading.bodyHtml = function() {
		var html = '<div class="hidden-full-page with-bg loading-page">';
		if (typeof this.getTips() === 'string') {
			html += '<div class="tips">' + this.getTips() + '</div>';
		}
		html += '<div class="la-ball-clip-rotate"><div></div></div></div>';
		return html;
	};
	Loading.getAlertTips = function() {
		return this.alertTips;
	};
	Loading.creatBody = function() {
		var html = this.bodyHtml();
		this.bodyDom = $(html);
	};
	Loading.setData = function(v, c) {
		if (typeof v === 'string') {
			this.alertTips = v;
		}
		if (typeof c === 'string') {
			this.setTips(c);
		}
	};
	Loading.setData(alertTips, tips);
	Loading.timeing = function() {
		if (typeof this.getAlertTips() === 'string') {
			setTimeout(function() {
				this.hide();
				AlertClassTip(this.getAlertTips());
			}.bind(this), 5000);
		}
	};
	Loading.show = function(v, c) {
		if (!this.getLoading()) {
			console.log(this);
			this.setData(v, c);
			this.creatBody();
			$(document.body).append(this.bodyDom);
			this.bodyDom.show();
			this.timeing();
			this.loading = true;
		}
	};
	Loading.hide = function() {
		if (this.getLoading()) {
			this.bodyDom.hide();
			this.bodyDom.remove();
			this.loading = false;
		}
	};
	return Loading;
};

/**
 * *此为插件原本 分为几大类 1.在构造时取数据展示（根据市选择医院） 2.已有数据展示(如选择科室） ) showType init（new） show
 * hide
 */

var PageSelectObj = function(title) {
	var obj = new Object();
	obj.title = title;
	obj.getTitle = function() {
		return this.title;
	};
	obj.showType = 'init';
	obj.html = function(items) {
		var html = '<div class="page off-canvas-right"><div class="content">'
				+ '<header class="top-bar text-center">'
				+ '<span class="icon-arr-l back-btn"></span>'
				+ this.getTitle()
				+ '</header>'
				+ '<div class="item-contain list-with-icon white-item with-arr">';
		if (typeof items === 'object') {
			html += this.buildLabel(items)
		}
		html += '</div></div></div>';
		return html;
	};
	obj.htmlCreatDom = function(items) {
		var html = this.html(items);
		this.htmlDom = $(html);
	};
	obj.request = function(requestUrl) {
		// 请求服务器数据
		$.getJSON(requestUrl, {
			datatime : new Date().getTime()
		}, function(data) {
			if (isSuccess(data)) {
				var items = data.itemList;
				if (isItemList(items)) {
					var labels = this.buildLabel(items);
					this.labelAppend(labels);
					this.bindClick();
					return;
				}
			}
			// 当取到数据不存在
			var labels_none = this.buildLabel();
			this.labelAppend(labels_none);
			this.bindClick();
		}.bind(this));
	};
	obj.labelAppend = function(labels) {
		this.htmlDom.find('div.item-contain').append(labels);
	};
	obj.buildLabel = function(items) {
		var labels = '';
		if (typeof items === 'undefined') {
			labels = '<label class="box txtC" data-uuid="">暂无' + this.title
					+ '数据</label>';
		} else {
			if (!isItemList(items)) {
				labels = '<label class="box txtC" data-uuid="">暂无' + this.title
						+ '数据</label>';
			} else {
				$.each(items, function(index, item) {
					labels += '<label class="box" data-uuid="' + item.uuid
							+ '">' + '<span class="item-name">' + item.name
							+ '</span>'
							+ '<span class="icon icon-arr-r"></span></label>';
				});
			}
		}
		return labels;
	};
	obj.hide = function() {
		this.showType = 'hide';
		this.htmlDom.removeClass('active');
		// this.htmlDom.hide();
	};
	obj.remove = function() {
		this.htmlDom.remove();
		this.showType = 'init';
	};
	obj.labelClick = function($item) {
		$item.click(function(e) {
			e.preventDefault();
			this.showType = 'hide';
			var uuid = $item.data('uuid');
			var name = $item.find('.item-name').html();
			var callback = this.callback;
			callback({
				uuid : uuid,
				name : name
			});
			// this.htmlDom.hide();
		}.bind(this));
	};
	obj.bindClick = function() {
		this.htmlDom.find('.back-btn').click(function(e) {
			this.hide();
		}.bind(this));
		this.htmlDom.find('label.box').each(function(index, item) {
			var $item = $(item);
			this.labelClick($item);
		}.bind(this));
	};
	obj.setItemData = function(opt) {
		if (this.dataUuid != opt.dataUuid) {
			if (opt.type == 'object') {
				this.htmlDom.find('div.item-contain').empty();
				var items = opt.items;
				if (!isItemList(items)) {
					items = undefined;
				}
				var labels = this.buildLabel(items);
				this.labelAppend(labels);
				this.bindClick();
			} else if (opt.type == 'request') {
				this.htmlDom.find('div.item-contain').empty();
				this.request(opt.requestUrl);
			}
		}
	};
	obj.domShow = function() {
		this.showType = 'show';
		this.htmlDom.addClass('active');
		// this.htmlDom.fadeIn();
	};
	/**
	 * opt type(object,request) items,requestUrl,dataUuid callback点击后的事件
	 */
	obj.show = function(opt, callback) {
		this.callback = callback;
		if (opt.type == 'object') {
			if (this.showType == 'init') {
				this.htmlCreatDom(opt.items);
				this.bindClick();
				$(document.body).append(this.htmlDom);
				// this.domShow();
			} else if (this.showType == 'show') {
				// 展示
			} else if (this.showType == 'hide') {
				// 隐藏
				this.setItemData(opt);
				this.domShow();
			}
		} else if (opt.type = 'request') {
			// 通过showType判断创建
			if (this.showType == 'init') {
				this.htmlCreatDom();
				this.request(opt.requestUrl);
				$(document.body).append(this.htmlDom);
				this.domShow();
			} else if (this.showType == 'show') {
				// 展示
			} else if (this.showType == 'hide') {
				// 隐藏
				this.setItemData(opt);
				this.domShow();
			}

		}
		this.dataUuid = opt.dataUuid;
	};
	// 触发激活dom
	obj.show({
		type : 'object',
		items : [],
		dataUuid : 'init'
	});
	obj.hide();
	return obj;
};

// 省市医院的3级级联
// threeType=1 医院 threeType=2 区域
var ThreeStageCascade = function(pro_data, threeType) {
	var obj = new Object();
	var privince = PageSelectObj('省份');
	var city = PageSelectObj('城市');
	var hosipital = PageSelectObj('医院');
	var district = PageSelectObj('区域');
	obj.threeType = threeType;
	obj.creatPrivince = function() {
		this.privince = privince;
	};
	obj.creatCity = function() {
		this.city = city;
	};
	obj.creatHosipital = function() {
		this.hosipital = hosipital;
	};
	obj.creatDistrict = function() {
		this.district = district;
	};
	obj.selectCity = function(uuid) {
		var citys = [];
		$.each(pro_data, function(index, item) {
			if (item.uuid == uuid) {
				citys = item.citys;
				return false;
			}
		});
		return citys;
	};
	obj.selectDistrict = function(p_uuid, c_uuid) {
		var district = [];
		var citys = this.selectCity(p_uuid);
		$.each(citys, function(p_i, p_t) {
			if (c_uuid == p_t.uuid) {
				district = p_t.districts;
				return false;
			}
		});
		return district;
	};
	obj.executeCallBack = function(privince_data, city_data, three_data) {
		var callback = this.callback;
		var call_data;
		if (this.threeType == 1) {
			call_data = {
				privince_data : privince_data,
				city_data : city_data,
				hosipital_data : three_data
			};
		} else if (this.threeType == 2) {
			call_data = {
				privince_data : privince_data,
				city_data : city_data,
				district_data : three_data
			};
		}
		callback(call_data);
	};
	obj.hide = function() {
		this.privince.hide();
		this.city.hide();
		if (this.threeType == 1) {
			this.hosipital.hide();
		} else if (this.threeType == 2) {
			this.district.hide();
		}

	};
	obj.control = function() {
		this.privince.show({
			type : 'object',
			items : pro_data,
			dataUuid : 'privince'
		}, function(privince_data) {
			if (privince_data.uuid != '') {
				var citys = this.selectCity(privince_data.uuid);
				this.city.show({
					type : 'object',
					items : citys,
					dataUuid : privince_data.uuid
				}, function(city_data) {
					if (city_data.uuid != '') {
						if (this.threeType == 1) {
							var requestUrl = '/api/webchat/hospital/'
									+ city_data.uuid;
							this.hosipital.show({
								type : 'request',
								requestUrl : requestUrl,
								dataUuid : city_data.uuid
							}, function(hosipital_data) {
								// 医院数据
								this.executeCallBack(privince_data, city_data,
										hosipital_data);
							}.bind(this));
						} else if (this.threeType == 2) {
							var districts = this.selectDistrict(
									privince_data.uuid, city_data.uuid);
							this.district.show({
								type : 'object',
								items : districts,
								dataUuid : city_data.uuid
							}, function(district_data) {
								// 医院数据
								this.executeCallBack(privince_data, city_data,
										district_data);
							}.bind(this));
						}
					}
				}.bind(this));
			}
		}.bind(this));
	};
	obj.show = function(callback) {
		this.callback = callback;
		this.control();
	};
	obj.creatPrivince();
	obj.creatCity();
	if (obj.threeType == 1) {
		obj.creatHosipital();
	} else if (obj.threeType == 2) {
		obj.creatDistrict();
	}
	return obj;
};

/* 血腥提醒弹框 */
var bloodyAlert = function(bloody) {
	var obj = new Object();
	obj.first = true;
	obj.bloody = bloody;
	obj.setCallback = function(callBack) {
		this.callBack=callBack;

	};
	obj.exectCallBack=function(type){
		var call=this.callBack;
		if(typeof call === 'function'){
			call(type);
		}
	};
	obj.createTips = function() {
		var msg_tips = '<p><img src="/assets/img/app/icon_bloody.png"></p><p class="mt8">该视频为手术视频，内含血腥画面，请在观看时注意。</p>';
		AlertClassTip(msg_tips, {
			title : '请注意！',
			option : [ '继续观看', '关闭' ]
		}, function(flag) {
			this.first = false;
			var call=this.callBack;
			if (flag == 1) {
				this.exectCallBack(1);
			}else{
				this.exectCallBack(2);
			}
		}.bind(this));
	};
	obj.alertCtrl = function(callback) {
		this.setCallback(callback);
		if (this.bloody == '1') {
			this.bloody = true;
		} else if (this.bloody == '0') {
			this.bloody = false;
		}
		if (this.first && this.bloody) {
			this.createTips();
		}
	};
	return obj;
};
