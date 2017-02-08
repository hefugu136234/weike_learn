$(function() {
	var $name = $('#name');
	var $phone = $('#phone');
	var $city = $('#city');
	var $city_text = $($city.find('.select-text').get(0));
	var $district = $('#district');
	var $district_text = $($district.find('.select-text').get(0));
	var $address = $('#address');
	var $item_uuid = $('#item_uuid');

	// 添加地址
	$('#add_address_submit').click(function(e) {
		e.preventDefault();
		var $this = $(this);
		var name = $name.val();
		var phone = $phone.val();
		var city = $city_text.data('uuid');
		var district = $district_text.data('uuid');
		var address = $address.val();
		if (!name) {
			AlertClassTip('请填写收件人姓名！');
			return false;
		}
		if (!phone) {
			AlertClassTip('请填写收件人的手机号码！');
			return false;
		}
		if (!verify_phone.test(phone)) {
			AlertClassTip('请填写正确的手机号码！');
			return false;
		}
		if (!city) {
			AlertClassTip('请选择所在城市！');
			return false;
		}
//		if (!district) {
//			AlertClassTip('请选择所在区域！');
//			return false;
//		}
		if (!address) {
			AlertClassTip('请填写详细地址！');
			return false;
		}
		$this.prop('disabled', true);
		$.post('/api/webchat/shop/address/add/data', {
			'name' : name,
			'phone' : phone,
			'cityUuid' : city,
			'districtUuid' : district,
			'address' : address
		}, function(data) {
			$this.prop('disabled', false);
			if (data.status == 'success') {
				window.location.href = '/api/webchat/shop/address/list/page';
			} else {
				if (!!data.message) {
					AlertClassTip(data.message);
				} else {
					AlertClassTip("新增失败！");
				}
			}
		});
	});

	// 设置默认地址
	$('#set_default').click(function(e) {
		e.preventDefault();
		var $this = $(this);
		if($this.hasClass('current')){
            return false;
        }
		var message = '您确定将此地址设置为默认地址？';
		var uuid = $item_uuid.val();
		AlertClassTip(message, {}, function(flag) {
			if (flag == 1) {
				$this.prop('disabled', true);
				$.post('/api/webchat/shop/address/set/default', {
					'uuid' : uuid
				}, function(data) {
					$this.prop('disabled', false);
					if (data.status == 'success') {
						$this.addClass('current');
						AlertClassTip('您已设置成功！');
					} else {
						AlertClassTip(data.message);
					}
				});
			}
		});
	});

	// 更新地址
	$('#update_address_submit').click(function(e) {
		e.preventDefault();
		var $this = $(this);
		var uuid = $item_uuid.val();
		var name = $name.val();
		var phone = $phone.val();
		var city = $city_text.data('uuid');
		var district = $district_text.data('uuid');
		var address = $address.val();
		if (!name) {
			AlertClassTip('请填写收件人姓名！');
			return false;
		}
		if (!phone) {
			AlertClassTip('请填写收件人的手机号码！');
			return false;
		}
		if (!verify_phone.test(phone)) {
			AlertClassTip('请填写正确的手机号码！');
			return false;
		}
		if (!city) {
			AlertClassTip('请选择所在城市！');
			return false;
		}
		if (!address) {
			AlertClassTip('请填写详细地址！');
			return false;
		}
		$this.prop('disabled', true);
		$.post('/api/webchat/shop/address/update/data', {
			'uuid' : uuid,
			'name' : name,
			'phone' : phone,
			'cityUuid' : city,
			'districtUuid' : district,
			'address' : address
		}, function(data) {
			$this.prop('disabled', false);
			if (data.status == 'success') {
				window.location.href = '/api/webchat/shop/address/list/page';
			} else {
				if (!!data.message) {
					AlertClassTip(data.message);
				} else {
					AlertClassTip("编辑失败！");
				}
			}
		})
	});

	// 获取省市数据
	$.get('/info/getLocation', function(data) {
		var pro_data = jQuery.parseJSON(data);
		var select_obj = ThreeStageCascade(pro_data, 2);
		var clickBindThing = function(data) {
			select_obj.hide();
			if (data.city_data.uuid != '') {
				var name = data.privince_data.name + '-'
						+ data.city_data.name;
				$city_text.data('uuid', data.city_data.uuid);
				$city_text.html(name);
				if(!data.district_data.uuid){
					data.district_data.name='';
				}
				$district_text.data('uuid', data.district_data.uuid);
				$district_text.html(data.district_data.name);
			}
		};
		$city.click(function() {
			select_obj.show(function(data) {
				clickBindThing(data);
			});
		});
		$district.click(function() {
			select_obj.show(function(data) {
				clickBindThing(data);
			});
		});
	});

});
