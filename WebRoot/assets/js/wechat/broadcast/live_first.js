$(function() {
	var enterBtn = $('#enter_live');
	var isPinCode = $('#isPinCode').val();
	var uuid = $('#uuid').val();
	// isPinCode == 'true'

	// 倒计时开始
	var countDownStart = $('#countDownStart').val();
	if (!!countDownStart) {
		countDownStart = parseInt(countDownStart);
		if (countDownStart > 0) {
			$('.countdown').each(function() {
				countDownTimer($(this), countDownStart);
			});
		}
	}

	enterBtn.click(function(event) {
		event.preventDefault();
		// 请求判断跳转那个页面
		$.post('/api/webchat/cast/book/user', {
			'uuid' : uuid
		}, function(data) {
			if (data.status == 'success') {
				var url = '/api/webchat/broadcast/detail/page/' + uuid;
				redHerf(url);
			} else if (data.status == 'record') {
				if (data.hasRes == 'hasRes') {
					var url = '/api/webchat/resource/first/view/'
							+ data.resUuid;
					redHerf(data.resUrl);
				} else if (data.hasRes == 'hasUrl') {
					redHerf(data.resUrl);
				} else {
					AlertClassTip(data.message);
				}
			} else if (data.status == 'error') {
				AlertClassTip(data.message);
			} else if (data.status == 'failure') {
				if (data.message == 'not login') {
					var url = '/api/webchat/broadcast/first/page/' + uuid;
					loginAlert(url);
				} else {
					// 刷新报名人数的数据
					$('span.num').html(data.currentBook);
					if (data.bookLimit > 0) {
						$('span.group').html('/' + data.currentBook + '人');
					} else {
						$('span.group').html('/无限制');
					}
					alert(data.message);
				}
			}
		});
	});
});

// 2016-3-25修改，添加报名
function redHerf(refurl) {
	$('#broadcast_bg, #broadcast_guide').fadeOut(function() {
		location.href = refurl;
	});
	// setTimeout(function() {
	// location.href = url;
	// }, 100);
}

function hasCode() {
	enterBtn.click(function() {
		var codeInputVal = $('#code_input').val();
		if (codeInputVal == '') {
			alert('请先输入PINCODE！');
			return false;
		} else {
			$.getJSON('/api/webchat/broadcast/check/pincode', {
				'pincode' : codeInputVal,
				'uuid' : uuid
			}, function(data) {
				if (data.status == 'success') {
					redHerf('');
				} else {
					alert(data.message);
				}
			});
		}
	});
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
		countObj.find('.day_show').html(day);
		countObj.find('.hour_show').html(hour);
		countObj.find('.minute_show').html(minute);
		countObj.find('.second_show').html(second);
		intDiff--;
	}, 1000);
}

function checkInput(input, btn) {
	if (input.val() == '') {
		btn.removeClass('active');
	} else {
		btn.addClass('active');
	}
}

function noCode() {
	// 倒计时开始
	var countDownStart = $('#countDownStart').val();
	if (!!countDownStart) {
		countDownStart = parseInt(countDownStart);
		if (countDownStart > 0) {
			$('.countdown').each(function() {
				countDownTimer($(this), countDownStart);
			});
		}
	}
	enterBtn.click(function(event) {
		event.preventDefault();
		// 请求判断跳转那个页面
		$.post('/api/webchat/cast/book/user', {
			'uuid' : uuid
		}, function(data) {
			if (data.status == 'success') {
				redHerf('');
			} else if (data.status == 'record') {
				if (data.hasRes == 'hasRes') {
					var url = '/api/webchat/resource/first/view/'
							+ data.resUuid;
					redHerf(data.resUrl);
				} else if (data.hasRes == 'hasUrl') {
					redHerf(data.resUrl);
				} else {
					alert(data.message);
				}
			} else if (data.status == 'error') {
				alert(data.message);
			} else if (data.status == 'failure') {
				if (data.message == 'not login') {
					var url = '/api/webchat/broadcast/first/page/' + uuid;
					loginAlert(url);
				} else {
					// 刷新报名人数的数据
					$('span.num').html(data.currentBook);
					if (data.bookLimit > 0) {
						$('span.group').html('/' + data.currentBook + '人');
					} else {
						$('span.group').html('/无限制');
					}
					alert(data.message);
				}
			}
		});
	});
}
