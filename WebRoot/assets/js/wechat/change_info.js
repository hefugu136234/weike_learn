$(function() {

	// 退出登录
	$('#out_login').click(function(event) {
		event.preventDefault();
		AlertClassTip('是否要退出登录？',{},function(flag){
			if(flag==1){
				$.post('/api/webchat/out/login', function(data) {
					if (data.status == 'success') {
						location.href = '/api/webchat/page/login';
					} else {
						AlertClassTip(data.message);
					}
				});
			}
		});
	});

	var user_type = parseInt($('#user_type').val());
	var $city = $('#city');
	var $city_text = $($city.find('.select-text').get(0));
	var $hosipital = $('#hosipital');
	var $hosipital_text = $($hosipital.find('.select-text').get(0));
	var $department = $('#department');
	var $department_text = $($department.find('.select-text').get(0));
	var $company = $('#company');
	var $company_text = $($company.find('.select-text').get(0));
	if (user_type == 0) {
		// 医生
		// 获取省市数据
		$.get('/info/getLocation', function(data) {
			var pro_data = jQuery.parseJSON(data);
			var select_obj = ThreeStageCascade(pro_data, 1);
			var clickBindThing = function(data) {
				select_obj.hide();
				if (data.hosipital_data.uuid != '') {
					var name = data.privince_data.name + '-'
							+ data.city_data.name;
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

	} else if (user_type == 1) {
		// 企业
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
	}

	$('#submitBtn').click(function(e) {
		e.preventDefault();
		var $this = $(this);
		var name = $('#name').val();
		var city = $city_text.data('uuid');
		var hosipital = $hosipital_text.data('uuid');
		var office = $department_text.data('uuid');
		var company = $company_text.data('uuid');
		var professor = $('#professor').val();
		if (name == '') {
			AlertClassTip('请填写您的姓名！');
			return false;
		}
		if (user_type == 0) {
			// 医生
			if (!city) {
				AlertClassTip('请选择城市！');
				return false;
			}
			if (!hosipital) {
				AlertClassTip('请选择医院！');
				return false;
			}
			if (!office) {
				AlertClassTip('请选择科室！');
				return false;
			}
		} else if (user_type == 1) {
			if (!company) {
				AlertClassTip('请选择企业名称!');
				return false;
			}
		}
		$this.prop('disabled', true);
		$.post('/api/webchat/change/info', {
        	'name':name,
        	'type':user_type,
        	'city':city,
        	'hosipital':hosipital,
        	'office':office,
        	'professor':professor,
        	'company':company
        }, function(data) {
        	$this.prop('disabled', false);
        	if(data.status=='success'){
        		window.location.href = '/api/webchat/my/center';
        	}else{
        		if(!!data.message){
        			AlertClassTip(data.message);
             	  }else{
             		 AlertClassTip("修改失败");
             	  }
        	}
		});

      });

});
