$(function() {
	// 加载省的数据
	var province_id = $('#province');
	if (province_id.length>0) {
		$.getJSON('/f/list/province', function(data) {
			if (data.status == 'success') {
				buildOption(data, province_id, '省份数据加载出错');
			} else {
				if (!!data.message) {
					alert(data.message);
				} else {
					alert("省份数据加载出错");
				}
			}
		});
	}

	var userDepartment_id = $('#userDepartment');
	// 加载科室的数据
	if (userDepartment_id.length>0) {
		$.getJSON('/f/list/department', function(data) {
			if (data.status == 'success') {
				buildOption(data, userDepartment_id, '科室数据加载出错');
			} else {
				if (!!data.message) {
					alert(data.message);
				} else {
					alert("科室数据加载出错");
				}
			}
		});
	}

	// 获取验证码
	$('#codeBtn').click(function() {
		var flag = checkMobile($('#userMobile'));
		if (flag) {
			// 验证成功按钮失效
			$('#codeBtn').attr('disabled', true);
			$.post('/f/subscribe/code/send', {
				mobile : $('#userMobile').val()
			}, function(data) {
				if (data.status == 'success') {
					alert('验证码发送成功');
					// 失效计时
					countTime($('#codeBtn'));
				} else {
					$('#codeBtn').attr('disabled', false);
					if (!!data.message) {
						alert(data.message);
					} else {
						alert("发送失败");
					}
				}
			});
		}
	});

	// 验证短信验证码的-下一步
	$('#nextBtn').click(function() {
		var flag = checkMobile($('#userMobile'));
		if (flag) {
			var code = $('#validCode').val();
			if (!!code) {
				$.post('/f/subscribe/code/valid', {
					mobile : $('#userMobile').val(),
					code : code
				}, function(data) {
					if (data.status == 'success') {
						$('#step1').hide();
						$('#step2').show();
						// console.log(data.valid_code);
						$('#valid_code').val(data.valid_code);
					} else {
						if (!!data.message) {
							alert(data.message);
						} else {
							alert("验证失败");
						}
					}
				});

			} else {
				alert('请输入验证码');
			}
		}
	});

	// 普通用户预约提交
	$('#submit_Btn').click(function() {
		var userName = $('#userName').val();
		var province = $('#province').val();
		var city = $('#city').val();
		var hospital = $('#hospital').val();
		var userDepartment = $('#userDepartment').val();
		var userJobTitle = $('#userJobTitle').val();
		if (userName == '') {
			alert('请填写您的姓名！');
			return;
		}
		if (province == '') {
			alert('请选择省份！');
			return;
		}
		if (city == '') {
			alert('请选择城市！');
			return;
		}
		if (hospital == '') {
			alert('请选择医院！');
			return;
		}
		if (userDepartment == '') {
			alert('请选择科室！');
			return;
		}
		if (userJobTitle == '') {
			alert('请选择职位！');
			return;
		}
		// 提交数据
		$.post('/f/subscribe/save', {
			valid_code : $('#valid_code').val(),
			userType : '普通用户',
			name : userName,
			hosipitalUuid : hospital,
			departmentUuid : userDepartment,
			position : userJobTitle,
		}, function(data) {
			if (data.status == 'success') {
				$('#step2').hide();
				$('#step3').show();
			} else {
				if (!!data.message) {
					alert(data.message);
				} else {
					alert("保存失败");
				}
			}
		});
	});

	$('#submit_Btn2').click(
					function() {
						var regTel = /^0\d{2,3}-?\d{7,8}$/; // 验证座机号码
						var regEmail = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/; // 验证邮箱地址
						var userName = $('#userName').val();
						var userTel = $('#userTel').val();
						var companyName = $('#companyName').val();
						var brandName = $('#brandName').val();
						var position = $('#position').val();
						var companyEmail = $('#companyEmail').val();
						var companyAddress = $('#companyAddress').val();
						if (userName == '') {
							alert('请填写您的姓名！');
							return;
						}
						if (userTel == '') {
							alert('请输入正确的区号加座机号！');
							return;
						} else if (userTel != '' && !regTel.test(userTel)) {
							alert('请输入正确的区号加座机号！');
							$('#userTel').val('');
							$('#userTel').focus();
							return;
						}
						if (companyName == '') {
							alert('请填写您的企业名称！');
							return;
						}
						if (brandName == '') {
							alert('请填写您的品牌名称！');
							return;
						}
						if (position == '') {
							alert('请填写您的职位！');
							return;
						}
						if (companyEmail == '') {
							alert('请填写您的企业邮箱！');
							return;
						} else if (companyEmail != ''
								&& !regEmail.test(companyEmail)) {
							alert('请填写正确的邮箱格式！');
							$('#companyEmail').val('');
							$('#companyEmail').focus();
							return;
						}
						if (companyAddress == '') {
							alert('请填写您的办公地址！');
							return;
						}

						$.post('/f/subscribe/save', {
							valid_code : $('#valid_code').val(),
							userType : '品牌用户',
							name : userName,
							phone : userTel,
							company :companyName,
							group : brandName,
							position : position,
							mail :companyEmail,
							mark : companyAddress
						}, function(data) {
							if (data.status == 'success') {
								$('#step2').hide();
								$('#step3').show();
							} else {
								if (!!data.message) {
									alert(data.message);
								} else {
									alert("保存失败");
								}
							}
						});
					});

});
