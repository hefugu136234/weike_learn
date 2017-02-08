$(function() {

	$('#close_reg_banner').click(function() {
		$('#reg_banner_page').fadeOut();
	});

	var reg_banner_swiper = new Swiper("#reg_banner", {
		pagination : "#reg_banner_pagination",
		paginationClickable : true,
		loop : false
	});


	$('.regChange_c').click(function() {
		$('#normalForm').hide();
		$('#companyForm').show();
	});

	$('.regChange_n').click(function() {
		$('#companyForm').hide();
		$('#normalForm').show();
	});

	$('.protocol-show-btn').each(function() {
		$(this).click(function() {
			$('#protocol_modal').show();
		});
	});

	$('#protocol_modal').find('.closePageBtn').click(function() {
		$('#protocol_modal').hide();
	});

	// 用户获取手机验证码
	// 获取验证码按钮
	var validCodeBut = $('#valid_code_but');
	// 验证码
	var valid_code = $('#valid_code');
	var mobileInput = $('#mobile');
	var nextStep = $('#next_step1');
	var countWait = 60;
	var $city = $('#city');
	var $city_text = $($city.find('.select-text').get(0));
	var $hosipital = $('#hosipital');
	var $hosipital_text = $($hosipital.find('.select-text').get(0));
	var $department = $('#department');
	var $department_text = $($department.find('.select-text').get(0));
	var $company = $('#company');
	var $company_text = $($company.find('.select-text').get(0));

	// 发送验证码
	validCodeBut
			.click(function(event) {
				event.preventDefault();
				var objThis = $(this);

				if (mobileInput.val() == '') {
					AlertClassTip('请填写您的手机号码！');
					return false;
				} else if (mobileInput.val() != ''
						&& !verify_phone.test(mobileInput.val())) {
					AlertClassTip('请填写正确的手机号码！');
					mobileInput.val('');
					mobileInput.focus();
					return false;
				}

				$.post('/api/webchat/register/code/send', {
					'mobile' : mobileInput.val()
				}, function(data) {
					if (data.status == 'success') {
						countTime(objThis);
						AlertClassTip('验证码已经发送成功');
					} else {
						if (!!data.message) {
							AlertClassTip(data.message)
						} else {
							AlertClassTip("验证码发送失败，请稍后再试");
						}
					}
				});
			});

	function countTime(obj) {
		var my_countTime = window.setInterval(function() {
			$(obj).attr("disabled", true);
			$(obj).addClass("disabled");
			$(obj).html(countWait + "秒后重新获取");
			countWait--;
			if (countWait == 0) {
				$(obj).attr("disabled", false);
				$(obj).removeClass("disabled");
				$(obj).html("获取验证码");
				window.clearInterval(my_countTime);
				countWait = 60;
			}
		}, 1000);
	}

	nextStep.click(function(event) {
		event.preventDefault();
		if (mobileInput.val() == '') {
			AlertClassTip('请填写手机号码');
			return false;
		}
		if (valid_code.val() == '') {
			AlertClassTip('请填写验证码');
			return false;
		}
		$.post('/api/webchat/register/code/valid', {
			'code' : valid_code.val(),
			'mobile' : mobileInput.val()
		}, function(data) {
			if (data.status == 'success') {
				$('#step1').hide();
				$('#step2').show();
				$('#hidden_vaild_code').val(data.valid_code);
			} else {
				if (!!data.message) {
					AlertClassTip(data.message)
				} else {
					AlertClassTip("验证码错误");
				}
			}
		});
	});

	// 医生注册
	$('#sub_doc').click(function(event) {
		event.preventDefault();
		var nickname = $('#nickname').val();
		var password = $('#password').val();
		var re_password = $('#re_password').val();
		var valid_code = $('#hidden_vaild_code').val();
		var activeCode = '';// $('#card_code_doc').val();
		//var province = $('#province').val();
		var city = $city_text.data('uuid');
		var hospital = $hosipital_text.data('uuid');
		var department = $department_text.data('uuid');
		var professor = $('#professor').val();
		var $this=$(this);
		//$('#sub_doc').prop('disabled', true);
		if (nickname == '') {
			AlertClassTip('请填写您的姓名！');
			return false;
		}
		if (password == '') {
			AlertClassTip('请设置您的密码！');
			return false;
		}
		if (re_password == '') {
			AlertClassTip('请再次填写密码！');
			return false;
		} else if (password != re_password) {
			AlertClassTip('两次填写的密码不一致！');
			return false;
		}
		if (!city||city == '') {
			AlertClassTip('请选择您的所在城市！');
			return false;
		}
		if (!hospital||hospital == '') {
			AlertClassTip('请选择医院！');
			return false;
		}
		if (!department||department == '') {
			AlertClassTip('请选择科室！');
			return false;
		}

		var protocol_check = $('#protocol_doc').is(':checked');
		if (protocol_check == false) {
			AlertClassTip('请选择用户协议！');
			return false;
		}
		$this.prop('disabled', true);
		$.post('/api/webchat/user/register/save', {
			'valid_code' : valid_code,
			'password' : password,
			'nickname' : nickname,
			'hosipital' : hospital,
			'city' : city,
			'professor' : professor,
			'department' : department,
			'activeCode' : activeCode,
			'type' : 0
		}, function(data) {
			$this.prop('disabled', false);
			if (data.status == 'success') {
				if (!!activeCode) {
					// 1=企业
					localpage(1);
				} else {
					localpage(3);
				}
			} else if (data.status == 'error') {
				AlertClassTip(data.message);
				// 绑定失败，跳转登录页面
				window.location.href = '/api/webchat/page/login';
			} else if (data.status = 'param invalid') {
				AlertClassTip('用户注册成功，云卡激活失败，请到个人中心重新激活');
				window.location.href = '/api/webchat/my/center';
			} else {
				if (!!data.message) {
					AlertClassTip(data.message);
				} else {
					AlertClassTip("注册失败");
				}
			}
		});
	});

	// 企业用户
	$('#sub_Company').click(function(event) {
		event.preventDefault();
		var nickname = $('#nickname').val();
		var password = $('#password').val();
		var re_password = $('#re_password').val();
		var valid_code = $('#hidden_vaild_code').val();
		var activeCode = '';// $('#card_code_com').val();
		var professor = $('#professor_com').val();
		var company = $company_text.data('uuid');
		var $this=$(this);
		if (nickname == '') {
			AlertClassTip('请填写您的姓名！');
			return false;
		}
		if (password == '') {
			AlertClassTip('请设置您的密码！');
			return false;
		}
		if (re_password == '') {
			AlertClassTip('请再次填写密码！');
			return false;
		} else if (password != re_password) {
			AlertClassTip('两次填写的密码不一致！');
			return false;
		}
		if (!company||company == '') {
			AlertClassTip('请选择您所在的公司');
			return false;
		}
		var protocol_check = $('#protocol_com').is(':checked');
		if (protocol_check == false) {
			AlertClassTip('请选择用户协议！');
			return false;
		}
		$this.prop('disabled', true);
		$.post('/api/webchat/user/register/save', {
			'valid_code' : valid_code,
			'password' : password,
			'nickname' : nickname,
			'professor' : professor,
			'activeCode' : activeCode,
			'company' : company,
			'type' : 1
		}, function(data) {
			$this.prop('disabled', false);
			if (data.status == 'success') {
				if (!!activeCode) {
					// 2=企业
					localpage(2);
				} else {
					localpage(4);
				}
			} else if (data.status == 'error') {
				AlertClassTip(data.message);
				// 绑定失败，跳转登录页面
				window.location.href = '/api/webchat/page/login';
			} else if (data.status = 'param invalid') {
				AlertClassTip('用户注册成功，云卡激活失败，请到个人中心重新激活');
				window.location.href = '/api/webchat/my/center';
			} else {
				if (!!data.message) {
					AlertClassTip(data.message);
				} else {
					AlertClassTip("注册失败");
				}
			}
		});
	});

	// 获取省市数据
	$.get('/info/getLocation', function(data) {
		var pro_data = jQuery.parseJSON(data);
		var select_obj = ThreeStageCascade(pro_data, 1);
		var clickBindThing = function(data) {
			select_obj.hide();
			if (data.hosipital_data.uuid != '') {
				var name = data.privince_data.name + '-' + data.city_data.name;
				$city_text.data('uuid', data.city_data.uuid);
				$city_text.html(name);
				$hosipital_text.data('uuid', data.hosipital_data.uuid);
				$hosipital_text.html(data.hosipital_data.name);

			}
		};
		$city.click(function() {
			select_obj.show(function(data) {
				clickBindThing(data);
			});
		});
		$hosipital.click(function() {
			select_obj.show(function(data) {
				clickBindThing(data);
			});
		});
	});

	$.getJSON('/api/webchat/department/data', function(data) {
		if (isSuccess(data)) {
			var items = data.itemList;
			var select = PageSelectObj('科室');
			$department.click(function() {
				select.show({
					type : 'object',
					items : items,
					dataUuid : 'department'
				}, function(data) {
					select.hide();
					if (data.uuid != '') {
						$department_text.data('uuid', data.uuid);
						$department_text.html(data.name);
					}
				});
			});
		}
	});

	$.getJSON('/api/webchat/manufacturer/data', function(data) {
		if (isSuccess(data)) {
			var items = data.itemList;
			var select = PageSelectObj('企业');
			$company.click(function() {
				select.show({
					type : 'object',
					items : items,
					dataUuid : 'company'
				}, function(data) {
					select.hide();
					if (data.uuid != '') {
						$company_text.data('uuid', data.uuid);
						$company_text.html(data.name);
					}
				});
			});
		}
	});

});

function localpage(num) {
	window.location.href = '/api/webchat/reg/success/' + num;
}
