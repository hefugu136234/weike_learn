$(document).ready(function() {
	showActive([ 'assets-mgr', 'hospital_mgr_nav' ]);
	
	$('#hospital_update_form').submit(function(event) {
		event.preventDefault();
		
		if('0' == $('#hospital_province').val()){
			alert('请选择省份!');
			return false;
		}
		if('0' == $('#hospital_city').val()){
			alert('请选择城市!');
			return false;
		}
		if('0' == $('#hospital_grade').val()){
			alert('请选择医院等级!');
			return false;
		}
		if('' == $('#hospital_name').val()){
			alert('请填写医院名称!');
			return false;
		}
		
		/*var hospitalMobile = $('#hospital_mobile').val();
		var parten = /[0-9]{6,15}/;
		if(!(parten.test(hospitalMobile)) && '' !== hospitalMobile){
			alert('请填写正确的电话号码!');
			return false;
		}*/
		
		var $form = $(this);
		$.post('/hospital/mgr/update/', $form.serialize(), function() {
		}).always(function(data) {
			if ('success' == data['status']) {
				alert("更新成功");
				window.location.href = '/project/hospital/mgr';
			} else {
				alert(data['message']);
			}
		});
	});
})

function changeProvince(uuid) {
	clearContent($('#hospital_city'));
	if (uuid != 0) {
		$.getJSON('/api/webchat/city/' + uuid, function(data) {
			var city = $('#hospital_city');
			if (data.itemList) {
				$.each(data.itemList, function(index, item) {
					var option = '<option value=' + item.uuid + '>' + item.name
							+ '</option>';
					city.append(option);
				});
			} else {
				alert("城市数据加载错误");
			}
		});
	}
}

function clearContent(parent) {
	parent.empty();
	parent.append('<option value="0">请选择</option>');
}

function buildOption(vo,elem,msg){
	var itemList=vo.itemList;
	if(!!itemList){
		$.each(itemList, function(index, item) {
			var option = '<option value='+item.uuid+'>' + item.name
					+ '</option>';
			elem.append(option);
		});
	}else{
		alert(msg);
	}
}