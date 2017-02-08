var verify_phone = /^(0|86|17951)?(13[0-9]|15[012356789]|17[0-9]|18[0-9]|14[57])[0-9]{8}$/; // 验证手机号码
var verify_http = new RegExp("^(http).*");
var countdown_count = 60; // 倒计时默认时间

$(document).ready(function() {
	var nowTime = new Date().getTime();
	var $divNavSuer = $('div.pull-right.nav-user');
	aAddClsssHide($divNavSuer);
	// 顶部登录模块数据处理
	$.getJSON('/f/web/is/login/info', {
		'dateTime' : nowTime
	}, function(data) {
		if (isSuccess(data)) {
			if (data.login) {
				var login_ing_a = $('#login_ing_a');
				if (!!data.photo) {
					login_ing_a.find('img.img-circle').attr('src', data.photo);
				}
				login_ing_a.find('p.name').html(data.showName);
				if (data.vip == 'in_use') {
					$('#vip_login_img').show();
				} else {
					$('#vip_login_img').hide();
				}
				if (data.realName) {
					$('#real_login_img').show();
				} else {
					$('#real_login_img').hide();
				}
				aRemoveClassHide(login_ing_a);
				aAddClsssHide($('#no_login_a'));
			} else {
				aRemoveClassHide($('#no_login_a'));
				aAddClsssHide($('#login_ing_a'));
			}
			aRemoveClassHide($divNavSuer);
		}
	});

	//点击登录之前的处理
	$('#no_login_a').click(function(e){
		e.preventDefault();
		var url=location.href.split('#')[0];
		$.post('/f/web/record/prior/url',{
			url:url
		},function(data){
			if (isSuccess(data)){
				location.href='/f/web/login';
			}else{
				console.log(data);
			}
		});
	});

	// 顶部获取菜单的数据
	$.getJSON('/f/web/top/menu', function(data) {
		if (isSuccess(data)) {
			var item_list = data.itemList;
			var $top_menu_list = $('#top_menu_list');
			if (isItemList(item_list)) {
				$.each(item_list, function(index, item) {
					$top_menu_list.append(buildTopMenuAitem(item));
				});
			}
		}

	});

	// 顶部弹出菜单
	$('#nav_index').hover(function() {
		$('#sub_nav_con').show();
	}, function() {
		$('#sub_nav_con').hide();
	});

	// 顶部菜单收回
	$('#sub_nav_con').hover(function() {
		$(this).show();
	}, function() {
		$(this).hide();
	});

	// 通用滚动banner
	if (typeof Swiper != 'undefined') {
		var default_banner = new Swiper('.default-banner', {
			pagination : '.default-banner-ctrl',
			paginationClickable : true,
			autoplay : 5000,
			autoplayDisableOnInteraction : false
		});
	}

	// 返回顶部
	$('#back_top').on('click', function() {
		$('body,html').animate({
			scrollTop : 0
		}, 400);
		return false;
	});

	// 顶部菜单fixed
	// var $win = $(window);
	// var $top_bar = $('#top_bar');
	// var navTop = $top_bar.length && $top_bar.offset().top;
	// var isFixed = 0;

	// scrollTopNav();
	// $win.on('scroll', scrollTopNav);

	// function scrollTopNav(){
	// var scrollTop = $win.scrollTop();

	// if (scrollTop >= navTop && !isFixed){
	// isFixed = 1;
	// $top_bar.addClass('fixed');
	// } else if (scrollTop <= navTop && isFixed){
	// isFixed = 0;
	// $top_bar.removeClass('fixed');
	// }
	// }

	// 点击收藏公用方法
	$('.star-btn').each(function() {
		var $that = $(this);
		var $icon = $that.find('.icon');
		var $tt = $that.find('.tt');
		var $num = $that.find('.num');
		var uuid = $that.data('uuid');

		$that.on('click', function(e) {
			e.preventDefault();
			$.post('/f/web/res/collection/status', {
				'uuid' : uuid
			}, function(data) {
				if (isSuccess(data)) {
					if (data.reStatus) {
						$icon.attr('class', 'icon icon-star');
						$tt.html('已收藏');
					} else {
						$icon.attr('class', 'icon icon-star-line');
						$tt.html('收藏');
					}
					$num.html(data.reCount);
				} else {
					alert(data.message);
				}
			});
		});
	});

	// 点击赞公用方法
	$('.like-btn').each(function() {
		var $that = $(this);
		var $icon = $that.find('.icon');
		var $tt = $that.find('.tt');
		var $num = $that.find('.num');
		var uuid = $that.data('uuid');

		$that.on('click', function(e) {
			e.preventDefault();
			$.post('/f/web/res/praise/status', {
				'uuid' : uuid
			}, function(data) {
				if (isSuccess(data)) {
					if (data.reStatus) {
						$tt.html('已赞');
						var score = data.integral;
						if (score > 0) {
							addScoreTips(score);
						}
					} else {
						$tt.html('赞');
					}
					$num.html(data.reCount);
				} else {
					alert(data.message);
				}
			});
		});
	});

	// 限制字符串字数
	limitStr();

});

function limitStr() {
	$('.limit-str-text').each(function() {
		var $that = $(this);
		var text = $that.html();
		var min_str = $that.data('minstr');
		var new_str = cutStrForNum(text, min_str);

		$that.html(new_str);
	});
}

// 触发添加积分效果
function addScoreTips(score){
	var $score_ui = $('<div class="score-alert-tips">'
								+	'<h5 class="tt">积 分</h5>'
								+	'<div class="num">+' + score + '</div>'
								+	'</div>');

	$('body').append($score_ui);
	$score_ui.animate({top: '50%'});
	setTimeout(function() {
		$score_ui.fadeOut(function(){
			$score_ui.remove();
		});
	}, 1500);
}

function aAddClsssHide(item) {
	if (!item.hasClass('hide')) {
		item.addClass('hide');
	}
}

function aRemoveClassHide(item) {
	if (item.hasClass('hide')) {
		item.removeClass('hide');
	}
}

// iChecks设置
function iChecksSet(obj) {
	var skin = (obj.attr('data-skin') !== undefined) ? "_"
			+ obj.attr('data-skin') : "", color = (obj.attr('data-color') !== undefined) ? "-"
			+ obj.attr('data-color')
			: "";
	var opt = {
		checkboxClass : 'icheckbox' + skin + color,
		radioClass : 'iradio' + skin + color,
	}
	obj.iCheck(opt);
}

// 验证码按钮倒计时
function countdownCode(btn) {
	var my_countTime = window.setInterval(function() {
		$(btn).attr('disabled', true);
		$(btn).addClass('disabled');
		$(btn).html(countdown_count + '秒后重新获取');
		countdown_count--;
		if (countdown_count == 0) {
			$(btn).attr('disabled', false);
			$(btn).removeClass('disabled');
			$(btn).html('获取验证码');
			window.clearInterval(my_countTime);
			countdown_count = 60;
		}
	}, 1000);
}

// 倒计时函数
function countDownTimer(countObj, intDiff) {
	window.setInterval(function() {
		var day = 0, hour = 0, minute = 0, second = 0;// 时间默认值
		if (intDiff > 0) {
			day = Math.floor(intDiff / (60 * 60 * 24));
			hour = Math.floor(intDiff / (60 * 60)) - (day * 24);
			minute = Math.floor(intDiff / 60) - (day * 24 * 60) - (hour * 60);
			second = Math.floor(intDiff) - (day * 24 * 60 * 60)
					- (hour * 60 * 60) - (minute * 60);
		}
		if (minute <= 9)
			minute = '0' + minute;
		if (second <= 9)
			second = '0' + second;
		countObj.find('.day-show').html(day);
		countObj.find('.hour-show').html(hour);
		countObj.find('.minute-show').html(minute);
		countObj.find('.second-show').html(second);
		intDiff--;
	}, 1000);
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

// 添加图标
function itemAddIcons(item, target) {
	var icon_name = target.data('name');
	target.addClass(menuImgType(icon_name));
}

// 创建头部菜单UI
function buildTopMenuAitem(item) {
	var a_href = '/f/web/category/list/' + item.uuid;
	var type_class = menuImgType(item.name);
	var a_item = '<a href="' + a_href + '" class="item"><span class="'
			+ type_class + '"></span>' + item.name + '<span class="num">（'
			+ item.resCount + '资源）</span></a>';
	return a_item;
}

// 筛选板块图标
function menuImgType(name) {
	var resultType = 'icon';
	if (!name) {
		return resultType;
	}
	var typeClass = '';
	switch (name) {
	case '外科':
		typeClass = 'icon-waike';
		break;
	case '内科':
		typeClass = 'icon-neike';
		break;
	case '重症医学':
		typeClass = 'icon-knowledge';
		break;
	case '皮肤':
		typeClass = 'icon-pifu';
		break;
	case '消化内科':
		typeClass = 'icon-xiaohua';
		break;
	case '麻醉':
		typeClass = 'icon-mazui';
		break;
	case '儿科':
		typeClass = 'icon-erke';
		break;
	}
	if (!!typeClass) {
		resultType = resultType + ' ' + typeClass;
	}
	return resultType;
}

// 图片过滤
function coverShow(cover) {
	var img_src = cover;
	if (!verify_http.test(cover)) {
		img_src = "http://cloud.lankr.cn/api/image/" + cover
				+ "?m/2/h/500/f/jpg";
	}
	return img_src;
}

// 顶部菜单当前状态
function activeNav(nav_item) {
	$('#' + nav_item).addClass('active');
}

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

	// 如果给定字符串小于指定长度，则返回源字符串；
	// if (str_length < len) {
	// return str;
	// }
	return result;
}

/**
 * 生成分页的对象
 */
var pageObj = function(total, batchSize, currentPage) {
	var pageObject = new Object();
	pageObject.total = total;
	pageObject.batchSize = batchSize;
	pageObject.currentPage = currentPage;
	return pageObject;
}

/**
 * 分页的控制页面 total=总数据量 batchSize=分页量 currentPage=当前分页
 */
function pageControllerInit(pageObject, containerCallback, requestCallback) {
	var total = pageObject.total;
	var batchSize = pageObject.batchSize;
	var currentPage = pageObject.currentPage;
	var container = containerCallback();
	container.empty();
	if (total == 0) {
		return;
	}
	var show_interval = 3;
	var pages = Math.ceil(total / batchSize);
	var pre = $('<li data-page="' + Math.max(1, currentPage - 1)
			+ '"><a href="javascript:void(0);">上一页</a></li>');
	var next = $('<li data-page="' + Math.min(pages, currentPage + 1)
			+ '"><a href="javascript:void(0);" >下一页</a></li>');
	var first = $('<li data-page="1"><a href="javascript:void(0);" >1</a></li>');
	var tail = $('<li data-page="' + pages + '"><a href="javascript:void(0);">'
			+ pages + '</a></li>');
	var others = $('<li><span>…</span></li>')
	container.append(pre);
	container.append(first);
	if (currentPage - show_interval > 2) {
		container.append(others.clone());
	}
	var max_show = 1;
	for (var i = currentPage - show_interval; i <= currentPage + show_interval; i++) {
		if (i > 1 && i < pages) {
			var item = $('<li data-page="' + i
					+ '"><a href="javascript:void(0);">' + i + '</a></li>');
			container.append(item);
			if (currentPage == i) {
				item.addClass('active')
			}
			max_show = i;
		}
	}
	if (max_show < pages - 1) {
		container.append(others.clone());
	}
	if (pages > 1) {
		container.append(tail);
	}
	container.append(next);
	if (currentPage == 1) {
		pre.addClass('disabled');
		first.addClass('active');
	}
	if (currentPage == pages) {
		next.addClass('disabled');
		tail.addClass('active');
	}
	container.find('li').each(function(i, e) {
		var $e = $(e);
		if ($e.hasClass('disabled') || $e.hasClass('active')) {
			return;
		}
		var page = $e.data("page")
		if (page > 0) {
			$e.click(function(e) {
				console.log('当前点击：' + page);
				pageObject.currentPage = page;
				requestCallback(pageObject);
			})
		}
	});
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
			$.post('/f/web/res/first/view/add/jifen', {
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
