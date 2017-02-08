$(function() {
	var $certified_form_list = $('#certified_form_list');
	$('#open_certified_form').on('click', function() {
		$certified_form_list.stop().slideToggle();
	});

	var $city = $('#city');
	var $city_text = $($city.find('.select-text').get(0));
	var $hosipital = $('#hosipital');
	var $hosipital_text = $($hosipital.find('.select-text').get(0));
	var $department = $('#department');
	var $department_text = $($department.find('.select-text').get(0));
	var $professor = $('#professor');
	var $user_license = $('#user_license');
	var $certified_feedback = $('#certified_feedback');
	var $userName = $('#userName');

	wx.ready(function() {
		// 拍照上传图片
		var $takeCertified = document.querySelector('#takeCertified');
		if ($takeCertified != null) {
			$takeCertified.onclick = function() {
				var call_back_element_id = "#user_license";
				wx.chooseImage({
					count : 1,
					sizeType : [ 'original', 'compressed' ],
					sourceType : [ 'album', 'camera' ],
					success : function(res) {
						var localIds = res.localIds;
						wx.uploadImage({
							localId : localIds[0],
							isShowProgressTips : 1,
							success : function(res) {
								var server_id = res.serverId;
								download_image(server_id);
							}
						});
					}
				});
			}
		}

	});

	var download_image = function(server_id) {
		var dateTime = new Date().getTime();
		$.getJSON('/api/webchat/wx/get/upload/file', {
			'media_id' : server_id,
			'dateTime' : dateTime
		}, function(data) {
			if (data.status == 'success') {
				$user_license.attr('src', data.message);
				$('#takeCertified').hide();
				$user_license.show();
			} else {
				AlertClassTip(data.message)
			}
		});
	};

	$('#submitBtn').click(function(e) {
		e.preventDefault();
		var $this = $(this);
		var user_license = $user_license.attr('src');
		var name = $('#real_name').val();
		var city = $city_text.data('uuid');
		var hosipital = $hosipital_text.data('uuid');
		var office = $department_text.data('uuid');
		var professor = $professor.val();
		if (!user_license) {
			AlertClassTip('请上传您的执照！');
			return false;
		}
		if (!name) {
			$certified_form_list.show();
			AlertClassTip('请填写您的真实姓名！');
			return false;
		}
		if (!city) {
			$certified_form_list.show();
			AlertClassTip('请选择城市！');
			return false;
		}
		if (!hosipital) {
			$certified_form_list.show();
			AlertClassTip('请选择医院！');
			return false;
		}
		if (!office) {
			$certified_form_list.show();
			AlertClassTip('请选择科室！');
			return false;
		}
		if (!professor) {
			$certified_form_list.show();
			AlertClassTip('请填写职称！');
			return false;
		}
		$this.prop('disabled', true);
		$.post('/api/webchat/activity/real/name/submit', {
			'real_name' : name,
			'user_license' : user_license,
			'city' : city,
			'hosipital' : hosipital,
			'office' : office,
			'professor' : professor
		}, function(data) {
			$this.prop('disabled', false);
			if (data.status == 'success') {
				$userName.html(name);
				$certified_feedback.show();
			} else {
				if (!!data.message) {
					AlertClassTip(data.message);
				} else {
					AlertClassTip('认证失败');
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

});
